package com.pwn9.PwnPlantGrowth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PwnPlantGrowth extends JavaPlugin 
{
	
	// declare some stuffs to be used later
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	public static File dataFolder;
	public static Boolean logEnabled;
	public static Boolean blockWaterBucket;
	public static Boolean blockWaterDispenser;
	public static int naturalLight;
	public static List<String> darkGrow;
	public static List<String> enabledWorlds;
	static Random randomNumberGenerator = new Random();
	
	// Week killer
	public static int radius;
	public static String weedKiller;	
	
	public void onEnable() 
	{
		this.saveDefaultConfig();
		
		// Start Metrics
		try 
		{
		    MetricsLite metricslite = new MetricsLite(this);
		    metricslite.start();
		} 
		catch (IOException e) 
		{
		    // Failed to submit the stats :-(
		}
				
		// Setup listeners
		new PlantListener(this);
		new WaterListener(this);
		
		// Get data folder
		PwnPlantGrowth.dataFolder = getDataFolder();
		
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = getConfig().getStringList("enabled_worlds");
		
		// Get water source setting
		PwnPlantGrowth.blockWaterBucket = getConfig().getBoolean("block_water_bucket");
		
		// Get water source setting
		PwnPlantGrowth.blockWaterDispenser = getConfig().getBoolean("block_water_dispenser");
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = getConfig().getBoolean("debug_log");
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = getConfig().getInt("min_natural_light");
		
		// Dark growth
		PwnPlantGrowth.darkGrow = getConfig().getStringList("grow_in_dark");

		// Week Killer
		PwnPlantGrowth.radius = getConfig().getInt("weed_killer_radius");
		PwnPlantGrowth.weedKiller = getConfig().getString("weed_killer");
		
	}
		
	public void onDisable() 
	{
		
	}
	
	static boolean random(int percentChance) 
	{
			return randomNumberGenerator.nextInt(100) < percentChance;
	}
	
	public static boolean isEnabledIn(String world) 
	{
		return enabledWorlds.contains(world);
	}	

	public static boolean canDarkGrow(String plant) 
	{
		return darkGrow.contains(plant);
	}	
	
	public static boolean weedKiller(Block block) 
	{
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        
	    for (x = -5; x <= 5; x++) {
	        for (y = -5; y <= 5; y++) {
	           for (z = -5; z <= 5; z++) {
	        	   if (block.getRelative(x,y,z).getType().toString() == weedKiller) {
	        		   return true;
	        	   }
	           }
	        }
	    } 
		return false;
	}
	
	public static boolean weedKiller(StructureGrowEvent e) {
		
		Block block = e.getLocation().getBlock();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        
	    for (x = -5; x <= 5; x++) {
	        for (y = -5; y <= 5; y++) {
	           for (z = -5; z <= 5; z++) {
	        	   if (block.getRelative(x,y,z).getType().toString() == weedKiller) {
	        		   return true;
	        	   }
	           }
	        }
	    } 
		
		return false;
	}
	
    public static void logToFile(String message) 
    {   
	    	try 
	    	{
			    
			    if(!dataFolder.exists()) 
			    {
			    	dataFolder.mkdir();
			    }
			     
			    File saveTo = new File(dataFolder, "pwnplantgrowth.log");
			    if (!saveTo.exists())  
			    {
			    	saveTo.createNewFile();
			    }
			    
			    FileWriter fw = new FileWriter(saveTo, true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(getDate() +" "+ message);
			    pw.flush();
			    pw.close();
		    } 
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
		    }
    }
    
    public static String getDate() 
    {
    	  String s;
    	  Format formatter;
    	  Date date = new Date(); 
    	  formatter = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
    	  s = formatter.format(date);
    	  return s;
    }

}