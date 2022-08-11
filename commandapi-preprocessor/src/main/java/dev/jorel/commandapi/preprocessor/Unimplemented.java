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
package dev.jorel.commandapi.preprocessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Literally just a fancy comment that I can put next to stuff to know why
 * something wasn't implemented. Retention Policy is source, so it doesn't get
 * carried over to the compiled code. Again, literally just a fancy comment.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(UnimplementedRepeatable.class)
public @interface Unimplemented {

	/**
	 * A reason why this method hasn't been implemented
	 * 
	 * @return a reason why this method hasn't been implemented
	 */
	REASON[] because();

	/**
	 * @return description
	 */
	String from() default "";

	/**
	 * @return description
	 */
	String to() default "";

	/**
	 * @return description
	 */
	String in() default "";

	/**
	 * @return description
	 */
	String introducedIn() default "";

	/**
	 * @return description
	 */
	String classNamed() default "";
	
	/**
	 * @return description
	 */
	String info() default "";

	/**
	 * The reason why this method was unimplemented
	 */
	enum REASON {
		/**
		 * This method requires importing {@code org.bukkit.craftbukkit}
		 */
		REQUIRES_CRAFTBUKKIT,

		/**
		 * A method or field name changed
		 */
		NAME_CHANGED,

		/**
		 * The implementation of this feature is specific to a given Minecraft version
		 */
		VERSION_SPECIFIC_IMPLEMENTATION,

		/**
		 * Requires access to CommandSourceStack (CommandListenerWrapper)
		 */
		REQUIRES_CSS,

		/**
		 * Requires access to the NMS Minecraft server
		 */
		REQUIRES_MINECRAFT_SERVER
	}

}