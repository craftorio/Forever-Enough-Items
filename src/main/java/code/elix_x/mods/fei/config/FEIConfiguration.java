package code.elix_x.mods.fei.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import code.elix_x.mods.fei.ForeverEnoughItemsBase;
import code.elix_x.mods.fei.api.client.IRenderable.ItemStackRenderable;
import code.elix_x.mods.fei.api.permission.FEIPermissionLevel;
import code.elix_x.mods.fei.net.FEIGuiType;
import code.elix_x.mods.fei.permission.FEIPermissionsManager;
import code.elix_x.mods.fei.utils.BinFEIUtil;
import code.elix_x.mods.fei.utils.EnchantFEIUtil;
import code.elix_x.mods.fei.utils.FEIInternalGuiDisplayUtil;
import code.elix_x.mods.fei.utils.GameModeFEIUtil;
import code.elix_x.mods.fei.utils.HealFEIUtil;
import code.elix_x.mods.fei.utils.IFEIUtilInternal;
import code.elix_x.mods.fei.utils.MagnetFEIUtil;
import code.elix_x.mods.fei.utils.SaturateFEIUtil;
import code.elix_x.mods.fei.utils.TimeFEIUtil;
import code.elix_x.mods.fei.utils.WeatherFEIUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FEIConfiguration {

	public static final Logger logger = LogManager.getLogger("FEI Configuration");

	public static final String icons = "textures/icons/";

	private static FEIPermissionLevel givePermissonLevel;
	private static FEIPermissionLevel deletePermissonLevel;
	private static FEIPermissionLevel loadInventoryPermissionLevel;

	public static boolean developerMode;

	public static boolean loadJeiFromProfileConfig;
	public static boolean loadJeiFromProfileWorld;
	public static boolean loadJeiFromProfileBlacklist;
	public static boolean loadJeiFromProfileColors;

	public static ImmutableList<IFEIUtilInternal> utils;

	public static BinFEIUtil bin;
	public static GameModeFEIUtil gameMode;
	public static TimeFEIUtil time;
	public static WeatherFEIUtil weather;
	public static MagnetFEIUtil magnet;
	public static HealFEIUtil heal;
	public static SaturateFEIUtil saturate;

	public static FEIInternalGuiDisplayUtil repair;
	public static EnchantFEIUtil enchant;
	public static FEIInternalGuiDisplayUtil effect;

	public static double magnetRadius;

	public static void preInit(FMLPreInitializationEvent event){
		ImmutableList.Builder utils = ImmutableList.builder();
		utils.add(bin = new BinFEIUtil());
		utils.add(gameMode = new GameModeFEIUtil());
		utils.add(time = new TimeFEIUtil());
		utils.add(weather = new WeatherFEIUtil());
		utils.add(magnet = new MagnetFEIUtil());
		utils.add(heal = new HealFEIUtil());
		utils.add(saturate = new SaturateFEIUtil());

		utils.add(repair = new FEIInternalGuiDisplayUtil("Repair", "fei.gui.override.grid.utils.repair", new ItemStackRenderable(new ItemStack(Blocks.ANVIL)), FEIGuiType.REPAIR));
		utils.add(enchant = new EnchantFEIUtil());
		utils.add(effect = new FEIInternalGuiDisplayUtil("Effect", "fei.gui.override.grid.utils.effect", new ItemStackRenderable(new ItemStack(Items.DRAGON_BREATH)), FEIGuiType.EFFECT));

		FEIConfiguration.utils = utils.build();

		File configFile = new File(ForeverEnoughItemsBase.configDir, "static.cfg");
		try{
			configFile.createNewFile();
		} catch(IOException e){
			logger.error("Caught exception while creating config file: ", e);
		}

		Configuration config = new Configuration(configFile);
		config.load();

		load(config);

		config.save();
	}

	public static void load(Configuration config){
		developerMode = config.getBoolean("Developer Mode", "Dev", false, "Only activate it in deobf environment, though not compulsory.\nIn any case, do not activate it if you don't understand statement above.");

		ConfigCategory permissions = config.getCategory("Permission Levels");
		permissions.setComment("Permission Levels: USER < MODERATOR < ADMINISTRATOR < OWNER");

		givePermissonLevel = getPermissionLevel(permissions, "Give Items Permission Level", FEIPermissionLevel.MODERATOR);
		deletePermissonLevel = getPermissionLevel(permissions, "Delete Items Permission Level", FEIPermissionLevel.MODERATOR);
		loadInventoryPermissionLevel = getPermissionLevel(permissions, "Load Inventory Permission Level", FEIPermissionLevel.MODERATOR);

		ConfigCategory utilsPermissions = config.getCategory("Permission Levels.Utilities");

		for(IFEIUtilInternal util : utils)
			setPermissionLevel(utilsPermissions, util, FEIPermissionLevel.MODERATOR);

		magnetRadius = config.getFloat("Magnet Radius", "Utilities", 16, 0, 100, "Radius of item magnet.");

		loadJeiFromProfileConfig = config.getBoolean("Load From Profile - jei.cfg", "JEI Override", true, "Load JEI configuration from jei.cfg per profile (true) or per game (false)?");
		loadJeiFromProfileWorld = config.getBoolean("Load From Profile - worldSettings.cfg", "JEI Override", true, "Load JEI configuration from worldSettings.cfg per profile (true) or per game (false)?");
		loadJeiFromProfileBlacklist = config.getBoolean("Load From Profile - itemBlacklist.cfg", "JEI Override", true, "Load JEI configuration from itemBlacklist.cfg per profile (true) or per game (false)?");
		loadJeiFromProfileColors = config.getBoolean("Load From Profile - searchColors.cfg", "JEI Override", true, "Load JEI configuration from searchColors.cfg per profile (true) or per game (false)?");
	}

	private static void setPermissionLevel(ConfigCategory cat, IFEIUtilInternal util, FEIPermissionLevel defaultl){
		util.setPermissionLevel(getPermissionLevel(cat, util.getName(), defaultl));
	}

	private static FEIPermissionLevel getPermissionLevel(ConfigCategory cat, String name, FEIPermissionLevel defaultl){
		Property p = cat.get(name);
		if(p == null){
			p = new Property(name, defaultl.name(), Type.STRING, FEIPermissionLevel.names());
			cat.put(name, p);
		}
		p.setValidValues(FEIPermissionLevel.names());
		p.setDefaultValue(defaultl.name());
		p.setComment("Permission level required to use " + name);
		String s = p.getString();
		if(!ArrayUtils.contains(p.getValidValues(), s)) p.set(s = defaultl.name());
		return FEIPermissionLevel.valueOf(s);
	}

	public static boolean canGive(EntityPlayer player){
		return FEIPermissionsManager.getPermissionLevels(player).isHigherOrEqual(givePermissonLevel);
	}

	public static boolean canDelete(EntityPlayer player){
		return FEIPermissionsManager.getPermissionLevels(player).isHigherOrEqual(deletePermissonLevel);
	}

	public static boolean canLoadInventory(EntityPlayer player){
		return FEIPermissionsManager.getPermissionLevels(player).isHigherOrEqual(loadInventoryPermissionLevel);
	}

}
