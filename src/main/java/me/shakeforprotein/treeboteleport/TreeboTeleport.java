package me.shakeforprotein.treeboteleport;

import com.vdurmont.semver4j.Semver;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import me.shakeforprotein.treeboroots.*;
import me.shakeforprotein.treeboteleport.Bungee.BungeeRecieve;
import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.Commands.*;
import me.shakeforprotein.treeboteleport.Commands.NameIt;
import me.shakeforprotein.treeboteleport.Commands.TabCompleters.TabCompleterTp;
import me.shakeforprotein.treeboteleport.HelpBook.HelpBook;
import me.shakeforprotein.treeboteleport.Listeners.*;
import me.shakeforprotein.treeboteleport.Methods.Teleports.ToWorld;
import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class TreeboTeleport extends JavaPlugin {

    public static HashMap<String, Integer> playerCounts = new HashMap<>();
    //remove above for hardcore
    public static List<String> fullPlayerList = new ArrayList<>();
    private TreeboTeleport instance;
    public TreeboRoots roots;
    public BungeeChannelApi bungeeApi = new BungeeChannelApi(this);
    public HashMap<UUID, Long> lockMove = new HashMap<>();
    private HashMap<Player, Long> commCooldown = new HashMap<>();
    public HashMap<UUID, Location> lastLocConf = new HashMap<>();
    public List<String> serverListNew = new ArrayList<>();
    public String badge = ChatColor.translateAlternateColorCodes('&', getConfig().getString("general.messages.badge") + " ");
    public String err = badge + ChatColor.translateAlternateColorCodes('&', getConfig().getString("general.messages.error") + " ");
    private File playerDataFolder;
    private ToWorld toWorld = new ToWorld(this);
    private AddMaxHomes addMaxHomes = new AddMaxHomes(this);
    private Bed bed = new Bed(this);
    private ClearMyChat clearMyChat = new ClearMyChat(this);
    private ConfigureHomes configureHomes = new ConfigureHomes(this);
    private ConfigureHubMenu configureHubMenu = new ConfigureHubMenu(this);
    private ConfigureWarps configureWarps = new ConfigureWarps(this);
    private DeleteHome deleteHome = new DeleteHome(this);
    private DeleteWarp deleteWarp = new DeleteWarp(this);
    private DisableTpSafety disableTpSafety = new DisableTpSafety(this);
    private GetWorldSpawn getWorldSpawn = new GetWorldSpawn(this);
    private Hub hub = new Hub(this);
    private NameIt nameIt = new NameIt(this);
    private Reload reload = new Reload(this);
    private RestorePlayerInventory restorePlayerInventory = new RestorePlayerInventory(this);
    private SaveConfig saveConfig = new SaveConfig(this);
    private SendSpawn sendSpawn = new SendSpawn(this);
    private SetShop setShop = new SetShop(this);
    private SetTTeleCooldown setTTeleCooldown = new SetTTeleCooldown(this);
    private SetVanillaWorldSpawn setVanillaWorldSpawn = new SetVanillaWorldSpawn(this);
    private SetWarp setWarp = new SetWarp(this);
    private SetWorldSpawn setWorldSpawn = new SetWorldSpawn(this);
    private Shop shop = new Shop(this);
    private ShowMaxHomes showMaxHomes = new ShowMaxHomes(this);
    private Spawn spawn = new Spawn(this);
    private Tp tp = new Tp(this);
    private Tp2Me tp2Me = new Tp2Me(this);
    private Version version = new Version(this);
    private WarpTo warpTo = new WarpTo(this);
    private Wild2 wild = new Wild2(this);
    private JoinServerAtWorld jsaw = new JoinServerAtWorld(this);
    //Remove these for hardcore
    private TpNo tpNo = new TpNo(this);
    private TpOk tpOk = new TpOk(this);
    private TpToggle tpToggle = new TpToggle(this);
    private Top top = new Top(this);
    private Tp2MePls tp2MePls = new Tp2MePls(this);
    private Back back = new Back(this);
    private GiveHubItem giveHubItem = new GiveHubItem(this);
    private Home home = new Home(this);
    private Homes homes = new Homes(this);
    private MayITp mayITp = new MayITp(this);

    private SetHome setHome = new SetHome(this);
    private BungeeSend bungeeSend = new BungeeSend(this);


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        instance = this;
        Semver requiredTreeboRootsVersion = new Semver("0.0.0");
        if (this.getServer().getPluginManager().getPlugin("TreeboRoots") != null && new Semver(this.getServer().getPluginManager().getPlugin("TreeboRoots").getDescription().getVersion()).isGreaterThanOrEqualTo(requiredTreeboRootsVersion)) {
            roots = ((TreeboRoots) this.getServer().getPluginManager().getPlugin("TreeboRoots")).getInstance();
            this.setPlayerDataFolder(new File(roots.getDataFolder() + File.separator + "PlayerData"));
            if(playerDataFolder.mkdirs()){
                getLogger().info("Created player data folder in TreeboRoots data tree");
            }


            //TODO: Move this functionality to TreeboRoots
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeRecieve(this));

            backupModifiedConfig();
            saveConfig();

            if (getConfig().get("bstatsIntegration") != null) {
                if (getConfig().getBoolean("bstatsIntegration")) {
                    Metrics metrics = new Metrics(this);
                    if(metrics.isEnabled()){
                        getLogger().info(this.getDescription().getName() + " has registered with BStats");
                    }
                }
            }

            registerCommands();
            registerEventHandlers();

            saveConfig();

            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                    bungeeSend.getPlayerList("ALL");

                    StringBuilder builder = new StringBuilder();
                    builder.append(getConfig().getString("general.serverName"));
                    builder.append("|");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        builder.append(p.getWorld().getName());
                        builder.append("|");
                    }
                    bungeeSend.sendPerWorldPlayerList(builder.toString());
            }, 200, 40);



            registerServerShortcutCommands();

            Bukkit.getScheduler().runTaskLater(this, () -> {
                roots.updateHandler.registerPlugin(instance, "TreeboMC", "TreeboTeleport");
                roots.getInstance().updateHandler.registerPlugin(instance, "TreeboMC", "TreeboTeleport");
                new HelpBook(instance);
                recoverOldFormatHomes();
            }, 100L);
        } else {
            getLogger().warning("was unable to find dependency 'TreeboRoots' or it's version was too low to be compatible. Disabling Self");
            this.getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        purgeFilesOlderThanSevenDays("logs");
        purgeFilesOlderThanSevenDays("deaths");
        saveConfig();
    }

    public boolean getCommandCooldown(Player p) {
        if (commCooldown.containsKey(p)) {
            long now = System.currentTimeMillis();
            long lastRun = commCooldown.get(p);
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

    @SuppressWarnings("ConstantConditions")
    public ItemStack getHubItemFromConfig() {
        File menuYml = new File(getDataFolder(), "hubMenu.yml");
        FileConfiguration hubMenu = YamlConfiguration.loadConfiguration(menuYml);

        ItemStack configItem = new ItemStack(Material.getMaterial(hubMenu.getString("hubItem.item")), 1);
        ItemMeta confMeta = configItem.getItemMeta();
        confMeta.setDisplayName(ChatColor.valueOf(hubMenu.getString("hubItem.colour").toUpperCase()) + hubMenu.getString("hubItem.name"));
        List<String> confItemLore = new ArrayList<>();
        confItemLore.add(hubMenu.getString("hubItem.lore"));
        confMeta.setLore(confItemLore);
        configItem.setItemMeta(confMeta);
        return configItem;
    }

    public void registerNewCommand(String fallback, BukkitCommand command) {
        /*try {

            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            if(commandMap.getCommand(command.getName()) != null){
                System.out.println("Command: " + command.getName() + " - has already been registered by " + Bukkit.getPluginCommand(command.getName()).getPlugin().getDescription().getName());
                Bukkit.broadcastMessage("Command: " + command.getName() + " - has already been registered by " + Bukkit.getPluginCommand(command.getName()).getPlugin().getDescription().getName());
            }
            commandMap.register(fallback, command);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

         */
    }

    public void saveFileConfigurationToFile(File file, FileConfiguration conf, CommandSender s) {
        try {
            conf.save(file);
        } catch (IOException ex) {
            roots.errorLogger.logError(this, ex);
            s.sendMessage("Saving " + file + " failed.");
        }
    }


    public File getPlayerDataFolder() {
        return playerDataFolder;
    }

    private void setPlayerDataFolder(File playerDataFolder) {
        this.playerDataFolder = playerDataFolder;
    }

    @SuppressWarnings("ConstantConditions")
    private void backupModifiedConfig() {
        if (getConfig().getString("version") != null && !getConfig().getString("version").equalsIgnoreCase(this.getDescription().getVersion())) {
            File oldConfig = new File(getDataFolder(), "config-" + this.getDescription().getVersion() + "-" + LocalDateTime.now().toString().replace(":", "_").replace("T", "__") + ".yml");
            try {
                getConfig().save(oldConfig);
            } catch (IOException ex) {
                roots.errorLogger.logError(this, ex);
            }
            getConfig().options().copyDefaults(true);
            getConfig().set("version", this.getDescription().getVersion());
        } else {
            getConfig().options().copyDefaults(true);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void purgeFilesOlderThanSevenDays(String folder) {
        int i = 0;
        File[] files = new File(this.getDataFolder() + File.separator + folder).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                long diff = new Date(System.currentTimeMillis()).getTime() - file.lastModified();

                if (diff > 7 * 24 * 60 * 60 * 1000) {
                    file.delete();
                    i++;
                }
            }
            if (i > 0) {
                System.out.println(badge + "Purged " + i + " files of type " + folder);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerServerShortcutCommands(){

        File serverFile = new File(getDataFolder(), File.separator + "servers.yml");
        FileConfiguration serverList = YamlConfiguration.loadConfiguration(serverFile);

        for (String srv : getConfig().getStringList("ServerList")) {
            playerCounts.put(srv, 0);
        }

        if (!serverFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                serverFile.createNewFile();
                try {
                    serverList.options().copyDefaults();
                    serverList.save(serverFile);
                } catch (FileNotFoundException ex) {
                    roots.errorLogger.logError(this, ex);
                }
            } catch (IOException ex) {
                roots.errorLogger.logError(this, ex);
            }
        }

        for (String item : serverList.getConfigurationSection("servers").getKeys(false)) {
            BukkitCommand item2 = new BukkitCommand(item.toLowerCase()) {
                @SuppressWarnings("NullableProblems")
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
    }


    private static Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    private static void unRegisterBukkitCommand(PluginCommand cmd) {
        try {
            Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());
            for (String alias : cmd.getAliases()) {
                if (knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(cmd.getName())) {
                    knownCommands.remove(alias);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands(){
        try {
            /*addMaxHomes.register("AddMaxHomes");
            configureHubMenu.register("ConfigureHubMenu");
            configureHomes.register("ConfigureHomes");
            bed.register("Bed");
            clearMyChat.register("ClearMyChat");
            configureWarps.register("ConfigureWarps");
            deleteHome.register("DeleteHome");
            deleteHome.register("DelHome");
            deleteWarp.register("DeleteWarp");
            deleteWarp.register("DelWarp");
            //disableTpSafety.register("DisableTpSafety");
            disableTpSafety.register("ToggleTpSafety");
            getWorldSpawn.register("gws");
            getWorldSpawn.register("GetWorldSpawn");
            //hub.register("Hub");
            //nameIt.register("NameIt");
            reload.register("ttelereload");
            restorePlayerInventory.register("RestorePlayerInventory");
            saveConfig.register("ttelesaveconfig");
            sendSpawn.register("SendSpawn");
            setShop.register("SetShop");
            setTTeleCooldown.register("SetTTeleCoolDown");
            setWarp.register("SetWarp");
            setWorldSpawn.register("SetWorldSpawn");
            shop.register("Shop");
            showMaxHomes.register("ShowMaxHomes");
            spawn.register("Spawn");
            tp2Me.register("tp2me");
            tp2Me.register("tphere");
            //disable these for hardcore
            back.register("Back");
            giveHubItem.register("GiveHubItem");
            home.register("Home");
            homes.register("Homes");
            mayITp.register("MayITp");
            mayITp.register("tpa");
            mayITp.register("tpask");
            setHome.register("SetHome");
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
            jsaw.register("jsaw");
            setVanillaWorldSpawn.register("SetVanillaWorldSpawn");
            top.register("top");
            //toggleDeathDocket.register("ToggleDeathDocket");
            Bukkit.broadcastMessage("registering commands");*/


            this.getCommand("addmaxhomes").setExecutor(new AddMaxHomes(this));
            this.getCommand("back").setExecutor(new Back(this));
            this.getCommand("bed").setExecutor(new Bed(this));
            this.getCommand("clearmychat").setExecutor(new ClearMyChat(this));
            this.getCommand("configurehomes").setExecutor(new ConfigureHubMenu(this));
            this.getCommand("configurehubmenu").setExecutor(new ConfigureHubMenu(this));
            this.getCommand("configurewarps").setExecutor(new ConfigureWarps(this));
            this.getCommand("deletehome").setExecutor(new DeleteHome(this));
            this.getCommand("deletewarp").setExecutor(new DeleteWarp(this));
            this.getCommand("delhome").setExecutor(new DeleteHome(this));
            this.getCommand("delwarp").setExecutor(new DeleteWarp(this));
            this.getCommand("getworldspawn").setExecutor(new GetWorldSpawn(this));
            this.getCommand("givehubitem").setExecutor(new GiveHubItem(this));
            this.getCommand("gws").setExecutor(new GetWorldSpawn(this));
            this.getCommand("home").setExecutor(new Home(this));
            this.getCommand("homes").setExecutor(new Homes(this));
            this.getCommand("jsaw").setExecutor(new JoinServerAtWorld(this));
            this.getCommand("restoreplayerinventory").setExecutor(new RestorePlayerInventory(this));
            this.getCommand("sendspawn").setExecutor(new SendSpawn(this));
            this.getCommand("sethome").setExecutor(new SetHome(this));
            this.getCommand("setshop").setExecutor(new SetShop(this));
            this.getCommand("setttelecooldown").setExecutor(new SetTTeleCooldown(this));
            this.getCommand("setvanillaworldspawn").setExecutor(new SetVanillaWorldSpawn(this));
            this.getCommand("setwarp").setExecutor(new SetWarp(this));
            this.getCommand("setworldspawn").setExecutor(new SetWorldSpawn(this));
            this.getCommand("shop").setExecutor(new Shop(this));
            this.getCommand("showmaxhomes").setExecutor(new ShowMaxHomes(this));
            this.getCommand("spawn").setExecutor(new Spawn(this));
            this.getCommand("toggletpsafety").setExecutor(new DisableTpSafety(this));
            this.getCommand("top").setExecutor(new Top(this));
            this.getCommand("tp").setExecutor(tp);
            this.getCommand("tp").setTabCompleter(new TabCompleterTp());
            this.getCommand("tp2mepls").setExecutor(new Tp2MePls(this));
            this.getCommand("tpa").setExecutor(new MayITp(this));
            this.getCommand("tpahere").setExecutor(new Tp2MePls(this));
            this.getCommand("tpask").setExecutor(new MayITp(this));
            this.getCommand("tpdeny").setExecutor(new TpNo(this));
            this.getCommand("tpno").setExecutor(new TpNo(this));
            this.getCommand("tpok").setExecutor(new TpOk(this));
            this.getCommand("tptoggle").setExecutor(new TpToggle(this));
            this.getCommand("tpyes").setExecutor(new TpOk(this));
            this.getCommand("ttelereload").setExecutor(new Reload(this));
            this.getCommand("ttelesaveconfig").setExecutor(new SaveConfig(this));
            this.getCommand("version").setExecutor(new Version(this));
            this.getCommand("warp").setExecutor(new WarpTo(this));
            this.getCommand("warps").setExecutor(new WarpTo(this));
            this.getCommand("wild").setExecutor(new Wild2(this));
            Bukkit.broadcastMessage("done registering commands");
        } catch (NullPointerException ex){

            Bukkit.broadcastMessage(ex.getLocalizedMessage());
        }
    }

    private void registerEventHandlers(){
        getServer().getPluginManager().registerEvents(new HubItemListener(this), this);
        getServer().getPluginManager().registerEvents(new HubMenuInventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new HomeMenuInventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new WarpsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new BedClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

    }

    private void recoverOldFormatHomes(){
        File[] files = new File(getDataFolder() + File.separator + "homes").listFiles();
        if(files != null && files.length > 0) {
            for (File file : files) {
                String name = file.getName().toString().split("\\.")[0];
                File outputDir = new File(playerDataFolder + File.separator + name);
                File outputFile = new File(outputDir, "homes.yml");
                outputDir.mkdirs();
                file.renameTo(outputFile);
            }
        }
    }
}

