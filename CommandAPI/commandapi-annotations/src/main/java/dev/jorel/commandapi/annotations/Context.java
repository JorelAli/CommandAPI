package dev.jorel.commandapi.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;

public class Context {
	
	private RoundEnvironment roundEnv;
	private Logging logging;
	
	private List<TypeElement> subcommandClasses;
	private List<ExecutableElement> subcommandMethods;
	private List<ExecutableElement> suggestionMethods;
	
	// Construct some context :)
	public Context(Element classElement, RoundEnvironment roundEnv, Logging logging) {
		this.roundEnv = roundEnv;
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
							break;
						case METHOD:
							// Parse methods with @Subcommand
							if(typeElementChild.getAnnotation(Subcommand.class) != null) {
								parseSubcommandMethod((ExecutableElement) typeElementChild);
							}
							
							if(typeElementChild.getAnnotation(Suggestion.class) != null) {
								parseSuggestionMethod((ExecutableElement) typeElementChild);
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
		
		logging.info(parameters.get(0).getSimpleName().toString());
		logging.info(parameters.get(0).asType().toString());
// org.bukkit.command.CommandSender
		
//			String[] firstParam = methodType.getParameterTypes().get(0).toString().split("\\.");
//			out.print(indent(indent));
//			switch (firstParam[firstParam.length - 1]) {
//			case "Player" -> out.print(".executesPlayer");
//			case "ConsoleCommandSender" -> out.print(".executesConsole");
//			case "BlockCommandSender" -> out.print(".executesCommandBlock");
//			case "ProxiedCommandSender" -> out.print(".executesProxy");
//			case "NativeProxyCommandSender" -> out.print(".executesNative");
//			case "Entity" -> out.print(".executesEntity");
//			case "CommandSender" -> out.print(".executes");
//			default -> out.print(".executes");
	}

	private void parseSuggestionMethod(ExecutableElement typeElementChild) {
		// TODO Auto-generated method stub
		
	}

	public static Map<Element, Context> generateContexts(Set<? extends Element> commandClasses,
			RoundEnvironment roundEnv, Logging logging) {
		
		Map<Element, Context> contextMap = new HashMap<>();
		for(Element classElement : commandClasses) {
			
			contextMap.put(classElement, new Context(classElement, roundEnv, logging));
		}
		
		return contextMap;
	}

}
