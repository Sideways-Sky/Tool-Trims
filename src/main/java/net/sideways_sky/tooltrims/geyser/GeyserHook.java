package net.sideways_sky.tooltrims.geyser;

import org.bukkit.Bukkit;
import org.geysermc.geyser.api.GeyserApi;

public class GeyserHook {

    public GeyserApi api;
    public GeyserEvents events;

    public GeyserHook() throws ClassNotFoundException {
        if (!setup()) {
            throw new ClassNotFoundException();
        }
    }

    private boolean setup() {
        if(Bukkit.getPluginManager().getPlugin("Geyser-Spigot") == null){
            return false;
        }
        api = GeyserApi.api();
        events = new GeyserEvents(this);
        return true;
    }
}
