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
import javax.lang.model.element.AnnotationValue;
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
import dev.jorel.commandapi.annotations.annotations.Suggestion;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.annotations.WithoutPermission;
import dev.jorel.commandapi.annotations.parser.ArgumentData;
import dev.jorel.commandapi.annotations.parser.CommandData;

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

					// Parse @Suggestion classes
					typeElementChild.getAnnotation(Suggestion.class);
					if(typeElementChild.getAnnotation(Suggestion.class) != null) {
						parseSuggestionMethod((TypeElement) typeElementChild);
					}
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
		if(varElement.getAnnotation(Suggests.class) != null) {
			suggests = Optional.of(Utils.getAnnotationClassValue(varElement, Suggests.class));
		} else {
			suggests = Optional.empty();
		}

		// Add to command data
		commandData.addArgument(new ArgumentData(varElement, annotation, permission, nodeName, suggests));
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

	private void parseSuggestionMethod(TypeElement typeElement) {
		logging.info(typeElement, "Parsing '" + typeElement.getSimpleName() + "' class");
		
		// java.util.function.Supplier<dev.jorel.commandapi.arguments.ArgumentSuggestions>
		// java.util.function.Supplier<dev.jorel.commandapi.arguments.SafeSuggestions<org.bukkit.Location>>

		// Get the interfaces (e.g. Supplier<ArgumentSuggestions> or
		// Supplier<SafeSuggestions<Location>>)
		
		TypeMirror supplierMirror = null;
		
		for(TypeMirror mirror : typeElement.getInterfaces()) {
			
			final TypeMirror supplier = processingEnv.getElementUtils().getTypeElement(Supplier.class.getCanonicalName()).asType();
			Types types = processingEnv.getTypeUtils();
			if(!types.isSameType(types.erasure(supplier), types.erasure(mirror))) {
				logging.complain(types.asElement(mirror), "@Suggestion must implement java.util.function.Supplier");
			}
			
			supplierMirror = mirror; 

			processingEnv.getTypeUtils().erasure(supplier);
			System.out.println("Supplier: " + supplier );
			System.out.println("Mirror: " + mirror);
			System.out.println(processingEnv.getTypeUtils().isSameType(mirror, supplier));
			
			// We want to inspect the generics (e.g. ArgumentSuggestions or
			// SafeSuggestions<Location>
			if(mirror instanceof DeclaredType declaredType) {
				for(TypeMirror typeArgument : declaredType.getTypeArguments()) {
					System.out.println(typeArgument);
				}
			}
		}
		
		if(supplierMirror == null) {
			logging.complain(typeElement, "@Suggestion must implement java.util.function.Supplier");
		}
		logging.info(typeElement.getInterfaces());
		
		// SuggestionClass suggestion = new SuggestionClass();
		// commandData.addSuggestionClass(suggestion);		
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
