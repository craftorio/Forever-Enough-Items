package code.elix_x.mods.fei.utils;

import code.elix_x.mods.fei.ForeverEnoughItemsBase;
import code.elix_x.mods.fei.api.permission.FEIPermissionLevel;
import code.elix_x.mods.fei.api.utils.PermissionRequiredSyncedForFEIUtil;
import code.elix_x.mods.fei.config.FEIConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class WeatherFEIUtil extends PermissionRequiredSyncedForFEIUtil<Integer> {

	public static final String[] descs = {"clear", "rain", "thunderstorm"};
	public static final ResourceLocation[] icons = {new ResourceLocation(ForeverEnoughItemsBase.MODID, FEIConfiguration.icons + "clear.png"), new ResourceLocation(ForeverEnoughItemsBase.MODID, FEIConfiguration.icons + "rain.png"), new ResourceLocation(ForeverEnoughItemsBase.MODID, FEIConfiguration.icons + "thunderstorm.png")};

	private FEIPermissionLevel permissionLevel;

	public WeatherFEIUtil(){
		super("Weather", 0, 1, 2);
	}

	public void setPermissionLevel(FEIPermissionLevel permissionLevel){
		this.permissionLevel = permissionLevel;
	}

	@Override
	public Integer getCurrent(){
		return Minecraft.getMinecraft().theWorld.isThundering() ? 2 : Minecraft.getMinecraft().theWorld.isRaining() ? 1 : 0;
	}

	@Override
	public String getDesc(Integer i){
		return StatCollector.translateToLocal("fei.gui.override.grid.utils.weather." + descs[i]);
	}

	@Override
	public FEIPermissionLevel getPermissionLevel(Integer i){
		return permissionLevel;
	}

	@Override
	public ResourceLocation getTexture(Integer i){
		return icons[i];
	}

	@Override
	public String getText(Integer i){
		return null;
	}

	@Override
	public void onSelect(Integer i, EntityPlayer player, boolean permission){
		if(permission){
			player.worldObj.getWorldInfo().setRaining(i > 0);
			player.worldObj.getWorldInfo().setThundering(i > 1);
		}
	}

}
