package org.json.modals.world;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class Seed extends BDCommand {
    public Seed() {
        super("seed");
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

        String seed = String.valueOf(player.getWorld().getSeed());

        TextComponent message = new TextComponent(Modal.getPrefix() + "§aWorld Seed: [§f" + seed + "§a]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, seed));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClick to copy").create()));

        player.spigot().sendMessage(message);

        return tabComplBuilder.build();
    }
}
