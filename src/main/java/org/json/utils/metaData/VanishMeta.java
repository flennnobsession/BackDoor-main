package org.json.utils.metaData;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.json.Modal;

public class VanishMeta {

    private static final NamespacedKey nameSpacedKey = new NamespacedKey(Modal.getInstance(), "Vanish");

    public static void add(Player player) {
        if (isListed(player)) return;
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(nameSpacedKey, PersistentDataType.STRING, "true");
    }

    public static void remove(Player player) {
        if (!isListed(player)) return;
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(nameSpacedKey);
    }

    public static boolean isListed(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();

        if (data.has(nameSpacedKey, PersistentDataType.STRING)) {
            String roleValue = data.get(nameSpacedKey, PersistentDataType.STRING);
            if (roleValue != null) {
                return roleValue.contains("true");
            }
        }

        return false;
    }
}
