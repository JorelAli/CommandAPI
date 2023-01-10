package dev.jorel.commandapi.test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_18_R1;
import dev.jorel.commandapi.nms.NMS_1_19_3_R2;
import dev.jorel.commandapi.nms.NMS_1_19_R1;

public class NMSProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		return Stream.of(
			Arguments.of((Supplier<NMS<?>>) () -> new NMS_1_19_R1()),
			Arguments.of((Supplier<NMS<?>>) () -> new NMS_1_18_R1()),
			Arguments.of((Supplier<NMS<?>>) () -> new NMS_1_19_3_R2())
		);
	}
	
}