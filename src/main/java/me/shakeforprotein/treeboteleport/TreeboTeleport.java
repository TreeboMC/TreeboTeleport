package me.shakeforprotein.treeboteleport;

import me.shakeforprotein.treeboteleport.Commands.Hub;
import me.shakeforprotein.treeboteleport.UpdateChecker.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;

public final class TreeboTeleport extends JavaPlugin {




    @Override
    public void onEnable() {
        // Plugin startup logic
        /*
        host = getConfig().getString("host");
        port = getConfig().getInt("port");
        database = getConfig().getString("database");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
        table = getConfig().getString("transferTable");

        createTable(table);


        try {
            openConnection();
            Statement statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            int doesNothing = 1;
        }
        */
        //Set Command Executors
        this.getCommand("hub").setExecutor(new Hub(this));

        getConfig().options().copyDefaults(true);
        getConfig().set("Version", this.getDescription().getVersion());
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


    public boolean createTable(String table){
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
        String dateTimeString = LocalDateTime.now().toString().replace(":", "_").replace("T","__");
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
}

