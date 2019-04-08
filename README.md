# TreeboTeleport

## Commands:
```
  /hub - Open hub GUI

  Server Transfers: (Are entirely customizable in the servers.yml)
  /skyblock - Teleport you to Sky Server
  /skygrid - Teleport you to Sky Server
  /acisislands - Teleport you to Sky Server
  /prison - Teleport you to Prison Server
  /survial - Teleport you to Survival Server
  /hardcore - Teleport you to Hardcore Server
  /creative - Teleport you to Creative Server
  /plots - Teleport you to Creative Server
  /comp - Teleports you to the Creative Competition Plots world.
  /test- Teleport you to Test Server
  /lobby - Teleport you to Hub Server
  /games - Teleport you to Games Server
  ```


## Commands:
```
Players:
  /spawn -  Allows a player to return to spawn as defined in spawns.yml
    permission: tbteleport.player.spawn

  /hub - Opens Hub Gui as defined in hubmenu.yml
    permission: tbteleport.player.hub

  /givehubitem:
    description: Gives player the hub item as defined in hubmenu.yml
    permission: tbteleport.player.givehubitem

  /wild - Random Teleport in current world. Range can be set in the config.yml
    permission: tbteleport.player.wild

  /warp - Open the warps gui.
  /warp <warp name>  - Attempt to teleport a player to the warp key as defined in the warps.yml
    permission: tbteleport.player.warp

  /home - if no default is set, will open the homes gui. If a default is set, it will attempt to teleport the player to the default home.
  /home <home name> - Will attempt to send player to home key as defined in TreeboTeleport/homes/<player uuid>.yml
  /homes - Opens the Homes Gui
  /homes setdefault <home name>
    permission: tbteleport.player.home

  /sethome <home name> - Creates a player home.
    permission: tbteleport.player.sethome

  /delhome <home name> - Deletes a player home.
    permission: tbteleport.player.delhome

  /configurehomes set <home name> <icon | colour> <new value> - Allows a player to configure their home gui.
    permission: tbteleport.player.configure.homes

  /bed - Allows a player to teleport to their bed location
    permission: tbteleport.player.bed

  /tpahere <player name> - Requests a player teleports to the you
  /tpa <playername> - Requests permission to teleport to another player
  /tpok - Accepts a teleport request
  /tpaccept - Accepts a teleport request
  /tpyes - Accepts a teleport request
    permission: tbteleport.player.tp


Staff:
  /tp <x coord> <y coord> <z coord> - 'Force TP to location'
  /tp <world name> <x coord> <y coord> <z coord> - Force tp to world at location
  /tp <player name> - Force teleport sender to player.
  /tp <player name 1> <player name 2> - Force tp player 1 to player 2
    permission: tbtelerport.staff.tp


  /setwarp <warp name> <(optional)permission required> - Creates a new warp at current location.
    permission: tbteleport.staff.warps.setwarp

  /deletewarp <warp name> - Deletes warp with name <warp name>
    permission: tbteleport.staff.warps.deletewarp

  /configurewarps set <icon | colour | title> <warp id> <new value> - Configures the warps gui.
    permission: tbteleport.staff.warps.configure

  /sendspawn <player> - Sends target player to world spawn.
    permission: tbteleport.staff.sendspawn

  /configurehub set rows <1 - 6> - Sets total rows in hub gui.
  /congigurehub set <icon | label | colour | command> <menu item position> <new value>
    permission: tbteleport.staff.hub.configure

  /clearmychat - Spams 30 blank lines into your personal chat box.
    permission: tbteleport.staff.clearmychat

  /nameit <new name> - Sets display name of item in main hand (Accepts colour codes with &)
    permission: tbteleport.staff.nameit

Admin:
  /setworldspawn - Set a worlds spawn location in spawns.yml
  /sws - Sets the VANILLA world spawn
  /gws - Returns the VANILLA world spawn
  /ttelereload - Reloads TreeboTeleport config.yml
  /ttelesaveconfig - Writes current config back to config.yml file
    permission: tbteleport.admin
```

##Permissions:
```
Player:
  tbteleport.player.hub: - Use /hub
  tbteleport.player.wild: - Use /wild
  tbteleport.player.tp: - Use /tpa, /tpahere, /tpok, /tpyes, /tpaccept
  tbteleport.player.givehubitem: Use /giveHubItem
  tbteleport.player.warp: Use /warp
  tbteleport.player.home: Use /home, /homes
  tbteleport.player.sethome: Use /sethome
  tbteleport.player.delhome: Use /deletehome
  tbteleport.player.bed: Use /bed
  tbteleport.player.spawn: Use /spawn
  tbteleport.maxhomes.3: Sets the maximum number of homes a player can have.
    default: true

 Staff:
  tbteleport.staff.nameit: Use /nameit
  tbteleport.staff.clearmychat: Use /clearMyChat
  tbteleport.staff.setwarp: Use /setwarp
  tbteleport.staff.deletewarp: Use /deletewarp
  tbteleport.staff.sendspawn: Use /sendspawn
  tbteleport.staff.homes.others: Use /homes <player>
  tbteleport.staff.wild.other: Use /wild <player>
    default: op

Admin:
  tbteleport.updatechecker: Notifies player of plugin updates.
  tbteleport.admin: Use /sws, /gws, /setworldspawn, tteleReload, tteleSaveConfig
    default: op
```