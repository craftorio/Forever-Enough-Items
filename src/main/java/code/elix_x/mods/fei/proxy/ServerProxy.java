package code.elix_x.mods.fei.proxy;

import java.io.File;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.mods.fei.ForeverEnoughItemsBase;
import code.elix_x.mods.fei.api.FEIApi;
import code.elix_x.mods.fei.api.permission.IFEIPermissionsManager;
import code.elix_x.mods.fei.api.utils.IFEIUtil;
import code.elix_x.mods.fei.permission.FEIPermissionsManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements IFEIProxy {

	public ServerProxy(){
		new AClass<>(FEIApi.class).getDeclaredField("INSTANCE").setFinal(false).set(null, new FEIApi(){

			@Override
			public void onUtilPropertySelect(int id){

			}

			@Override
			public IFEIPermissionsManager getPermissionsManager(World world){
				return FEIPermissionsManager.get(world);
			}

			@Override
			public File getFEIConfigDir(){
				return ForeverEnoughItemsBase.configDir;
			}

			@Override
			public void addGridUtil(IFEIUtil util){

			}

		});
	}

	@Override
	public void preInit(FMLPreInitializationEvent event){

	}

	@Override
	public void init(FMLInitializationEvent event){

	}

	@Override
	public void postInit(FMLPostInitializationEvent event){

	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event){

	}

}
