package dev.jorel.commandapi.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;

public class Context {
	
	private ProcessingEnvironment processingEnv;
	private Logging logging;
	
	private List<TypeElement> subcommandClasses;
	private List<ExecutableElement> subcommandMethods;
	private List<ExecutableElement> suggestionMethods;
	
	// Construct some context :)
	public Context(Element classElement, ProcessingEnvironment roundEnv, Logging logging) {
		this.processingEnv = roundEnv;
		this.logging = logging;
		
		this.subcommandClasses = new ArrayList<>();
		this.subcommandMethods = new ArrayList<>();
		this.suggestionMethods = new ArrayList<>();
		
		parseCommandClass(classElement);
	}

	private void parseCommandClass(Element classElement) {
		if(classElement.getKind() != ElementKind.CLASS) {
			logging.complain(classElement, "The @Command annotation must go on a class");
		} else {
			// Parse @Command annotations
			TypeElement typeElement = (TypeElement) classElement;

			Command commandAnnotation = typeElement.getAnnotation(Command.class);
			logging.info(typeElement, "Parsing '" + Arrays.toString(commandAnnotation.value()) + "' class");

			if(AnnotationValidator.validateCommand(typeElement, commandAnnotation, logging)) {
				for(Element typeElementChild : typeElement.getEnclosedElements()) {
					switch(typeElementChild.getKind()) {
						case CLASS:
							// @Command classes with classes with @Command is invalid
							if(typeElementChild.getAnnotation(Command.class) != null) {
								logging.complain(typeElementChild, "Inner class of a @Command class cannot contain another @Command class");
							}
							
							// Parse @Command classes with classes with @Subcommand
							if(typeElementChild.getAnnotation(Subcommand.class) != null) {
								parseSubcommandClass((TypeElement) typeElementChild);
							}

							// Parse @Suggestion classes
							if(typeElementChild.getAnnotation(Suggestion.class) != null) {
								parseSuggestionMethod((TypeElement) typeElementChild);
							}
							break;
						case METHOD:
							// Parse methods with @Subcommand
							if(typeElementChild.getAnnotation(Subcommand.class) != null) {
								parseSubcommandMethod((ExecutableElement) typeElementChild);
							}
							break;
						default:
							// We don't care about this method :)
							break;
					}
				}
			}
		}
	}

	private void parseSubcommandClass(TypeElement typeElement) {
		Subcommand subcommandAnnotation = typeElement.getAnnotation(Subcommand.class);
		logging.info(typeElement, "Parsing '" + Arrays.toString(subcommandAnnotation.value()) + "' class");
	}

	private void parseSubcommandMethod(ExecutableElement methodElement) {
		Subcommand subcommandAnnotation = methodElement.getAnnotation(Subcommand.class);
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
	}

	private void parseSuggestionMethod(TypeElement typeElement) {
		Suggestion suggestionAnnotation = typeElement.getAnnotation(Suggestion.class);
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
		
	}

	public static Map<Element, Context> generateContexts(Set<? extends Element> commandClasses,
			ProcessingEnvironment processingEnv, Logging logging) {
		
		Map<Element, Context> contextMap = new HashMap<>();
		for(Element classElement : commandClasses) {
			
			contextMap.put(classElement, new Context(classElement, processingEnv, logging));
		}
		
		return contextMap;
	}

}
