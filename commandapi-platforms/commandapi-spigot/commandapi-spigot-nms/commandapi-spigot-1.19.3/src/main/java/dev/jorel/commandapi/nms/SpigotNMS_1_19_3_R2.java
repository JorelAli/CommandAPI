package dev.jorel.commandapi.nms;

public class SpigotNMS_1_19_3_R2 extends SpigotNMS_CommonWithFunctions {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_19_3_R2();
	}
}
