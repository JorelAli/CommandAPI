package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.Logging;
import dev.jorel.commandapi.annotations.Utils;

public abstract class CommandElement {
	
	public Logging logging;
	
	public CommandElement(Logging logging) {
		this.logging = logging;
	}

	/**
	 * Emits the current ADT.
	 * 
	 * @param out the print writer to write to
	 */
	public abstract void emit(PrintWriter out, int currentIndentation);

	public int indentation;

	public String indentation() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indentation; i++) {
			builder.append("    ");
		}
		return builder.toString();
	};

	public void indent() {
		indentation++;
	}

	public void dedent() {
		indentation--;
		if (indentation < 0) {
			indentation = 0;
		}
	}

	public boolean emitPermission(PrintWriter out, CommandPermission permission) {
		// TODO: This case should never occur
		if(permission == null) {
			return false;
		}
		if (permission.equals(CommandPermission.NONE)) {
			// Do nothing
			return false;
		} else if (permission.equals(CommandPermission.OP)) {
			out.println();
			out.print(indentation() + ".withPermission(CommandPermission.OP)");
			return true;
		} else if(permission.getPermission().isPresent()) {
			out.println();
			if (permission.isNegated()) {
				out.print(indentation() + ".withoutPermission(\"");
			} else {
				out.print(indentation() + ".withPermission(\"");
			}
			out.print(permission.getPermission().get());
			out.println("\")");
			return true;
		} else {
			return false;
		}
	}

	public boolean emitSuggestion(PrintWriter out, Optional<SuggestionClass> suggestions, CommandData parent) {
		if (suggestions.isPresent()) {
			SuggestionClass suggestion = suggestions.get();

			if (suggestion.isSafeSuggestions()) {
				// TODO: Semantics must check that whatever we're applying these suggestions to
				// implements SafeOverrideableArgument.
				// TODO: Semantics must check that the type argument of
				// SafeOverrideableArgument<?> matches this.primitive
				out.print(indentation() + ".replaceSafeSuggestions(");
			} else {
				out.print(indentation() + ".replaceSuggestions(");
			}

			// If the suggestion implementation class is a top-level class, we can literally
			// just instantiate it
			if (suggestion.typeElement().getNestingKind() == NestingKind.TOP_LEVEL) {
				out.print("new " + suggestion.typeElement().getSimpleName() + "().get())");
			} else {
				// If it's not a top-level class, we have to assume it's declared in some class
				// within @Command or @Subcommand. TODO: This should be checked during
				// semantics!

				// We need to derive the path of classes required to get to this suggestion
				// class, from the top-level @Command class
				CommandData topLevelCommand = parent;
				while (topLevelCommand.getParent() != null) {
					topLevelCommand = topLevelCommand.getParent();
				}

				Deque<TypeElement> typeStack = new ArrayDeque<>();
				

				TypeElement currentTypeElement = suggestion.typeElement();
				Types types = suggestion.processingEnv().getTypeUtils();

				while (!types.isSameType(currentTypeElement.asType(), topLevelCommand.getTypeElement().asType())) {
					typeStack.push(currentTypeElement);

					if (currentTypeElement.getNestingKind() == NestingKind.TOP_LEVEL) {
						// Stop, otherwise we'll keep going forever.
						// TODO: Error here
						break;
					} else {
						// TODO: We've assumed it's a type element. It's possible that the enclosing
						// element is an executable element if we've declared this class inside a
						// function (very very improbable, but possible!)
						currentTypeElement = (TypeElement) currentTypeElement.getEnclosingElement();
					}
				}

				out.print(Utils.COMMAND_VAR_NAME);
				for(TypeElement typeElement : typeStack) {
					out.print(".new " + typeElement.getSimpleName() + "()");
				}
				out.print(".get())");
			}
		}
		return suggestions.isPresent();
	}

}
