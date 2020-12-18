package net.lapismc.afkplusrandommessages;

import net.lapismc.afkplus.api.AFKStartEvent;
import net.lapismc.afkplus.api.AFKStopEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class AFKPlusRandomMessages extends JavaPlugin implements Listener {

    private final HashMap<UUID, Integer> stopPair = new HashMap<>();
    private Random r;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        r = new Random();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void AFKStart(AFKStartEvent e) {
        List<String> messages = getConfig().getStringList("StartMessages");
        int i = r.nextInt(messages.size());
        String startMessage = messages.get(i);
        e.setBroadcastMessage(applyFormatting(startMessage));

        if (getConfig().getBoolean("Pairs")) {
            stopPair.put(e.getPlayer().getUUID(), i);
        }
    }

    @EventHandler
    public void AFKStop(AFKStopEvent e) {
        List<String> messages = getConfig().getStringList("StopMessages");
        int i;
        if (stopPair.containsKey(e.getPlayer().getUUID())) {
            i = stopPair.get(e.getPlayer().getUUID());
            stopPair.remove(e.getPlayer().getUUID());
        } else {
            i = r.nextInt(messages.size());
        }
        String stopMessage = messages.get(i);
        e.setBroadcastMessage(applyFormatting(stopMessage));
    }

    private String applyFormatting(String rawMessage) {
        String message = getConfig().getString("Format", "&8[&6AFK&4+&8] {MSG}").replace("{MSG}", rawMessage);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

}
