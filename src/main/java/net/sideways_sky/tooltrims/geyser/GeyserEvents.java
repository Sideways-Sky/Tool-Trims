package net.sideways_sky.tooltrims.geyser;

import net.sideways_sky.tooltrims.ToolTrim;
import net.sideways_sky.tooltrims.ToolTrimSmithingTemplate;
import net.sideways_sky.tooltrims.Tool_Trims;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.EventBus;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserLoadResourcePacksEvent;
import org.geysermc.geyser.api.item.custom.CustomItemData;

import java.io.File;

public class GeyserEvents implements EventRegistrar {
    public record CustomGeyserItem (CustomItemData itemData, String javaBaseItemId){}
    public GeyserEvents(GeyserHook hook){
        EventBus<EventRegistrar> bus = hook.api.eventBus();
        bus.register(this, this);
        bus.subscribe(this, GeyserLoadResourcePacksEvent.class, this::onGeyserLoadResourcePacks);
        bus.subscribe(this, GeyserDefineCustomItemsEvent.class, this::onGeyserDefineCustomItems);
    }
    @Subscribe
    public void onGeyserDefineCustomItems(GeyserDefineCustomItemsEvent e){
        for (ToolTrimSmithingTemplate template : ToolTrimSmithingTemplate.values()){
            CustomGeyserItem item = template.geyserItem();
            e.register(item.javaBaseItemId, item.itemData);
        }
        for (ToolTrim trim : ToolTrim.Trims){
            CustomGeyserItem item = trim.geyserItem();
            e.register(item.javaBaseItemId, item.itemData);
        }
    }
    @Subscribe
    public void onGeyserLoadResourcePacks(GeyserLoadResourcePacksEvent e){
        File rp = new File(Tool_Trims.Instance.getDataFolder(), "pack.mcpack");
        if(!rp.exists()){
            Tool_Trims.Instance.saveResource("pack.mcpack", true);
        }
        e.resourcePacks().add(rp.toPath());
    }
}
