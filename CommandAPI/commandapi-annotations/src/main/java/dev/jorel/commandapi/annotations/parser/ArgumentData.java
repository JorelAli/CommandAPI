package dev.jorel.commandapi.annotations.parser;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.arguments.Primitive;

public class ArgumentData {
	
	private final VariableElement varElement;
	
	private final String[] primitiveTypes;
	
	private final Annotation argumentAnnotation;
	
	// The relevant class for suggestions that @Suggests points to.
	// This isn't populated in the constructor, instead this is populated during the linking step // TODO: Implement during linking
	private final Optional<TypeElement> suggestions;
	
	// Permission for this argument, if any. Implemented from @NeedsOp or @Permission
	private final CommandPermission permission;
	
	// The argument node's name. Retrieved from the parameter/field name, or @NodeName annotation if declared
	private final String nodeName;
	
	public ArgumentData(VariableElement varElement, Annotation annotation, CommandPermission permission, String nodeName) {
		this.varElement = varElement;
		
		Primitive primitive = annotation.getClass().getAnnotation(Primitive.class);
		this.primitiveTypes = primitive.value();
		
		this.argumentAnnotation = annotation;
		this.suggestions = Optional.empty();
		this.permission = permission;
		this.nodeName = nodeName;
	}

}
