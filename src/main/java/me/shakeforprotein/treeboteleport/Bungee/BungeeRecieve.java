package me.shakeforprotein.treeboteleport.Bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeRecieve implements PluginMessageListener {

    TreeboTeleport pl;
    private BungeeSend bungeeSend;
    public String[] playerList;


    public BungeeRecieve(TreeboTeleport main) {
        this.pl = main;
        bungeeSend = new BungeeSend(main);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.startsWith(pl.getName() + "Channel-")) {
            Short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String msgData = msgin.readUTF(); // Read the data in the same way you wrote it
                if (subchannel.endsWith("CrossServerTeleport")) {
                    for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
                        if (targetPlayer.getName().equalsIgnoreCase(msgData.split(",")[1])) {
                            bungeeSend.sendConnectOther(pl.getConfig().getString("general.serverName"), msgData.split(",")[0]);
                            Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                                @Override
                                public void run() {
                                    Player telporter = Bukkit.getPlayer(msgData.split(",")[0]);
                                    Player reciever = Bukkit.getPlayer(msgData.split(",")[1]);
                                    System.out.println(telporter.getName());
                                    System.out.println(reciever.getName());

                                    telporter.teleport(reciever);
                                }
                            }, 40L);
                        }
                    }

                } else if (subchannel.endsWith("CrossServerTPAHere")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(msgData.split(",")[2])) {
                            String command = "";

                            p.sendMessage("Player " + ChatColor.GOLD + msgData.split(",")[1] + ChatColor.RESET + " would like you to teleport " + ChatColor.GOLD + "TO THEM");
                            command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
                            pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                            pl.getConfig().set("tpRequest." + p.getName() + ".requester", msgData.split(",")[1]);
                        }
                    }

                } else if (subchannel.endsWith("CrossServerTPA")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(msgData.split(",")[2])) {
                            p.sendMessage(pl.badge + "Player " + ChatColor.GOLD + msgData.split(",")[1] + ChatColor.RESET + " would like to teleport " + ChatColor.GOLD + "TO YOU");
                            String command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            pl.getConfig().set("tpRequest." + p.getName() + ".type", "toPlayer");
                            pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                            pl.getConfig().set("tpRequest." + p.getName() + ".requester", msgData.split(",")[1]);
                        }
                    }
                }
                else if (subchannel.endsWith("Jsaw")) {
                    Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getPlayer(msgData.split(",")[1]).teleport(Bukkit.getWorld(msgData.split(",")[0]).getSpawnLocation());
                        }
                    },40L);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
