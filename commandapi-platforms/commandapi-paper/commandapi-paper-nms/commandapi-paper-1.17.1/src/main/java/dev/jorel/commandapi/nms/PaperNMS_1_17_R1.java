package dev.jorel.commandapi.nms;

public class PaperNMS_1_17_R1 extends PaperNMS_1_17_Common {

	private NMS_1_17_R1 bukkitNMS;

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			this.bukkitNMS = new NMS_1_17_R1();
		}
		return bukkitNMS;
	}

}
