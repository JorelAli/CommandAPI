package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.annotations.NodeName;
import dev.jorel.commandapi.annotations.annotations.Permission;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.annotations.WithoutPermission;
import dev.jorel.commandapi.annotations.parser.ArgumentData;
import dev.jorel.commandapi.annotations.parser.CommandData;
import dev.jorel.commandapi.annotations.parser.SuggestionClass;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.SafeSuggestions;

public class Context {
	
	private ProcessingEnvironment processingEnv;
	private Logging logging;
	
	private CommandData commandData;
	
	// Construct some context :)
	public Context(TypeElement classElement, ProcessingEnvironment processingEnv, Logging logging, boolean subCommandClass) {
		this.processingEnv = processingEnv;
		this.logging = logging;
		
		this.commandData = new CommandData();
		
		parseCommandClass(classElement, subCommandClass);
	}

	private void parseCommandClass(TypeElement typeElement, boolean subCommandClass) {
		
		if(subCommandClass) {
			Subcommand subcommandAnnotation = typeElement.getAnnotation(Subcommand.class);
			logging.info(typeElement, "Parsing '" + Arrays.toString(subcommandAnnotation.value()) + "' class");
		} else {
			Command commandAnnotation = typeElement.getAnnotation(Command.class);
			logging.info(typeElement, "Parsing '" + Arrays.toString(commandAnnotation.value()) + "' class");

			// Help only exists for @Command
			parseHelp(typeElement);
			
			if(!AnnotationValidator.validateCommand(typeElement, commandAnnotation, logging)) {
				return;
			}
		}
		
		// Parse annotations on inner fields, classes and methods
		Annotation annotation = null;
		for(Element typeElementChild : typeElement.getEnclosedElements()) {
			switch(typeElementChild.getKind()) {
				case CLASS:
					// @Command classes with classes with @Command is invalid
					if(typeElementChild.getAnnotation(Command.class) != null) {
						logging.complain(typeElementChild, "Inner class of a @Command class cannot contain another @Command class");
					}
					
					// Parse @Subcommand classes
					annotation = typeElementChild.getAnnotation(Subcommand.class);
					if(annotation != null) {
						Context subCommandContext = new Context((TypeElement) typeElementChild, processingEnv, logging, true);
						commandData.addSubcommandClass(subCommandContext.commandData);
					}

					// DON'T parse @Suggestion - this annotation is effectively redundant, we
					// get all of the information about this from @Suggests, by linking to the
					// relevant type mirror instead :)
					// Parse @Suggestion classes
//					typeElementChild.getAnnotation(Suggestion.class);
//					if(typeElementChild.getAnnotation(Suggestion.class) != null) {
//						typeCheckSuggestionClass((TypeElement) typeElementChild);
//					}
					break;
				case METHOD:
					// Parse methods with @Subcommand
					annotation = typeElementChild.getAnnotation(Subcommand.class); 
					if(annotation != null) {
						parseSubcommandMethod((ExecutableElement) typeElementChild, (Subcommand) annotation);
					}
					break;
				case FIELD:
					annotation = Utils.getArgumentAnnotation(typeElementChild);
					if(annotation != null) {
						parseArgumentField((VariableElement) typeElementChild, annotation);
					}
					break;
				default:
					// We don't care about this element :)
					break;
			}
		}
	}

	private void parseHelp(TypeElement typeElement) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses argument fields - any class fields or method parameters with any argument annotation declared in Annotations.ARGUEMNT_ANNOTATIONS
	 */
	private void parseArgumentField(VariableElement varElement, Annotation annotation) {
		// Validate
		AnnotationValidator.validatePermissions(varElement, logging);
		
		// Parse permissions
		final CommandPermission permission;
		if(varElement.getAnnotation(NeedsOp.class) != null) {
			permission = CommandPermission.OP;
		} else if(varElement.getAnnotation(Permission.class) != null) {
			permission = CommandPermission.fromString(varElement.getAnnotation(Permission.class).value());
		} else if(varElement.getAnnotation(WithoutPermission.class) != null) {
			permission = CommandPermission.fromString(varElement.getAnnotation(WithoutPermission.class).value()).negate();
		} else {
			permission = CommandPermission.NONE;
		}
		
		// Parse argument node name
		final String nodeName;
		if(varElement.getAnnotation(NodeName.class) != null) {
			nodeName = varElement.getAnnotation(NodeName.class).value();
		} else {
			nodeName = varElement.getSimpleName().toString();
		}
		
		// Parse suggestions, via @Suggests
		final Optional<TypeMirror> suggests;
		final Optional<SuggestionClass> suggestionsClass;
		if(varElement.getAnnotation(Suggests.class) != null) {
			TypeMirror suggestsMirror = Utils.getAnnotationClassValue(varElement, Suggests.class);
			suggests = Optional.of(suggestsMirror);
			suggestionsClass = Optional.of(typeCheckSuggestionClass((TypeElement) processingEnv.getTypeUtils().asElement(suggestsMirror)));
		} else {
			suggests = Optional.empty();
			suggestionsClass = Optional.empty();
		}

		// Add to command data
		ArgumentData argumentData = new ArgumentData(varElement, annotation, permission, nodeName, suggests, suggestionsClass);
		if(suggestionsClass.isPresent()) {
			argumentData.validateSuggestionsClass(processingEnv);
		}
		commandData.addArgument(argumentData);
	}

