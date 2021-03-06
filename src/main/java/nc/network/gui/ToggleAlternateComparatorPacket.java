package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.ITile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleAlternateComparatorPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	boolean alternateComparator;
	
	public ToggleAlternateComparatorPacket() {
		messageValid = false;
	}
	
	public ToggleAlternateComparatorPacket(ITile machine) {
		pos = machine.getTilePos();
		alternateComparator = machine.getAlternateComparator();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			alternateComparator = buf.readBoolean();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(alternateComparator);
	}
	
	public static class Handler implements IMessageHandler<ToggleAlternateComparatorPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleAlternateComparatorPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleAlternateComparatorPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITile) {
				ITile machine = (ITile) tile;
				machine.setAlternateComparator(message.alternateComparator);
				tile.markDirty();
			}
		}
	}
}
