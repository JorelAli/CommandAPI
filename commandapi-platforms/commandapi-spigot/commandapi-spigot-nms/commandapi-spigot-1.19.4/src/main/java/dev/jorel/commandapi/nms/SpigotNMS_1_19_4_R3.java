package dev.jorel.commandapi.nms;

public class SpigotNMS_1_19_4_R3 extends SpigotNMS_CommonWithFunctions {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_19_4_R3();
	}
}
