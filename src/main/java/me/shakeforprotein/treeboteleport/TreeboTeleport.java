package me.shakeforprotein.treeboteleport;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import me.shakeforprotein.treeboteleport.Commands.*;
import me.shakeforprotein.treeboteleport.Commands.NameIt;
import me.shakeforprotein.treeboteleport.Listeners.*;
import me.shakeforprotein.treeboteleport.Methods.Teleports.ToWorld;
import me.shakeforprotein.treeboteleport.UpdateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class TreeboTeleport extends JavaPlugin {

    private HubMenuInventoryListener hubMenuInventoryListener = new HubMenuInventoryListener(this);
    private HubItemListener hubItemListener = new HubItemListener(this);
    private ToWorld toWorld = new ToWorld(this);
    public BungeeChannelApi bungeeApi = new BungeeChannelApi(this);
    public HashMap openInvHash = new HashMap<String, Inventory>();
    public HashMap lockMove = new HashMap<UUID, String>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        getConfig().set("version", this.getDescription().getVersion());
        saveConfig();

        host = getConfig().getString("host");
        port = getConfig().getInt("port");
        database = getConfig().getString("database");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
        table = getConfig().getString("transferTable");

        //createTable(table);


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
        this.getCommand("hub").setExecutor(new Hub(this));
        this.getCommand("wild").setExecutor(new Wild(this));
        this.getCommand("gws").setExecutor(new GetWorldSpawn(this));
        this.getCommand("sws").setExecutor(new SetVanillaWorldSpawn(this));
        this.getCommand("tp").setExecutor(new Tp(this));
        this.getCommand("tpahere").setExecutor(new Tp2MePls(this));
        this.getCommand("tpa").setExecutor(new MayITp(this));
        this.getCommand("tpask").setExecutor(new MayITp(this));
        this.getCommand("tpok").setExecutor(new TpOk(this));
        this.getCommand("tpaccept").setExecutor(new TpOk(this));
        this.getCommand("tpyes").setExecutor(new TpOk(this));
        this.getCommand("givehubitem").setExecutor(new GiveHubItem(this));
        this.getCommand("setwarp").setExecutor(new SetWarp(this));
        this.getCommand("deletewarp").setExecutor(new DeleteWarp(this));
        this.getCommand("delwarp").setExecutor(new DeleteWarp(this));
        this.getCommand("warp").setExecutor(new WarpTo(this));
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
        this.getCommand("tpno").setExecutor(new TpNo(this));
        this.getCommand("tpdeny").setExecutor(new TpNo(this));
        this.getCommand("tpcancel").setExecutor(new TpNo(this));
        this.getCommand("back").setExecutor(new Back(this));

/*
/*
        this.getCommand("mergeessdata").setExecutor(new MergeEssentialsData(this));
        this.getCommand("fixtthomes").setExecutor(new FixTTHomes(this));
        */

        getServer().getPluginManager().registerEvents(new HubItemListener(this), this);
        getServer().getPluginManager().registerEvents(new HubMenuInventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new HomeMenuInventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new WarpsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new BedClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);


        File serverFile = new File(getDataFolder(), File.separator + "servers.yml");
        FileConfiguration serverList = YamlConfiguration.loadConfiguration(serverFile);


        createDefaultFile("", "homes", true);
        createDefaultFile("", "servers.yml", false);
        createDefaultFile("", "spawns.yml", false);
        createDefaultFile("", "hubMenu.yml", false);
        createDefaultFile("", "warps.yml", false);
        createDefaultFile("", "lastLocation.yml", false);


        if (!serverFile.exists()) {
            try {
                serverFile.createNewFile();
                try {
                    serverList.options().copyDefaults();
                    serverList.save(serverFile);
                } catch (FileNotFoundException e) {
                    makeLog(e);
                }
            } catch (IOException e) {
                makeLog(e);
            }
        }

        for (String item : serverList.getConfigurationSection("servers").getKeys(false)) {
            BukkitCommand item2 = new BukkitCommand(item.toLowerCase()) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] strings) {
                    Player p = (Player) commandSender;
                    String serverTo = serverList.getString("servers." + item + ".server");
                    String worldTo = serverList.getString("servers." + item + ".world");
                    toWorld.toWorld(serverTo, worldTo, p);
                    return false;
                }
            };
            registerNewCommand(this.getDescription().getName(), item2);
            //this.getCommand(item.toLowerCase()).setPermission("tbteleport.servers." + item);
        }

        UpdateChecker uc = new UpdateChecker(this);
        uc.getCheckDownloadURL();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic

    /*
    try{

        connection.close();
    }
    catch(SQLException e){
        int doesNothing = 1;
        }
    */
    }

    public String badge = ChatColor.translateAlternateColorCodes('&', getConfig().getString("general.messages.badge") + " ");
    public String err = badge + ChatColor.translateAlternateColorCodes('&', getConfig().getString("general.messages.error") + " ");

    public Connection connection;
    private String host, database, username, password;
    private int port;
    public String table = getConfig().getString("transferTable");


    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }


    public boolean createTable(String table) {
        System.out.println("Checking for InterServer Teleport Table");
        String teleportTableCreationQuery = "CREATE TABLE IF NOT EXISTS `" + table + "` (`ID` int(11) NOT NULL AUTO_INCREMENT,`UUID` text COLLATE utf8_bin NOT NULL,`IGNAME` text COLLATE utf8_bin NOT NULL,`PLAYTIME` bigint(20) NOT NULL DEFAULT '0',`TOTALKILLS` int(11) NOT NULL DEFAULT '0',`TOTALDEATHS` int(11) NOT NULL DEFAULT '0',`TOTALPLAYERDEATHS` int(11) NOT NULL DEFAULT '0',`TOTALMOBDEATHS` int(11) NOT NULL DEFAULT '0',`TOTALMOBKILLS` int(11) NOT NULL DEFAULT '0', `PASSIVEMOBKILLS` int(11) NOT NULL DEFAULT '0',`HOSTILEMOBKILLS` int(11) NOT NULL DEFAULT '0',`TOTALPLAYERKILLS` int(11) NOT NULL DEFAULT '0',`BATKILLS` int(11) NOT NULL DEFAULT '0',`CHICKENKILLS` int(11) NOT NULL DEFAULT '0',`CODKILLS` int(11) NOT NULL DEFAULT '0',`COWKILLS` int(11) NOT NULL DEFAULT '0',`DONKEYKILLS` int(11) NOT NULL DEFAULT '0',`HORSEKILLS` int(11) NOT NULL DEFAULT '0',`MUSHROOMCOWKILLS` int(11) NOT NULL DEFAULT '0',`MULEKILLS` int(11) NOT NULL DEFAULT '0',`OCELOTKILLS` int(11) NOT NULL DEFAULT '0',`PARROTKILLS` int(11) NOT NULL DEFAULT '0',`PIGKILLS` int(11) NOT NULL DEFAULT '0',`RABBITKILLS` int(11) NOT NULL DEFAULT '0',`SHEEPKILLS` int(11) NOT NULL DEFAULT '0',`SKELETONHORSEKILLS` int(11) NOT NULL DEFAULT '0',`SALMONKILLS` int(11) NOT NULL DEFAULT '0',`SQUIDKILLS` int(11) NOT NULL DEFAULT '0',`TURTLEKILLS` int(11) NOT NULL DEFAULT '0',`TROPICALFISHKILLS` int(11) NOT NULL DEFAULT '0',`VILLAGERKILLS` int(11) NOT NULL DEFAULT '0',`PUFFERFISHKILLS` int(11) NOT NULL DEFAULT '0',`DOLPHINKILLS` int(11) NOT NULL DEFAULT '0',`LLAMAKILLS` int(11) NOT NULL DEFAULT '0',`POLARBEARKILLS` int(11) NOT NULL DEFAULT '0',`WOLFKILLS` int(11) NOT NULL DEFAULT '0',`CAVESPIDERKILLS` int(11) NOT NULL DEFAULT '0',`ENDERMANKILLS` int(11) NOT NULL DEFAULT '0',`SPIDERKILLS` int(11) NOT NULL DEFAULT '0',`ZOMBIEPIGMANKILLS` int(11) NOT NULL DEFAULT '0',`CREEPERKILLS` int(11) NOT NULL DEFAULT '0',`ELDERGUARDIANKILLS` int(11) NOT NULL DEFAULT '0',`GUARDIANKILLS` int(11) NOT NULL DEFAULT '0',`PHANTOMKILLS` int(11) NOT NULL DEFAULT '0',`SILVERFISHKILLS` int(11) NOT NULL DEFAULT '0',`SLIMEKILLS` int(11) NOT NULL DEFAULT '0',`DROWNEDKILLS` int(11) NOT NULL DEFAULT '0',`HUSKKILLS` int(11) NOT NULL DEFAULT '0',`ZOMBIEKILLS` int(11) NOT NULL DEFAULT '0',`ZOMBIEVILLAGERKILLS` int(11) NOT NULL DEFAULT '0',`SKELETONKILLS` int(11) NOT NULL DEFAULT '0',`STRAYKILLS` int(11) NOT NULL DEFAULT '0',`WITHERSKELETONKILLS` int(11) NOT NULL DEFAULT '0',`BLAZEKILLS` int(11) NOT NULL DEFAULT '0',`GHASTKILLS` int(11) NOT NULL DEFAULT '0',`MAGMACUBEKILLS` int(11) NOT NULL DEFAULT '0',`ENDERMITEKILLS` int(11) NOT NULL DEFAULT '0',`SHULKERKILLS` int(11) NOT NULL DEFAULT '0',`EVOKERKILLS` int(11) NOT NULL DEFAULT '0',`VINDICATORKILLS` int(11) NOT NULL DEFAULT '0',`VEXKILLS` int(11) NOT NULL DEFAULT '0',`WITCHKILLS` int(11) NOT NULL DEFAULT '0',`IRONGOLEMKILLS` int(11) NOT NULL DEFAULT '0',`SNOWGOLEMKILLS` int(11) NOT NULL DEFAULT '0',`ENDERDRAGONKILLS` int(11) NOT NULL DEFAULT '0',`WITHERBOSSKILLS` int(11) NOT NULL DEFAULT '0',`CATKILLS` int(11) NOT NULL DEFAULT '0',`PANDAKILLS` int(11) NOT NULL DEFAULT '0',`PILLAGERKILLS` int(11) NOT NULL DEFAULT '0',`RAVAGERKILLS` int(11) NOT NULL DEFAULT '0',`TRADERLLAMAKILLS` int(11) NOT NULL DEFAULT '0',`WANDERINGTRADERKILLS` int(11) NOT NULL DEFAULT '0',`GIANTKILLS` int(11) NOT NULL DEFAULT '0',`ILLUSIONERKILLS` int(11) NOT NULL DEFAULT '0',`KILLERBUNNYKILLS` int(11) NOT NULL DEFAULT '0',`PUFFERFISHDEATHS` int(11) NOT NULL DEFAULT '0',`DOLPHINDEATHS` int(11) NOT NULL DEFAULT '0',`LLAMADEATHS` int(11) NOT NULL DEFAULT '0',`POLARBEARDEATHS` int(11) NOT NULL DEFAULT '0',`WOLFDEATHS` int(11) NOT NULL DEFAULT '0',`CAVESPIDERDEATHS` int(11) NOT NULL DEFAULT '0',`ENDERMANDEATHS` int(11) NOT NULL DEFAULT '0',`SPIDERDEATHS` int(11) NOT NULL DEFAULT '0',`ZOMBIEPIGMANDEATHS` int(11) NOT NULL DEFAULT '0',`CREEPERDEATHS` int(11) NOT NULL DEFAULT '0',`ELDERGUARDIANDEATHS` int(11) NOT NULL DEFAULT '0',`GUARDIANDEATHS` int(11) NOT NULL DEFAULT '0',`PHANTOMDEATHS` int(11) NOT NULL DEFAULT '0',`SILVERFISHDEATHS` int(11) NOT NULL DEFAULT '0',`SLIMEDEATHS` int(11) NOT NULL DEFAULT '0',`DROWNEDDEATHS` int(11) NOT NULL DEFAULT '0',`HUSKDEATHS` int(11) NOT NULL DEFAULT '0',`ZOMBIEDEATHS` int(11) NOT NULL DEFAULT '0',`ZOMBIEVILLAGERDEATHS` int(11) NOT NULL DEFAULT '0',`SKELETONDEATHS` int(11) NOT NULL DEFAULT '0',`STRAYDEATHS` int(11) NOT NULL DEFAULT '0',`WITHERSKELETONDEATHS` int(11) NOT NULL DEFAULT '0',`BLAZEDEATHS` int(11) NOT NULL DEFAULT '0',`GHASTDEATHS` int(11) NOT NULL DEFAULT '0',`MAGMACUBEDEATHS` int(11) NOT NULL DEFAULT '0',`ENDERMITEDEATHS` int(11) NOT NULL DEFAULT '0',`SHULKERDEATHS` int(11) NOT NULL DEFAULT '0',`EVOKERDEATHS` int(11) NOT NULL DEFAULT '0',`VINDICATORDEATHS` int(11) NOT NULL DEFAULT '0',`VEXDEATHS` int(11) NOT NULL DEFAULT '0',`WITCHDEATHS` int(11) NOT NULL DEFAULT '0',`IRONGOLEMDEATHS` int(11) NOT NULL DEFAULT '0',`SNOWGOLEMDEATHS` int(11) NOT NULL DEFAULT '0',`ENDERDARGONDEATHS` int(11) NOT NULL DEFAULT '0',`WITHERBOSSDEATHS` int(11) NOT NULL DEFAULT '0',`PILLAGERDEATHS` int(11) NOT NULL DEFAULT '0',`RAVAGERDEATHS` int(11) NOT NULL DEFAULT '0',`GIANTDEATHS` int(11) NOT NULL DEFAULT '0',`ILLUSIONERDEATHS` int(11) NOT NULL DEFAULT '0',`KILLERBUNNYDEATHS` int(11) NOT NULL DEFAULT '0',`ELYTRADEATHS` int(11) NOT NULL DEFAULT '0',`GRAVITYDEATHS` int(11) NOT NULL DEFAULT '0',`ENVIRONMENTDEATHS` int(11) NOT NULL DEFAULT '0', PRIMARY KEY (`ID`), UNIQUE KEY `ID` (`ID`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1";

        int teleportTableCreationResponse;
        try {
            teleportTableCreationResponse = connection.createStatement().executeUpdate(teleportTableCreationQuery);
        } catch (SQLException e) {
            teleportTableCreationResponse = -1;
            System.out.println("Encountered " + e.toString() + " during createServerStatsTable()");
            makeLog(e);
        }
        System.out.println(teleportTableCreationResponse + "");
        return true;
    }


    public void makeLog(Exception tr) {
        System.out.println("Creating new log folder - " + new File(this.getDataFolder() + File.separator + "logs").mkdir());
        String dateTimeString = LocalDateTime.now().toString().replace(":", "_").replace("T", "__");
        File file = new File(this.getDataFolder() + File.separator + "logs" + File.separator + dateTimeString + "-" + tr.getCause() + ".log");
        try {
            PrintStream ps = new PrintStream(file);
            tr.printStackTrace(ps);
            System.out.println(this.getDescription().getName() + " - " + this.getDescription().getVersion() + "Encountered Error of type: " + tr.getCause());
            System.out.println("A log file has been generated at " + this.getDataFolder() + File.separator + "logs" + File.separator + dateTimeString + "-" + tr.getCause() + ".log");
            ps.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error creating new log file for " + getDescription().getName() + " - " + getDescription().getVersion());
            System.out.println("Error was as follows");
            System.out.println(e.getMessage());
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean getCD(Player p) {
        if (getConfig().get("CommandCooldowns." + p.getName()) != null) {
            long now = System.currentTimeMillis();
            long lastRun = getConfig().getLong("CommandCooldowns." + p.getName());
            int timer = getConfig().getInt("CommandDelay") * 1000;
            boolean cooldown = (now - timer) > lastRun;

            if (!cooldown) {
                p.sendMessage(badge + "That command is currently on cooldown.");
            }

            setCooldown(p);

            return (cooldown);
        } else {
            setCooldown(p);
            return false;
        }
    }

    public boolean setCooldown(Player p) {
        getConfig().set("CommandCooldowns." + p.getName(), System.currentTimeMillis());
        return true;
    }

    public ItemStack getHubItem() {
        File menuYml = new File(getDataFolder(), "hubMenu.yml");
        FileConfiguration hubMenu = YamlConfiguration.loadConfiguration(menuYml);

        ItemStack configItem = new ItemStack(Material.getMaterial(hubMenu.getString("hubItem.item")), 1);
        ItemMeta confMeta = configItem.getItemMeta();
        confMeta.setDisplayName(ChatColor.valueOf(hubMenu.getString("hubItem.colour").toUpperCase()) + hubMenu.getString("hubItem.name"));
        List<String> confItemLore = new ArrayList<String>();
        confItemLore.add(hubMenu.getString("hubItem.lore"));
        confMeta.setLore(confItemLore);
        configItem.setItemMeta(confMeta);
        return configItem;
    }

    private void registerNewCommand(String fallback, BukkitCommand command) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(fallback, command);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }


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

    public YamlConfiguration getYaml(String path, String file) {
        file = File.separator + file;
        File theYml = new File(path, file);

        if (!theYml.exists()) {
            try {
                theYml.createNewFile();
            } catch (IOException e) {
                makeLog(e);
            }
        }
        return YamlConfiguration.loadConfiguration(theYml);
    }

    public void saveFile(File file, FileConfiguration conf, CommandSender s) {
        try {
            conf.save(file);
        } catch (IOException e) {
            makeLog(e);
            s.sendMessage("Saving " + file + " failed.");
        }
    }

    public void shakeTP(Player p, Location loc) {
        loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());
        lockMove.putIfAbsent(p.getUniqueId(), p.getName());
        p.setInvulnerable(true);
        p.teleport(loc);
        p.sendMessage(badge + "As a safety feature you have been locked in place for 3 seconds.");

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                p.setInvulnerable(false);
                lockMove.remove(p.getUniqueId());
            }
        }, 60L);
    }
}

