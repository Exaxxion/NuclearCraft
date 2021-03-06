package nc.item.tool;

import java.util.List;

import javax.annotation.Nullable;

import nc.item.IInfoItem;
import nc.util.InfoHelper;
import nc.util.OreDictHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCHoe extends ItemHoe implements IInfoItem {
	
	private final TextFormatting infoColor;
	private final String[] tooltip;
	public String[] info;

	public NCHoe(ToolMaterial material, TextFormatting infoColor, String... tooltip) {
		super(material);
		this.infoColor = infoColor;
		this.tooltip = tooltip;
	}
	
	@Override
	public void setInfo() {
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		if (info.length > 0) InfoHelper.infoFull(tooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, info);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = toolMaterial.getRepairItemStack();
		return mat != null && !mat.isEmpty() && OreDictHelper.isOreMatching(mat, repair);
	}
}
