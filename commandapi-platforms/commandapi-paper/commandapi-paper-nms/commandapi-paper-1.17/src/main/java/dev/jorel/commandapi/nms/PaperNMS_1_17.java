package dev.jorel.commandapi.nms;

public class PaperNMS_1_17 extends PaperNMS_1_17_Common {

	private NMS_1_17 bukkitNMS;

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			bukkitNMS = new NMS_1_17();
		}
		return bukkitNMS;
	}

}
