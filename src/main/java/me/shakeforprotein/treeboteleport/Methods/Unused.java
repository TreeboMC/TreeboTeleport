package me.shakeforprotein.treeboteleport.Methods;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Unused {

    public List<Block> getNearbyBlocks(Block block) {
        List<Block> list = new ArrayList<>();

        int X = block.getX();
        int Y = block.getY();
        int Z = block.getZ();
        int i, j, k = 0;

        for (i = 0; i < 10; i++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 10; k++) {
                    list.add(block.getWorld().getBlockAt(X, Y, Z));
                }
            }
        }
        return list;
    }


    //createDefaultFile("", "homes", true);
    //createDefaultFile("", "servers.yml", false);
    //createDefaultFile("", "spawns.yml", false);
    //createDefaultFile("", "hubMenu.yml", false);
    //createDefaultFile("", "warps.yml", false);
    //createDefaultFile("", "lastLocation.yml", false);


    /*
    host = getConfig().getString("host");
    port = getConfig().getInt("port");
    database = getConfig().getString("database");
    username = getConfig().getString("username");
    password = getConfig().getString("password");
    table = getConfig().getString("transferTable");
    createTable(table);
    */

       /* try {
            openConnection();
            Statement statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            int doesNothing = 1;
        }
        */
    /*Set Command Executors*/

        /*
            this.getCommand("hub").setExecutor(new Hub(this));
            this.getCommand("wild").setExecutor(new Wild2(this));
            this.getCommand("gws").setExecutor(new GetWorldSpawn(this));
            this.getCommand("sws").setExecutor(new SetVanillaWorldSpawn(this));
            this.getCommand("tp").setExecutor(new Tp(this));
            this.getCommand("tpahere").setExecutor(new Tp2MePls(this));
            this.getCommand("tpa").setExecutor(new MayITp(this));  // MAY HAVE ISSUES
            this.getCommand("tpask").setExecutor(new MayITp(this)); // MAY HAVE ISSUES
            this.getCommand("tpok").setExecutor(new TpOk(this));  // MAY HAVE ISSUES
            this.getCommand("tpaccept").setExecutor(new TpOk(this));  // MAY HAVE ISSUES
            this.getCommand("tpyes").setExecutor(new TpOk(this));  //MAY HAVE ISSUES
            this.getCommand("givehubitem").setExecutor(new GiveHubItem(this));
            this.getCommand("setwarp").setExecutor(new SetWarp(this));
            this.getCommand("deletewarp").setExecutor(new DeleteWarp(this));  // MAY HAVE ISSUES
            this.getCommand("delwarp").setExecutor(new DeleteWarp(this));  // MAY HAVE ISSUES
            this.getCommand("warp").setExecutor(new WarpTo(this));
            this.getCommand("warp").setTabCompleter(new TabCompleteWarp(this));
            this.getCommand("warps").setExecutor(new WarpTo(this));
            this.getCommand("sethome").setExecutor(new SetHome(this));
            this.getCommand("delhome").setExecutor(new DeleteHome(this));
            this.getCommand("home").setExecutor(new Home(this));
            this.getCommand("homes").setExecutor(new Homes(this));
            this.getCommand("bed").setExecutor(new Bed(this));
            this.getCommand("setworldspawn").setExecutor(new SetWorldSpawn(this));
            this.getCommand("spawn").setExecutor(new Spawn(this));
            this.getCommand("sendspawn").setExecutor(new SendSpawn(this));
            this.getCommand("ttelereload").setExecutor(new Reload(this));
            this.getCommand("configurehub").setExecutor(new ConfigureHubMenu(this));
            this.getCommand("configurewarps").setExecutor(new ConfigureWarps(this));
            this.getCommand("configurehomes").setExecutor(new ConfigureHomes(this));
            this.getCommand("clearmychat").setExecutor(new ClearMyChat(this));
            this.getCommand("ttelesaveconfig").setExecutor(new SaveConfig(this));
            this.getCommand("tteleversion").setExecutor(new Version(this));
            this.getCommand("nameit").setExecutor(new NameIt(this));
            this.getCommand("tptoggle").setExecutor(new TpToggle(this));
            this.getCommand("disabletpsafety").setExecutor(new DisableTpSafety(this));
            this.getCommand("tpno").setExecutor(new TpNo(this));   // MAY HAVE ISSUES
            this.getCommand("tpdeny").setExecutor(new TpNo(this));  // MAY HAVE ISSUES
            this.getCommand("tpcancel").setExecutor(new TpNo(this));  //MAY HAVE ISSUES
            this.getCommand("back").setExecutor(new Back(this));
            this.getCommand("addmaxhomes").setExecutor(new AddMaxHomes(this));
            this.getCommand("showmaxhomes").setExecutor(new ShowMaxHomes(this));
            this.getCommand("shop").setExecutor(new Shop(this));
            this.getCommand("setshop").setExecutor(new SetShop(this));
            this.getCommand("setttelecooldown").setExecutor(new SetTTeleCooldown(this));
            this.getCommand("fixskygridhomes").setExecutor(new FixSkyGridHomes(this));
            this.getCommand("restoreplayerinventory").setExecutor(new RestorePlayerInventory(this));
            this.getCommand("toggledeathdocket").setExecutor(new ToggleDeathDocket(this));
            */
/*
        this.getCommand("mergeessdata").setExecutor(new MergeEssentialsData(this));
        this.getCommand("fixtthomes").setExecutor(new FixTTHomes(this));
        */

    //Obsolete ??
    /*
    public void createDefaultFile(String path, String file, boolean isFolder) {

        if (isFolder) {
            File folderToRegister = new File(path, file);
            if (!folderToRegister.exists()) {
                folderToRegister.mkdir();
            }
        } else {
            File fileToRegister = new File(path, file);
            //System.out.println("Registering file " + path + File.separator + file);
            if (!fileToRegister.exists()) {
                //System.out.println("File does not exist. Creating new file.");
                saveResource(path + file, false);
            }
        }
    }
     */

    /* UNUSED ??
    public YamlConfiguration getYaml(String path, String file) {
        file = File.separator + file;
        File theYml = new File(path, file);

        if (!theYml.exists()) {
            try {
                theYml.createNewFile();
            } catch (IOException ex) {
                roots.errorLogger.logError(this, ex);
            }
        }
        return YamlConfiguration.loadConfiguration(theYml);
    }
     */
}
