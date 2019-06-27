package me.shakeforprotein.treeboteleport.UpdateChecker;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UpdateChecker {

    private TreeboTeleport pl;

    public UpdateChecker(TreeboTeleport main) {
        this.pl = main;
    }

    public String requiredPermission = "tbteleport.admin.updatechecker";
    public HashMap runningVersion = new HashMap<Integer, Integer>();
    public HashMap currentVersion = new HashMap<Integer, Integer>();
    public HashMap updateNotified = new HashMap<Player, Boolean>();
    public boolean isOutOfDate = false;
    public String newVersion = "";

    public Boolean getCheckDownloadURL() {
        // Code courtesy of Spigot user Ftbastler
        // Adjustments by ShakeforProtein
        if (pl.getConfig().getString("checkUpdates").equalsIgnoreCase("true")) {
            try {
                System.out.println(pl.badge + "Checking for updates");
                URL url = new URL(pl.getConfig().getString("apiLink"));
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/1.0");
                conn.setConnectTimeout(5000);
                conn.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine = in.readLine();
                String gitVersion = "";

                Boolean getLinkNow = false;
                System.out.println(pl.badge + "Checking for Git Data");
                while (inputLine != null) {
                    System.out.println(pl.badge + "Git Data Found");
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(inputLine);
                    gitVersion = json.get("tag_name").toString();
                    String fullGitVersion = gitVersion;
                    System.out.println(pl.badge + "Found Git Version - " + fullGitVersion);
                    gitVersion = gitVersion.split(" ")[0].split("-")[0];
                    currentVersion.put(0, Integer.parseInt(gitVersion.split("\\.")[0]));
                    currentVersion.put(1, Integer.parseInt(gitVersion.split("\\.")[1]));
                    currentVersion.put(2, Integer.parseInt(gitVersion.split("\\.")[2]));
                    String thisVersion = pl.getDescription().getVersion();
                    thisVersion = thisVersion.split(" ")[0].split("-")[0];
                    runningVersion.put(0, Integer.parseInt(thisVersion.split("\\.")[0]));
                    runningVersion.put(1, Integer.parseInt(thisVersion.split("\\.")[1]));
                    runningVersion.put(2, Integer.parseInt(thisVersion.split("\\.")[2]));
                    int cv0 = (int) currentVersion.get(0);
                    int cv1 = (int) currentVersion.get(1);
                    int cv2 = (int) currentVersion.get(2);
                    int rv0 = (int) runningVersion.get(0);
                    int rv1 = (int) runningVersion.get(1);
                    int rv2 = (int) runningVersion.get(2);
                    if (cv0 > rv0) {
                        isOutOfDate = true;
                    } else if (cv0 == rv0 && cv1 > rv1) {
                        isOutOfDate = true;
                    } else if (cv0 == rv0 && cv1 == rv1 && cv2 > rv2) {
                        isOutOfDate = true;
                    }

                    if (isOutOfDate) {
                        newVersion = pl.badge + ChatColor.RED + "is out of date. Please update to version " + fullGitVersion + " from " + pl.getConfig().getString("releasePage");
                        System.out.println(newVersion);}
                    else{
                        System.out.println(pl.badge +  "is up to date");
                    }
                    break;
                }
                in.close();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                pl.getServer().getLogger().warning("Something went wrong while downloading an update.");
                pl.getServer().getLogger().info("Please check the plugin's release page to see if there are any updates available.");
                pl.getServer().getLogger().info("" + pl.getConfig().getString("releasePage"));
                pl.getServer().getLogger().fine(e.getMessage());
                return false;
            }
        }
        return false;
    }

    public void checkUpdates(Player p) {
        if (isOutOfDate) {
            newVersion = pl.badge + ChatColor.RED + "is out of date. Please update to version at " + pl.getConfig().getString("releasePage");
            System.out.println(newVersion);}
        else{
            System.out.println(pl.badge +  "is up to date");
        }
    }


    /*
    if(e.getPlayer().hasPermission(uc.requiredPermission)){
        if (!uc.updateNotified.containsKey(e.getPlayer())) {
            uc.checkUpdates(e.getPlayer());
            uc.updateNotfied.putIfAbsent(e.getPlayer(), true);
        }
    }
    */

}
