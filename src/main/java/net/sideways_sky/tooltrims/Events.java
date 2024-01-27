package net.sideways_sky.tooltrims;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Events implements Listener {
    @EventHandler
    public static void onPrepareSmithing(PrepareSmithingEvent e){
        if(
                e.getInventory().getRecipe() == null ||
                e.getResult() == null ||
                e.getInventory().getInputTemplate() == null ||
                e.getInventory().getInputEquipment() == null ||
                e.getInventory().getInputMineral() == null ||
                        e.getInventory().getInputTemplate().getType() != Material.STRUCTURE_BLOCK ||
                        !e.getInventory().getInputTemplate().getItemMeta().hasCustomModelData()
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
    public static class ItemCompatibility {
        public static Map<Integer, String> modalDataToTrimName = new HashMap<>();
        public static void checkUpdateTool(ItemStack in){
            if(
                    in == null || !in.getItemMeta().hasCustomModelData() || ToolTrim.hasTrim(in)
            ){return;}
            String trimName = modalDataToTrimName.get(in.getItemMeta().getCustomModelData());
            if(trimName == null){return;}
            String key = (in.getType().name() + "_" + trimName).toLowerCase();
            ToolTrim trim = ToolTrim.Trims.get(key);
            if(trim == null){return;}
            trim.Transform(trim.UndoTransform(in));
        }
    }
}