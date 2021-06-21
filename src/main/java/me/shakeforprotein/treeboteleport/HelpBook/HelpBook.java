package me.shakeforprotein.treeboteleport.HelpBook;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HelpBook {

    private TreeboTeleport pl;
    private ArrayList<BaseComponent[]> pages = new ArrayList<>();
    private int counter = 3;

    public HelpBook(TreeboTeleport main){
        this.pl = main;
        pages.add(createTitlePage("Treebo Teleport"));
        pages.add(createTableOfContents());
        pages.add(createTableOfContents2());
        pages.addAll(createOtherPages());
        pl.roots.helpHandler.registerHelpBook("TreeboTeleport", "TreeboMC", pages);

        pl.roots.commandsGui.registerPlugin(pl, "openHelpBook-TreeboTeleport", new ItemStack(Material.ENDER_EYE, 1), ChatColor.translateAlternateColorCodes('&', "Treebo Teleport Help"), getLoreList());
    }

    private BaseComponent[] createTitlePage(String title){

        TextComponent titlePage = new TextComponent(title);
        titlePage.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
        titlePage.setUnderlined(true);

        BaseComponent[] titlePageComponent = new ComponentBuilder(titlePage).create();
        return titlePageComponent;
    }

    private BaseComponent[] createTableOfContents(){
        int i = counter;
        TextComponent textComponent = new TextComponent(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Contents (P1/2)\n");

        TextComponent newText = new TextComponent(ChatColor.DARK_BLUE + "/Back\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Bed\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/ClearMyChat\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/DeleteHome\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/DisableTpSafety\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/GiveHubItem\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Home\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Homes\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Hub\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/SetHome\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Spawn\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Top\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);
        BaseComponent[] tableOfContents= new ComponentBuilder(textComponent).create();
        counter = i;
        return tableOfContents;
    }

    private BaseComponent[] createTableOfContents2(){
        TextComponent textComponent = new TextComponent(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Contents (P2/2)\n");
        int i = counter;

        TextComponent newText = new TextComponent(ChatColor.DARK_BLUE + "/Ypa\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Ypahere\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/TpNo\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/TpToggle\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/TpYes\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Warp\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Warps\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        newText = new TextComponent(ChatColor.DARK_BLUE + "/Wild\n");
        i = i+1;
        newText.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, i + ""));
        newText.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
        textComponent.addExtra(newText);

        BaseComponent[] tableOfContents= new ComponentBuilder(textComponent).create();
        return tableOfContents;
    }

    private List<BaseComponent[]> createOtherPages(){

        List<BaseComponent[]> pageList = new ArrayList<>();

        pageList.add(new ComponentBuilder("/Back\n\nThe back command, available to players with the [Seedling] or above rank will return you to the last location minecraft has registered a teleport event from.").create());
        pageList.add(new ComponentBuilder("/Bed\n\nThe bed command, available to all players, will return you to your registered bed location, assuming it is still valid.").create());
        pageList.add(new ComponentBuilder("/ClearMyChat\n\nThe ClearMyChat command will add 30 blank lines to the bottom of your chat box. This is useful if you are trying to see the results of something normally displayed in the chat window.").create());
        pageList.add(new ComponentBuilder("/DeleteHome\n\nThe DeleteHome command will unregister a /home. In doing so you will no longer be able to teleport to that location. \n\nUsage: /DeleteHome <home name>\neg: /deletehome castle").create());
        pageList.add(new ComponentBuilder("/DisableTpSafety\n\nIf enabled, the DisableTpSafety command will disable TreeboTeleport's built in protections against terrain loading delays. Be warned, this may result in you teleporting into a wall.").create());
        pageList.add(new ComponentBuilder("/GiveHubItem\n\nThe GiveHubItem command will issue you with a new Hub Compass in the event you have lost yours.").create());
        pageList.add(new ComponentBuilder("/Home\n\nThe Home command on it's own will return you to your stored default home. If an argument is specified it will attempt to send you to your home with that name.\n\nUsage:\n/home\n/home castle").create());
        pageList.add(new ComponentBuilder("/Homes\n\nThe Homes command will display a customisable GUI listing up to 45 saved home locations. By default, players are given three homes per server with more purchasable on our store.").create());
        pageList.add(new ComponentBuilder("/Hub\n\nThe Hub command will display the hub GUI, which is used to transfer between our servers.").create());
        pageList.add(new ComponentBuilder("/SetHome\n\nThe sethome command allows you to define new homes up until your home limit.\n\nUsage:\n/sethome <home name>\neg. /sethome castle").create());
        pageList.add(new ComponentBuilder("/Spawn\n\nThe spawn command will teleport you to the spawn location for a given world. In most cases this will teleport you to the main world for a given server.").create());
        pageList.add(new ComponentBuilder("/Top\n\nThe Top command, available to [Seedling] rank and above will teleport you to the highest safe solid block at your current X/Y coordinates. ").create());
        pageList.add(new ComponentBuilder("/Tpa\n\nThe Tpa command will request another player allow you to teleport to THEIR location.\n\nUsage:\n /tpa <player name>\neg./tpa notch").create());
        pageList.add(new ComponentBuilder("/TpaHere\n\nThe TpaHere command will request another player teleports to YOUR location.\n\nUsage: /tpahere <player nane>\neg. /tpahere notch").create());
        pageList.add(new ComponentBuilder("/TpNo\n\nThe TpNo command denies an inbound /tpa or /tpahere command").create());
        pageList.add(new ComponentBuilder("/TpToggle\n\nThe TpToggle command will enable or disable you seeing inbound teleport requests.").create());
        pageList.add(new ComponentBuilder("/TpYes\n\nThe TpYes command will act as a confirmation and permission for an inbound /tpa or /tpahere request from another player.\n\nAlias: /tpok").create());
        pageList.add(new ComponentBuilder("/Warp\n\nThe warp command will allow you to teleport to locations predefined by the staff team.\n\nUsage: /warp shops").create());
        pageList.add(new ComponentBuilder("/Warps\n\nThe warps command will display a context sensitive GUI showing all /warp locations available to you.").create());
        pageList.add(new ComponentBuilder("/Wild (1/2)\n\nThe wild command will teleport you to a pseudo-random location in the current world within bounds from world spawn as configured by staff. By adding a numerical argument,").create());
        pageList.add(new ComponentBuilder("/Wild (2/2)\n\nIt is possible to teleport to a location at a distance centred on your current location.\n\nUsage:\n/wild\n/wild 5000" ).create());
        return pageList;
    }

    private List<String> getLoreList(){
        List<String> loreList = new ArrayList<>();

        loreList.add(ChatColor.translateAlternateColorCodes('&', pl.badge + "-Help"));
        loreList.add(ChatColor.translateAlternateColorCodes('&', ""));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "This book will provide more detailed"));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "help on Treebo's teleport commands"));

        return loreList;
    }
}
