package hawox.treeplanter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


public class Hawox_TreePlanter extends JavaPlugin{
    private final TreePlanterBlockListener blockListener = new TreePlanterBlockListener(this);

    PluginDescriptionFile pdfFile;
	//private iProperty config = new iProperty("plugins/AutoPlant/AutoPlant.config");
	 
	//variables specific to this file
	private String TellUserPlanted = "A new sapling will arrive in one moment";
	private String TellUserProtected = "This sapling is protected from jerks like you!";
	private int delayTime = 1000; //time in milliseconds until a new sapling is planted there
	private int protectTime = 1000; //time in milliseconds until a new sapling can be damaged
	private boolean Telluser = false;
	private boolean TelluserProtected = true;
	private boolean replantOnBurn = true;
	
	//saplings awaiting planting
	private ArrayList<Block> replant = new ArrayList<Block>();
	private ScheduledThreadPoolExecutor treeplant_Timer = new ScheduledThreadPoolExecutor(50);

	//protected saplings awaiting unprotecting
	private ArrayList<Block> protect = new ArrayList<Block>();
	private ScheduledThreadPoolExecutor treeprotect_Timer = new ScheduledThreadPoolExecutor(50);
	
    public void onDisable() {
		//System.out.println("[Hawox's AutoPlanter] disabled!");
    	System.out.println( "[" + pdfFile.getName() + "] version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {
    	pdfFile = getDescription();
    	
    	//load up our files if they don't exist
		moveFiles();

		// setup config
		readConfig();
		
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        if(replantOnBurn)
        	pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Priority.Normal, this);
        
        System.out.println( "[" + pdfFile.getName() + "] version " + pdfFile.getVersion() + " is enabled!" );
    }
    
	public void moveFiles(){
		getDataFolder().mkdir();
		getDataFolder().setWritable(true);
	    getDataFolder().setExecutable(true);
		extractFile("config.yml");
	}
	
	//Taken and modified from iCon
	  private void extractFile(String name) {
		    File actual = new File(getDataFolder(), name);
		    if (!actual.exists()) {
		      InputStream input = getClass().getResourceAsStream("/Default_Files/" + name);
		      if (input != null) {
		        FileOutputStream output = null;
		        try
		        {
		          output = new FileOutputStream(actual);
		          byte[] buf = new byte[8192];
		          int length = 0;

		          while ((length = input.read(buf)) > 0) {
		            output.write(buf, 0, length);
		          }

		          System.out.println("[" + this.pdfFile.getName() + "] Default file written: " + name);
		        } catch (Exception e) {
		          e.printStackTrace();
		        } finally {
		          try {
		            if (input != null)
		              input.close();
		          }
		          catch (Exception e) {
		          }
		          try {
		            if (output != null)
		              output.close();
		          }
		          catch (Exception e)
		          {
		          }
		        }
		      }
		    }
		  }

	public void readConfig() {
		Configuration config = new Configuration(new File(getDataFolder(), "config.yml"));
		config.load();
		
		Telluser = config.getBoolean("Tell_User_Planting", Telluser);
		TelluserProtected = config.getBoolean("Tell_User_Protected", TelluserProtected);

		
		replantOnBurn = config.getBoolean("Replant_Burned_Tree", replantOnBurn);

		TellUserPlanted = config.getString("Being_Planted_Text", TellUserPlanted);
		TellUserProtected = config.getString("Protected_Text", TellUserProtected);

		delayTime = config.getInt("Plant_Delay", delayTime);
		protectTime = config.getInt("Protect_Time", protectTime);
	}
	
    /*public void propFileCheck(){
    	//check to see if the properties exist in the config, if so read them if not set them to the presets above
		if(config.keyExists("TellUserPlanted")){
			//read it
			TellUserPlanted = config.getString("TellUserPlanted");
		}else{
			//create it in the prop file
			config.setString("TellUserPlanted", TellUserPlanted);
		}
		
		if(config.keyExists("delayTime")){
			//read it
			setdelayTime(config.getInt("delayTime"));
		}else{
			//create it in the prop file
			config.setInt("delayTime", getdelayTime());
		}
		
		if(config.keyExists("Telluser")){
			//read it
			Telluser = config.getBoolean("Telluser");
		}else{
			//create it in the prop file
			config.setBoolean("Telluser", Telluser);
		}
    }*/

	public PluginDescriptionFile getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(PluginDescriptionFile pdfFile) {
		this.pdfFile = pdfFile;
	}

	public String getTellUserPlanted() {
		return TellUserPlanted;
	}

	public void setTellUserPlanted(String tellUserPlanted) {
		TellUserPlanted = tellUserPlanted;
	}

	public String getTellUserProtected() {
		return TellUserProtected;
	}

	public void setTellUserProtected(String tellUserProtected) {
		TellUserProtected = tellUserProtected;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getProtectTime() {
		return protectTime;
	}

	public void setProtectTime(int protectTime) {
		this.protectTime = protectTime;
	}

	public boolean isTelluser() {
		return Telluser;
	}

	public void setTelluser(boolean telluser) {
		Telluser = telluser;
	}

	public ArrayList<Block> getReplant() {
		return replant;
	}

	public void setReplant(ArrayList<Block> replant) {
		this.replant = replant;
	}

	public ScheduledThreadPoolExecutor getTreeplant_Timer() {
		return treeplant_Timer;
	}

	public void setTreeplant_Timer(ScheduledThreadPoolExecutor treeplant_Timer) {
		this.treeplant_Timer = treeplant_Timer;
	}

	public ArrayList<Block> getProtect() {
		return protect;
	}

	public void setProtect(ArrayList<Block> protect) {
		this.protect = protect;
	}

	public ScheduledThreadPoolExecutor getTreeprotect_Timer() {
		return treeprotect_Timer;
	}

	public void setTreeprotect_Timer(ScheduledThreadPoolExecutor treeprotect_Timer) {
		this.treeprotect_Timer = treeprotect_Timer;
	}

	public void setdelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getdelayTime() {
		return delayTime;
	}

	public boolean isTelluserProtected() {
		return TelluserProtected;
	}

	public void setTelluserProtected(boolean telluserProtected) {
		TelluserProtected = telluserProtected;
	}

	public boolean isReplantOnBurn() {
		return replantOnBurn;
	}

	public void setReplantOnBurn(boolean replantOnBurn) {
		this.replantOnBurn = replantOnBurn;
	}
}