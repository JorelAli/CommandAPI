import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_Common;
import net.minecraft.commands.CommandListenerWrapper;

public abstract class BlankNMS extends NMS_Common<CommandListenerWrapper> {

	public final NMS<?> BASE_NMS;
	
	public BlankNMS(NMS<?> baseNMS) {
		this.BASE_NMS = baseNMS;
	}

}
