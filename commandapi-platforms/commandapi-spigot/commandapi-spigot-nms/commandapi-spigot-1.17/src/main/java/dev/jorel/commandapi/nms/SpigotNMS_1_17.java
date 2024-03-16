package dev.jorel.commandapi.nms;

public class SpigotNMS_1_17 extends SpigotNMS_1_17_Common {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_17();
	}
}
