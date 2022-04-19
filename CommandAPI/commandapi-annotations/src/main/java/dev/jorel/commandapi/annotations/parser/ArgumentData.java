package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.ALiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ALocation2DArgument;
import dev.jorel.commandapi.annotations.arguments.ALocationArgument;
import dev.jorel.commandapi.annotations.arguments.ALongArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AScoreHolderArgument;
import dev.jorel.commandapi.annotations.arguments.Primitive;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

public class ArgumentData extends CommandElement {

	private final VariableElement varElement;

	private final String[] primitiveTypes;

	private final Annotation argumentAnnotation;

	/**
	 * The relevant class for suggestions that @Suggests points to. This isn't
	 * populated in the constructor, instead this is populated during the linking
	 * step // TODO: Implement during linking
	 */
	private Optional<SuggestionClass> suggestions;

	/**
	 * Permission for this argument, if any. Implemented from @NeedsOp
	 * or @Permission
	 */
	private final CommandPermission permission;

	/**
	 * The argument node's name. Retrieved from the parameter/field name,
	 * or @NodeName annotation if declared
	 */
	private final String nodeName;

	/**
	 * The class that this argument @Suggests, if any. We should assume that this
	 * type element is something of type Class<? extends Supplier<?>>.
	 */
	private final Optional<TypeMirror> suggests;

	public ArgumentData(VariableElement varElement, Annotation annotation, CommandPermission permission,
			String nodeName, Optional<TypeMirror> suggests, Optional<SuggestionClass> suggestions) {
		this.varElement = varElement;
		this.primitiveTypes = annotation.annotationType().getAnnotation(Primitive.class).value();
		this.argumentAnnotation = annotation;
		this.suggestions = Optional.empty();
		this.permission = permission;
		this.nodeName = nodeName;
		this.suggests = suggests;
		this.suggestions = suggestions;
	}

	/**
	 * If the suggestions parameter is suitable for this argument, it links it.
	 * Otherwise, it doesn't. Returns true if linking was successful 
	 * @param suggestions
	 */
	public boolean validateSuggestionsClass(ProcessingEnvironment processingEnv) {
		// If this argument doesn't have @Suggests, we don't care
		if(suggests.isEmpty() || this.suggestions.isEmpty()) {
			return false;
		}
		
		SuggestionClass suggestions = this.suggestions.get();

		// This is already validated in Context:
		// Check that @Suggests and SuggestionClass are matching the right class. If not, we can't link it
//		if(!processingEnv.getTypeUtils().isSameType(suggests.get(), suggestions.typeElement().asType())) {
//			return false;
//		}

		if(suggestions.isSafeSuggestions()) {
			// Safe suggestions requires type checking. Ensure that the types match
			// TODO: We actually have to do a little bit more complex type checking here. If
			// this.primitiveTypes.length > 1, then we have to individually check what the
			// type is corresponding to the type of this argument (e.g. Player, Players).
			// This is where we use varElement
			for(String primitive : this.primitiveTypes) {
				if(suggestions.primitive().equals(primitive)) {
					this.suggestions = Optional.of(suggestions);
					return true;
				}
			}
		} else {
			// Normal suggestions, we can link against it with no issues
			this.suggestions = Optional.of(suggestions);
			return true;
		}
		
		return false;
	}

	@Override
	public void emit(PrintWriter out) {
		out.print(indentation() + ".withArguments(new ");

		// We're assuming that the name of the argument MUST be "A" + the same name
		out.print(argumentAnnotation.annotationType().getSimpleName().substring(1));

		// Handle parameters
		out.print("(");

		// Node name
		if (argumentAnnotation instanceof AMultiLiteralArgument || argumentAnnotation instanceof ALiteralArgument) {
			// Ignore node name for MultiLiteralArgument and LiteralArgument
		} else {
			out.print("\"");
			out.print(nodeName);
			out.print("\"");
		}

		// Number arguments
		if (argumentAnnotation instanceof AIntegerArgument argument) {
			out.print(", " + argument.min() + ", " + argument.max());
		} else if (argumentAnnotation instanceof ALongArgument argument) {
			out.print(", " + argument.min() + "L, " + argument.max() + "L");
		} else if (argumentAnnotation instanceof AFloatArgument argument) {
			out.print(", " + argument.min() + "F, " + argument.max() + "F");
		} else if (argumentAnnotation instanceof ADoubleArgument argument) {
			out.print(", " + argument.min() + "D, " + argument.max() + "D");
		}

		// Non-number arguments
		else if (argumentAnnotation instanceof ALocation2DArgument argument) {
			out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
		} else if (argumentAnnotation instanceof ALocationArgument argument) {
			out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
		} else if (argumentAnnotation instanceof AEntitySelectorArgument argument) {
			out.print(", " + EntitySelector.class.getSimpleName() + "." + argument.value().toString());
		} else if (argumentAnnotation instanceof AScoreHolderArgument argument) {
			out.print(", " + ScoreHolderType.class.getSimpleName() + "." + argument.value().toString());
		} else if (argumentAnnotation instanceof AMultiLiteralArgument argument) {
			out.print(Arrays.stream(argument.value()).map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
		} else if (argumentAnnotation instanceof ALiteralArgument argument) {
			out.print("\"");
			out.print(argument.value());
			out.print("\"");
		}

		out.print(")"); // End argument constructor parameters

		// Permissions
		if (permission.equals(CommandPermission.NONE)) {
			// Do nothing
		} else if (permission.equals(CommandPermission.OP)) {
			out.print(".withPermission(CommandPermission.OP)");
		} else {
			if (permission.isNegated()) {
				out.print(".withoutPermission(\"");
			} else {
				out.print(".withPermission(\"");
			}
			out.print(permission.getPermission());
			out.println("\")");
		}

		// Suggestions
		if (suggestions.isPresent()) {
			SuggestionClass suggestion = suggestions.get();
			if (suggestion.isSafeSuggestions()) {
				// TODO: Semantics must check that whatever we're applying these suggestions to
				// implements SafeOverrideableArgument.
				// TODO: Semantics must check that the type argument of
				// SafeOverrideableArgument<?> matches this.primitive
				out.print(".replaceSafeSuggestions(new ");
			} else {
				out.print(".replaceSuggestions(new ");
			}

			out.print(suggestion.typeElement().getQualifiedName());
			out.print("().get())");
		}

		// Argument listing. Only applies to @LiteralArgument
		if (argumentAnnotation instanceof ALiteralArgument) {
			out.print(".setListed(true)");
		}

		out.println(")"); // End .withArguments
	}

}
