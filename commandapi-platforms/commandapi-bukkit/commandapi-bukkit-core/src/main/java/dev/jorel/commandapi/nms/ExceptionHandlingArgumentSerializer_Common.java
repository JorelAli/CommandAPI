package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPIPlatform;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * An abstract class that handles common logic for serializing an {@link ExceptionHandlingArgumentType} in Minecraft
 * versions 1.15 to 1.18. For context, see <a href="https://wiki.vg/index.php?title=Command_Data&oldid=17429#Node_Format">
 * Command Data: Node Format (pre 1.19)</a>.
 *
 * @param <T> The same type parameter T for {@link ExceptionHandlingArgumentType}. This fixes a silly generics issue.
 * @param <EI> The same type parameter as ExceptionInformation in {@link ExceptionHandlingArgumentType}. This fixes a silly generics issue.
 * @param <WRITER> The netty {@link ByteBuf} subclass used to write packets in the version of Minecraft this is being used for.
 */
public abstract class ExceptionHandlingArgumentSerializer_Common<T, EI, WRITER extends ByteBuf> {
    /**
     * This method is expected to write the properties section for an argument node that uses the
     * {@link ExceptionHandlingArgumentType}, so that the argument can be reconstructed when received by a client.
     * However, we can't add {@link ExceptionHandlingArgumentType} to the client, so if it actually received a node
     * with that type it would freak out and disconnect. Instead, this method removes its own identifier and writes
     * the identifier and properties for the {@link ExceptionHandlingArgumentType#baseType()} instead.
     *
     * @param argument The {@link ExceptionHandlingArgumentType} to serialize
     * @param packetWriter The netty {@link ByteBuf} that contains the data
     */
    protected void commonSerializeToNetwork(ExceptionHandlingArgumentType<T, EI> argument, WRITER packetWriter) {
        // REMOVE MY KEY FROM THE PACKET
        Object myInfo = getArgumentTypeInformation(argument);

        byte[] myKeyBytes = getSerializationKey(myInfo).getBytes(StandardCharsets.UTF_8);

        // Removing bytes for the key, assuming the length always takes up 1 byte
        packetWriter.writerIndex(packetWriter.writerIndex() - myKeyBytes.length - 1);

        // ADD BASE TYPE KEY INSTEAD
        ArgumentType<T> baseType = argument.baseType();
        Object baseInfo = getArgumentTypeInformation(baseType);
        byte[] baseKeyBytes = getSerializationKey(baseInfo).getBytes(StandardCharsets.UTF_8);

        // Assume length always takes up 1 byte
        packetWriter.writeByte(baseKeyBytes.length);
        packetWriter.writeBytes(baseKeyBytes);

        // SERIALIZE BASE TYPE
        serializeBaseTypeToNetwork(baseType, baseInfo, packetWriter);
    }

    /**
     * Adds the properties of an {@link ExceptionHandlingArgumentType} node to a {@link JsonObject}. This is used when
     * generating a json representation for a Brigadier {@link CommandDispatcher}, like when
     * {@link CommandAPIPlatform#createDispatcherFile(File, CommandDispatcher)} is called.
     *
     * @param argument The {@link ExceptionHandlingArgumentType} to serialize
     * @param properties The {@link JsonObject} to put the properties in
     */
    protected void commonSerializeToJson(ExceptionHandlingArgumentType<T, EI> argument, JsonObject properties) {
        ArgumentType<T> baseType = argument.baseType();

        Object baseInfo = getArgumentTypeInformation(baseType);

        properties.addProperty("baseType", getSerializationKey(baseInfo));

        JsonObject baseProperties = new JsonObject();
        serializeBaseTypeToJson(baseType, baseInfo, baseProperties);
        if (baseProperties.size() > 0) {
            properties.add("baseProperties", baseProperties);
        }
    }

    /**
     * Retrieves the serialization information object for the given {@link ArgumentType}. The class of the returned
     * object is net.minecraft.commands.synchronization.ArgumentTypes.Entry (1.17 and 1.18)
     * or net.minecraft.server.[version].ArgumentRegistry.a (1.15 and 1.16)
     *
     * @param argumentType The {@link ArgumentType}
     * @return The serialization information object
     */
    protected abstract Object getArgumentTypeInformation(ArgumentType<?> argumentType);

    /**
     * Retrieves the serialization key from the given serialization information object. This key is used to identify
     * the parser responsible for deserializing a node in the Commands Packet's data.
     *
     * @param info The serialization information object
     * @return The serialization key
     */
    protected abstract String getSerializationKey(Object info);

    /**
     * Serializes the properties for the base type of {@link ExceptionHandlingArgumentType} into a packet.
     *
     * @param baseType The base {@link ArgumentType} that needs its properties serialized
     * @param baseInfo The serialization information object for the given {@link ArgumentType}
     * @param packetWriter The netty {@link ByteBuf} that contains the packet data.
     */
    protected abstract void serializeBaseTypeToNetwork(ArgumentType<T> baseType, Object baseInfo, WRITER packetWriter);

    /**
     * Serializes the properties for the base type of {@link ExceptionHandlingArgumentType} into json format.
     *
     * @param baseType The base {@link ArgumentType} that needs its properties serialized
     * @param baseInfo The serialization information object for the given {@link ArgumentType}
     * @param properties The {@link JsonObject} to put the properties in
     */
    protected abstract void serializeBaseTypeToJson(ArgumentType<T> baseType, Object baseInfo, JsonObject properties);
}