package me.shakeforprotein.treeboteleport;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import me.shakeforprotein.treeboteleport.Bungee.BungeeRecieve;
import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.Commands.*;
import me.shakeforprotein.treeboteleport.Commands.NameIt;
import me.shakeforprotein.treeboteleport.Commands.TabCompleters.TabCompleterTp;
import me.shakeforprotein.treeboteleport.Listeners.*;
import me.shakeforprotein.treeboteleport.Methods.Teleports.ToWorld;
import me.shakeforprotein.treeboteleport.UpdateChecker.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
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
    public HashMap lockMove = new HashMap<UUID, Long>();
    public HashMap commCooldown = new HashMap<Player, Long>();
    public HashMap lastLocConf = new HashMap<UUID, Location>();
    public HashMap tpSafetyOff = new HashMap<UUID, String>();

    private AddMaxHomes addMaxHomes = new AddMaxHomes(this);
    private Back back = new Back(this);
    private Bed bed = new Bed(this);
    private ClearMyChat clearMyChat = new ClearMyChat(this);
    private ConfigureHomes configureHomes = new ConfigureHomes(this);
    private ConfigureHubMenu configureHubMenu = new ConfigureHubMenu(this);
    private ConfigureWarps configureWarps = new ConfigureWarps(this);
    private DeleteHome deleteHome = new DeleteHome(this);
    private DeleteWarp deleteWarp = new DeleteWarp(this);
    private DisableTpSafety disableTpSafety = new DisableTpSafety(this);
    private GetWorldSpawn getWorldSpawn = new GetWorldSpawn(this);
    private GiveHubItem giveHubItem = new GiveHubItem(this);
    private Home home = new Home(this);
    private Homes homes = new Homes(this);
    private Hub hub = new Hub(this);
    private MayITp mayITp = new MayITp(this);
    private NameIt nameIt = new NameIt(this);
    private Reload reload = new Reload(this);
    private RestorePlayerInventory restorePlayerInventory = new RestorePlayerInventory(this);
    private SaveConfig saveConfig = new SaveConfig(this);
    private SendSpawn sendSpawn = new SendSpawn(this);
    private SetShop setShop = new SetShop(this);
    private SetHome setHome = new SetHome(this);
    private SetTTeleCooldown setTTeleCooldown = new SetTTeleCooldown(this);
    private SetVanillaWorldSpawn setVanillaWorldSpawn = new SetVanillaWorldSpawn(this);
    private SetWarp setWarp = new SetWarp(this);
    private SetWorldSpawn setWorldSpawn = new SetWorldSpawn(this);
    private Shop shop = new Shop(this);
    private ShowMaxHomes showMaxHomes = new ShowMaxHomes(this);
    private Spawn spawn = new Spawn(this);
    //private ToggleDeathDocket toggleDeathDocket = new ToggleDeathDocket(this);
    private Tp tp = new Tp(this);
    private Top top = new Top(this);
    private Tp2Me tp2Me = new Tp2Me(this);
    private Tp2MePls tp2MePls = new Tp2MePls(this);
    private Tp2Player tp2Player = new Tp2Player(this);
    private Tp2Pos tp2Pos = new Tp2Pos(this);
    private Tp2WorldAt tp2WorldAt = new Tp2WorldAt(this);
    private TpNo tpNo = new TpNo(this);
    private TpOk tpOk = new TpOk(this);
    private TpToggle tpToggle = new TpToggle(this);
    private Version version = new Version(this);
    private WarpTo warpTo = new WarpTo(this);
    private Wild2 wild = new Wild2(this);
    private JoinServerAtWorld jsaw = new JoinServerAtWorld(this);
    public static List<String> fullPlayerList = new ArrayList<>();
    private BungeeSend bungeeSend = new BungeeSend(this);


    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeRecieve(this));

        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        getConfig().set("version", this.getDescription().getVersion());
        saveConfig();
        if (getConfig().get("bstatsIntegration") != null) {
            if (getConfig().getBoolean("bstatsIntegration")) {
                Metrics metrics = new Metrics(this);
            }
        }
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

        /*
            this.getCommand("hub").setExecutor(new Hub(this));
            this.getCommand("wild").setExecutor(new Wild(this));
            this.getCommand("gws").setExecutor(new GetWorldSpawn(this));
            this.getCommand("sws").setExecutor(new SetVanillaWorldSpawn(this));
            this.getCommand("tp").setExecutor(new Tp(this));
            this.getCommand("tpahere").setExecutor(new Tp2MePls(this));
            this.getCommand("tpa").setExecutor(new MayITp(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("tpask").setExecutor(new MayITp(this)); //TODO: MAY HAVE ISSUES
            this.getCommand("tpok").setExecutor(new TpOk(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("tpaccept").setExecutor(new TpOk(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("tpyes").setExecutor(new TpOk(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("givehubitem").setExecutor(new GiveHubItem(this));
            this.getCommand("setwarp").setExecutor(new SetWarp(this));
            this.getCommand("deletewarp").setExecutor(new DeleteWarp(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("delwarp").setExecutor(new DeleteWarp(this));  //TODO: MAY HAVE ISSUES
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
            this.getCommand("tpno").setExecutor(new TpNo(this));   //TODO: MAY HAVE ISSUES
            this.getCommand("tpdeny").setExecutor(new TpNo(this));  //TODO: MAY HAVE ISSUES
            this.getCommand("tpcancel").setExecutor(new TpNo(this));  //TODO: MAY HAVE ISSUES
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

        addMaxHomes.register("AddMaxHomes");
        back.register("Back");
        bed.register("Bed");
        clearMyChat.register("ClearMyChat");
        configureHomes.register("ConfigureHomes");
        configureHubMenu.register("ConfigureHubMenu");
        configureWarps.register("ConfigureWarps");
        deleteHome.register("DeleteHome");
        deleteHome.register("DelHome");
        deleteWarp.register("DeleteWarp");
        deleteWarp.register("DelWarp");
        disableTpSafety.register("DisableTpSafety");
        disableTpSafety.register("ToggleTpSafety");
        getWorldSpawn.register("gws");
        getWorldSpawn.register("GetWorldSpawn");
        giveHubItem.register("GiveHubItem");
        home.register("Home");
        homes.register("Homes");
        hub.register("Hub");
        mayITp.register("MayITp");
        mayITp.register("tpa");
        mayITp.register("tpask");
        nameIt.register("NameIt");
        reload.register("ttelereload");
        restorePlayerInventory.register("RestorePlayerInventory");
        saveConfig.register("ttelesaveconfig");
        sendSpawn.register("SendSpawn");
        setHome.register("SetHome");
        setShop.register("SetShop");
        setTTeleCooldown.register("SetTTeleCoolDown");
        setVanillaWorldSpawn.register("SetVanillaWorldSpawn");
        setWarp.register("SetWarp");
        setWorldSpawn.register("SetWorldSpawn");
        shop.register("Shop");
        showMaxHomes.register("ShowMaxHomes");
        spawn.register("Spawn");
        //toggleDeathDocket.register("ToggleDeathDocket");
        getCommand("tp").setExecutor(tp);
        getCommand("tp").setTabCompleter(new TabCompleterTp());
        if(getConfig().getBoolean("disabledCommands.tp")){
           unRegisterBukkitCommand(getCommand("tp"));
        }
        tp2Me.register("tp2me");
        tp2Me.register("tphere");
        tp2MePls.register("tp2mePls");
        tp2MePls.register("tpahere");
        tpNo.register("tpno");
        tpNo.register("tpdeny");
        tpOk.register("tpok");
        tpOk.register("tpyes");
        tpToggle.register("tptoggle");
        version.register("tteleversion");
        warpTo.register("warp");
        warpTo.register("warps");
        wild.register("wild");
        top.register("top");
        jsaw.register("jsaw");
        //tp2Player.register

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

        getConfig().set("KillZombies", null);
        getConfig().set("ReplacePhantomsWithPissedOffWolves", null);


        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                bungeeSend.getPlayerList("ALL");
            }
        }, 200, 40);

        //createDefaultFile("", "homes", true);
        //createDefaultFile("", "servers.yml", false);
        //createDefaultFile("", "spawns.yml", false);
        //createDefaultFile("", "hubMenu.yml", false);
        //createDefaultFile("", "warps.yml", false);
        //createDefaultFile("", "lastLocation.yml", false);


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
        if (commCooldown.containsKey(p)) {
            long now = System.currentTimeMillis();
            long lastRun = (Long) commCooldown.get(p);
            int maxCooldown = getConfig().getInt("CommandDelay") * 1000;

            if (now - (lastRun + maxCooldown) > 0) {
                setCooldown(p);
                return false;
            } else {
                return true;
            }
        } else {
            setCooldown(p);
            return false;
        }
    }

    public void setCooldown(Player p) {
        commCooldown.putIfAbsent(p, System.currentTimeMillis());
        commCooldown.replace(p, System.currentTimeMillis());
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

    public void registerNewCommand(String fallback, BukkitCommand command) {
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
        //loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());
/*
        if (getConfig().get("teleportProtection") != null && getConfig().getInt("teleportProtection") > 0) {
            int tpProtection = getConfig().getInt("teleportProtection") * 20;
            lockMove.putIfAbsent(p.getUniqueId(), p.getName());
            p.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    p.setInvulnerable(false);
                    if (lockMove.containsKey(p.getUniqueId())) {
                        lockMove.remove(p.getUniqueId());
                    }
                }
            }, tpProtection);
            p.sendMessage(badge + "As a safety feature you have been locked in place for " + getConfig().getInt("teleportProtection") + " seconds.");
        }*/
        //loc = loc.add(0,0.5,0);
        p.teleport(loc);
    }

    private static Object getPrivateField(Object object, String field)throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }
    public static void unRegisterBukkitCommand(PluginCommand cmd) {
        try {
            Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());
            for (String alias : cmd.getAliases()){
                if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(cmd.getName())){
                    knownCommands.remove(alias);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

