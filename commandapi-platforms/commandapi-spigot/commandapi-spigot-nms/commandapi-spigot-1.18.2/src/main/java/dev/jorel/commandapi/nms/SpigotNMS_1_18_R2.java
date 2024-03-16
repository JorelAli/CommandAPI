package dev.jorel.commandapi.nms;

public class SpigotNMS_1_18_R2 extends SpigotNMS_Common {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_18_R2();
	}
}
