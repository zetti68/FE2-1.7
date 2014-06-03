package forestryextras.main;

import java.util.ArrayList;

import net.minecraftforge.common.MinecraftForge;
import wasliecore.helpers.FileHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import forestryextras.handlers.GUIHandler;
import forestryextras.handlers.events.OnPlayerJoinWorld;
import forestryextras.helpers.DonatorHelper;
import forestryextras.main.init.FEBees;
import forestryextras.main.init.FEBlocks;
import forestryextras.main.init.FEItems;
import forestryextras.main.init.Recipes;
import forestryextras.main.init.intergration.IntergrationLoader;

@Mod(modid = "ForestryExtras", name = "ForestryExtras", version = "1.32" ,dependencies = "required-after:Forestry;required-after:WaslieCore;after:Thaumcraft;after:ExtraTiC;after:EnderIO")
public class Main {
    @SidedProxy(clientSide = "forestryextras.client.ClientProxy", serverSide = "forestryextras.main.CommonProxy")
    public static CommonProxy proxy;
 
    @Instance("ForestryExtras")
    public static Main instance;
    public static double version = 1.330;
    public static String modName = "ForestryExtras";
    public static String alias = "FE";
    public static IntergrationLoader integration = new IntergrationLoader();
   
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception{
		@SuppressWarnings("unused")
		Config config = new Config();
		Config.loadConfig(event);
		
		if(DonatorHelper.canConnect()){
			createFiles();}
		
		proxy.load();
		integration.prePreInit();
    	FEItems.init();
    	FEBlocks.init();
    	integration.preInit();
    	initTiles();
    }
    
    public void createFiles() throws Exception{
    	FileHelper.createModFolder("Forestry Extras 2");    	
    	if(DonatorHelper.getNames() != null){
        	ArrayList<String> list = new ArrayList<String>();
    		for(String s : DonatorHelper.getNames()){
    			list.add(s);
    		}
    		DonatorHelper.createFailFile(list);
    	}
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	/** Use FMPIntegration for FMP Support! */
    	integration.init();
    	FEBees.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
    	initEvents(event);
    	Recipes.init();
    }
    
    public void initEvents(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this); 
        MinecraftForge.EVENT_BUS.register(new OnPlayerJoinWorld());
    }
    
    public void initTiles(){
        GameRegistry.registerTileEntity(forestryextras.blocks.tiles.TileEntityProducer.class, "10001");
    }
    
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt){
    	integration.postInit();
    }
}