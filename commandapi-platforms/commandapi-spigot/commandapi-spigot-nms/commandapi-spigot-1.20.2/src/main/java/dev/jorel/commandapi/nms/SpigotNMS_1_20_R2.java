package dev.jorel.commandapi.nms;

public class SpigotNMS_1_20_R2 extends SpigotNMS_CommonWithFunctions {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_20_R2();
	}
}
