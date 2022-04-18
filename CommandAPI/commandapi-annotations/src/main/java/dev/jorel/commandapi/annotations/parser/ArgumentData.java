package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.VariableElement;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.annotations.Permission;
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
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

public class ArgumentData extends Emittable {

	private final VariableElement varElement;

	private final String[] primitiveTypes;

	private final Annotation argumentAnnotation;

	// The relevant class for suggestions that @Suggests points to.
	// This isn't populated in the constructor, instead this is populated during the
	// linking step // TODO: Implement during linking
	private final Optional<SuggestionClass> suggestions;

	// Permission for this argument, if any. Implemented from @NeedsOp or
	// @Permission
	private final CommandPermission permission;

	// The argument node's name. Retrieved from the parameter/field name, or
	// @NodeName annotation if declared
	private final String nodeName;

	public ArgumentData(VariableElement varElement, Annotation annotation, CommandPermission permission,
			String nodeName) {
		this.varElement = varElement;
		this.primitiveTypes = annotation.getClass().getAnnotation(Primitive.class).value();
		this.argumentAnnotation = annotation;
		this.suggestions = Optional.empty();
		this.permission = permission;
		this.nodeName = nodeName;
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
		if(permission.equals(CommandPermission.NONE)) {
			// Do nothing
		} else if(permission.equals(CommandPermission.OP)) {
			out.print(".withPermission(CommandPermission.OP)");
		} else {
			out.print(".withPermission(\"");
			// TODO: We need to take into account whether this is negated or not
			//out.print(permission.toString());
			out.println("\")");
		}
		
		if (argumentAnnotation instanceof ALiteralArgument) {
			out.print(".setListed(true)");
		}

		out.println(")"); // End .withArguments
	}

}
