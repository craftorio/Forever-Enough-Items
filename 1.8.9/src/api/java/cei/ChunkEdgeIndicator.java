package cei;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;

@Mod(modid="cei", name="ChunkEdgeIndicator", version="0.1", acceptedMinecraftVersions="[1.8.9]")
public class ChunkEdgeIndicator
{
  public static final String MODID = "cei";
  public static final String MODNAME = "ChunkEdgeIndicator";
  public static final String VERSION = "0.1";
  @Mod.Instance(MODID)
  public static ChunkEdgeIndicator instance;
  public static final KeyBinding keyBindChunkOverlay = new KeyBinding("key.chunkEdgeIndicator", 67, "key.categories.misc");
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    MinecraftForge.EVENT_BUS.register(new ChunkEdgeRenderer());
    ClientRegistry.registerKeyBinding(keyBindChunkOverlay);
  }
}