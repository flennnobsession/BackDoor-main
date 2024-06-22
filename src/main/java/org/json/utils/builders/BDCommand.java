package org.json.utils.builders;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public abstract class BDCommand implements Listener {

    String name;

    public BDCommand(String commandName) {
        this.name = commandName;
    }

    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
