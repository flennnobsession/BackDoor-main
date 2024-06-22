package org.json.modals.world;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.VanishMeta;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class Vanish extends BDCommand {

    public Vanish() {
        super("vanish");
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

        VanishMeta.add(player);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(Modal.getInstance(), player);
        }

        player.sendMessage(Modal.getPrefix() + "§aYou are now vanished!");
        return tabComplBuilder.build();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getOnlinePlayers().stream().filter(VanishMeta::isListed).forEach(vanishedPlayer -> player.hidePlayer(Modal.getInstance(), vanishedPlayer));
    }
}
