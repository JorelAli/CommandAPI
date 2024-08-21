/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * A logger for the CommandAPI annotation system annotation processor
 */
public class Logging {

	private final ProcessingEnvironment processingEnv;

	public Logging(ProcessingEnvironment processor) {
		this.processingEnv = processor;
	}

	public void complain(Element element, String message) {
		System.out.println(message + " at " + element);
		getMessager().printMessage(Kind.ERROR, message, element);
	}

	public void complain(String message) {
		System.out.println(message);
		getMessager().printMessage(Kind.ERROR, message);
	}

	public void warn(Element element, String message) {
		getMessager().printMessage(Kind.MANDATORY_WARNING, message, element);
	}

	public void info(Element element, String message) {
		getMessager().printMessage(Kind.NOTE, message, element);
	}

	public void info(Object message) {
		getMessager().printMessage(Kind.NOTE, String.valueOf(message));
	}

	public Messager getMessager() {
		return processingEnv.getMessager();
	}

	/*
		typeElement.getAnnotationMirrors().forEach(mirror -> {
			mirror.getElementValues();
			logging.getMessager().printMessage(Kind.MANDATORY_WARNING, mirror.getElementValues().toString());
			logging.getMessager().printMessage(Kind.MANDATORY_WARNING, mirror.getElementValues().toString(), typeElement, (AnnotationMirror)mirror);
		});
	 */

}
