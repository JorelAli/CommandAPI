package dev.jorel.commandapi.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.StringParser;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;

public class CustomArgumentType_1_18_R2 implements ArgumentType<String> {

	public static final void register() {
		ArgumentTypes.register("custom", CustomArgumentType_1_18_R2.class, new CustomArgumentSerializer());
	}

	public Class<?> containingClass;
	public StringParser parsingFunction;

	public CustomArgumentType_1_18_R2(Class<?> containingClass, StringParser parsingFunction) {
		this.containingClass = containingClass;
		this.parsingFunction = parsingFunction;
	}

	public static String getString(final CommandContext<?> context, final String name) {
		return context.getArgument(name, String.class);
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return parsingFunction.parse(reader);
	}

	@Override
	public String toString() {
		return "custom(" + containingClass.getSimpleName() + ")";
	}

	static class CustomArgumentSerializer implements ArgumentSerializer<CustomArgumentType_1_18_R2> {

		@Override
		public void serializeToNetwork(CustomArgumentType_1_18_R2 argument, FriendlyByteBuf packetByteBuf) {

			// We need to write two byte arrays: The class that the lambda came from and the
			// lambda info itself
			
			String path = argument.containingClass.getName().replace('.', '/') + ".class";
			InputStream stream = argument.containingClass.getClassLoader().getResourceAsStream(path);
			
			try {
				ByteArrayOutputStream lambda = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(lambda);
				oos.writeObject(argument.parsingFunction);
				oos.flush();
				
				// Write the class name
				packetByteBuf.writeByteArray(argument.containingClass.getName().getBytes());

				// Write the class
				packetByteBuf.writeByteArray(stream.readAllBytes());
				
				// Write the lambda itself
				packetByteBuf.writeByteArray(lambda.toByteArray());
				
				stream.close();
				oos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("resource")
		@Override
		public CustomArgumentType_1_18_R2 deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
			String className = new String(packetByteBuf.readByteArray());
			System.out.println("Reading class " + className);
			byte[] classBytes = packetByteBuf.readByteArray();
			
			// Load the class. This HAS to be done before we unpack the lambda
			ByteClassLoader cl = new ByteClassLoader(new URL[0], getClass().getClassLoader(), Map.of(className, classBytes));
			Class<?> parserClass = null;
			try {
				parserClass = cl.findClass(className);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Found " + parserClass);
			
			// Read and unpack the lambda
			ByteArrayInputStream in = new ByteArrayInputStream(packetByteBuf.readByteArray());
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(in);
				Object readObject = (Object) objectInputStream.readObject();
				objectInputStream.close();
				
				System.out.println(readObject);
				System.out.println(readObject.getClass().getSimpleName());
				
				StringParser parser = (StringParser) readObject;
				return new CustomArgumentType_1_18_R2(parserClass, parser);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

			System.out.println("AAAAAAAAAA");
			return null;
		}

		@Override
		public void serializeToJson(CustomArgumentType_1_18_R2 argument, JsonObject jsonObject) {
		}

		public static class ByteClassLoader extends URLClassLoader {
			private final Map<String, byte[]> extraClassDefs;

			public ByteClassLoader(URL[] urls, ClassLoader parent, Map<String, byte[]> extraClassDefs) {
				super(urls, parent);
				this.extraClassDefs = new HashMap<String, byte[]>(extraClassDefs);
			}

			@Override
			protected Class<?> findClass(final String name) throws ClassNotFoundException {
				byte[] classBytes = this.extraClassDefs.remove(name);
				if (classBytes != null) {
					return defineClass(name, classBytes, 0, classBytes.length);
				}
				return super.findClass(name);
			}
		}

	}

}