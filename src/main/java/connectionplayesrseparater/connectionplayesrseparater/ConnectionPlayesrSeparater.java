package connectionplayesrseparater.connectionplayesrseparater;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class ConnectionPlayesrSeparater extends Plugin implements Listener {
    private static Map<String, Boolean> isServerOnline = new HashMap<>();
    public static ConnectionPlayesrSeparater CPS;
    private void checkOnline() {
        for (ServerInfo info: getProxy().getServers().values().stream().filter(s -> isServerOnline.containsKey(s.getName())).collect(Collectors.toList())) {
            info.ping((result, error) -> isServerOnline.put(info.getName(), error != null));
        }
    }
    private int lastLobby = 0;

    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        if(!event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {return;}
        List<ServerInfo> lobbyServers = isServerOnline.keySet().stream().filter(s -> isServerOnline.get(s)).map(s -> getProxy().getServerInfo(s)).collect(Collectors.toList());
        if(lobbyServers.size() == 0) {
            event.setCancelled(true);
        }
        lastLobby = (lastLobby + 1) % lobbyServers.size();
        event.setTarget(lobbyServers.get(lastLobby));
    }

    @Override
    public void onEnable() {
        CPS = this;
        reloadConfig();
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this , new Command("cps"));
        getProxy().getScheduler().schedule(this, new Task(), 0 , 10 , TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadConfig() {
        isServerOnline.clear();
        Configuration config = null;
        File file = new File(getDataFolder(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(config != null) {
            config.getStringList("Servers").forEach(l -> isServerOnline.put(l , false));
        }
    }

    public static class Task implements Runnable {
        @Override
        public void run() {
            CPS.checkOnline();
        }
    }
}
