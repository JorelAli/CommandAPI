package dev.jorel.commandapi.nms;

public class SpigotNMS_1_20_R3 extends SpigotNMS_Common {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_20_R3();
	}
}
