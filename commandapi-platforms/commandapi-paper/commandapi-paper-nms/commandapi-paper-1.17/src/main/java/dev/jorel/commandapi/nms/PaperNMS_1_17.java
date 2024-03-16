package dev.jorel.commandapi.nms;

public class PaperNMS_1_17 extends PaperNMS_1_17_Common {

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_17();
	}
}
