package net.sideways_sky.tooltrims;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sideways_sky.tooltrims.Tool_Trims.ConsoleSend;

public class Events implements Listener {
    @EventHandler
    public static void onPrepareSmithing(PrepareSmithingEvent e){
        if(
                e.getInventory().getRecipe() == null ||
                e.getResult() == null ||
                e.getInventory().getInputTemplate() == null ||
                e.getInventory().getInputEquipment() == null ||
                e.getInventory().getInputMineral() == null ||
                        e.getInventory().getInputTemplate().getType() != Material.STRUCTURE_BLOCK
        )
        {return;}

        ItemStack item = e.getResult();

        ItemCompatibility.checkUpdateTool(e.getInventory().getInputEquipment());

        if(ToolTrim.hasTrim(e.getInventory().getInputEquipment())){
            ToolTrim trim = ToolTrim.getTrim(item);
            if(trim != null){
                item = trim.UndoTransform(item);
            }
        } else if(ToolTrim.hasTrim(item)){
            return;
        }

        for (ToolTrim trim : ToolTrim.Trims.values()) {
            if(trim.IsMyRecipe(e.getInventory())){
                e.setResult(trim.Transform(item));
                return;
            }
        }
    }

    @EventHandler
    public static void onPickupItemEvent(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof HumanEntity)){return;}
        e.getItem().setItemStack(ItemCompatibility.checkUpdateTemplate(e.getItem().getItemStack()));
    }
    @EventHandler
    public static void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
        String message = e.getMessage().toLowerCase();
        if(!(
                message.contains("give") || message.contains("loot") || message.contains("item")
                )){
            return;
        }
        Bukkit.getScheduler().runTaskLater(Tool_Trims.Instance, () -> {
            PlayerInventory playerInv = e.getPlayer().getInventory();
            ItemStack[] items = playerInv.getContents();
            for (int i = 0; i < items.length; i++) {
                items[i] = ItemCompatibility.checkUpdateTemplate(items[i]);
            }
            playerInv.setContents(items);
        }, 1);
    }
    @EventHandler
    public static void onLootGenerate(LootGenerateEvent e){
        List<ItemStack> loot = e.getLoot();
        loot.replaceAll(ItemCompatibility::checkUpdateTemplate);
    }

    @EventHandler
    public static void onServerLoad(ServerLoadEvent e){
        if(Tool_Trims.justInstalledDataPack && e.getType() == ServerLoadEvent.LoadType.STARTUP){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"datapack list");
            Bukkit.getScheduler().runTaskLater(Tool_Trims.Instance, () -> {
                Bukkit.getDatapackManager().getPacks().stream().filter(datapack -> datapack.getName().equals("file/Tool-Trims(with plugin).zip")).findFirst().ifPresent(
                        datapack -> {
                            ConsoleSend("Enabling datapack");
                            datapack.setEnabled(true);
                        }
                );
            }, 1);

        }
    }

    public static class ItemCompatibility {

        public static Map<Integer, String> modalDataToTrimName = new HashMap<>();
        public static ItemStack checkUpdateTemplate(ItemStack in){
            if(
                    in == null || in.getType() != Material.STRUCTURE_BLOCK || !in.getItemMeta().hasCustomModelData()
            ){return in;}
            switch (in.getItemMeta().getCustomModelData()){
                case 313001 -> {
                    return ToolTrimSmithingTemplate.LINEAR.item.asQuantity(in.getAmount());
                }
                case 313002 -> {
                    return ToolTrimSmithingTemplate.TRACKS.item.asQuantity(in.getAmount());
                }
                case 313003 -> {
                    return ToolTrimSmithingTemplate.CHARGE.item.asQuantity(in.getAmount());
                }
                case 313004 -> {
                    return ToolTrimSmithingTemplate.FROST.item.asQuantity(in.getAmount());
                }
            }
            return in;
        }
        public static ItemStack checkUpdateTool(ItemStack in){
            if(
                    in == null || !in.getItemMeta().hasCustomModelData() || ToolTrim.hasTrim(in)
            ){return in;}
            String trimName = modalDataToTrimName.get(in.getItemMeta().getCustomModelData());
            if(trimName == null){return in;}
            String key = (in.getType().name() + "_" + trimName).toLowerCase();
            ToolTrim trim = ToolTrim.Trims.get(key);
            if(trim == null){return in;}
            return trim.Transform(trim.UndoTransform(in));
        }
    }
}