	private void parseSubcommandMethod(ExecutableElement methodElement, Subcommand subcommandAnnotation) {
		logging.info(methodElement, "Parsing '" + Arrays.toString(subcommandAnnotation.value()) + "' method");
		
		// Check that there is at least one parameter
		List<? extends VariableElement> parameters = methodElement.getParameters();
		if(parameters.isEmpty()) {
			logging.complain(methodElement, "This method has no valid CommandSender parameter!");
			return;
		}

		// Check if the first parameter is a CommandSender (or instance thereof)
		try {
			if(!Utils.isValidSender(parameters.get(0).asType())) {
				logging.complain(methodElement, parameters.get(0).asType() + " is not a valid CommandSender");
				return;
			}
		} catch (ClassNotFoundException e) {
			logging.complain(methodElement, "Could not load class information for '" + parameters.get(0).asType() + "'");
			e.printStackTrace();
			return;
		}
		
		// commandData.addSubcommandMethod(null);
	}

	private SuggestionClass typeCheckSuggestionClass(TypeElement typeElement) {
		logging.info(typeElement, "Checking type signature for @Suggests class '" + typeElement.getSimpleName() + "' class");
		
		// java.util.function.Supplier<dev.jorel.commandapi.arguments.ArgumentSuggestions>
		// java.util.function.Supplier<dev.jorel.commandapi.arguments.SafeSuggestions<org.bukkit.Location>>

		// Get the interfaces (e.g. Supplier<ArgumentSuggestions> or
		// Supplier<SafeSuggestions<Location>>)
		
		Types types = processingEnv.getTypeUtils();
		TypeMirror argumentSuggestionsMirror = processingEnv.getElementUtils().getTypeElement(ArgumentSuggestions.class.getCanonicalName()).asType();
		TypeMirror safeSuggestionsMirror = processingEnv.getElementUtils().getTypeElement(SafeSuggestions.class.getCanonicalName()).asType();
		
		TypeMirror supplierMirror = null;
		
		for(TypeMirror mirror : typeElement.getInterfaces()) {
			
			final TypeMirror supplier = processingEnv.getElementUtils().getTypeElement(Supplier.class.getCanonicalName()).asType();
			if(!types.isSameType(types.erasure(supplier), types.erasure(mirror))) {
				logging.complain(types.asElement(mirror), "@Suggests class must implement java.util.function.Supplier directly");
			}
			
			supplierMirror = mirror;
		}
		
		if(supplierMirror == null) {
			logging.complain(typeElement, "@Suggests class must implement java.util.function.Supplier");
		}
		
		// We want to inspect the generics (e.g. ArgumentSuggestions or
		// SafeSuggestions<Location>
		if(supplierMirror instanceof DeclaredType declaredType) {
			for(TypeMirror typeArgument : declaredType.getTypeArguments()) {
				if(types.isSameType(argumentSuggestionsMirror, typeArgument)) {
					return new SuggestionClass(typeElement);
				} else if(types.isSameType(types.erasure(safeSuggestionsMirror), types.erasure(typeArgument))) {
					// TODO: More type checking here
					return new SuggestionClass(typeElement);
				} else {
					logging.complain(typeElement, "@Suggests class's Supplier has an invalid type argument. Expected Supplier<ArgumentSuggestions> or Supplier<SafeSuggestions>");
				}
			}
		}
		
		return null;
	}

	/**
	 * Main starting entrypoint - generates contexts for all elements with @Command
	 * @param commandClasses classes with @Command. Each element is assumed to be a TypeElement
	 * @param processingEnv the processing environment from the annotation processor
	 * @param logging logging class
	 * @return
	 */
	public static Map<Element, Context> generateContexts(Set<? extends Element> commandClasses,
			ProcessingEnvironment processingEnv, Logging logging) {
		Map<Element, Context> contextMap = new HashMap<>();
		for(Element classElement : commandClasses) {
			contextMap.put(classElement, new Context((TypeElement) classElement, processingEnv, logging, false));
		}
		return contextMap;
	}

}
