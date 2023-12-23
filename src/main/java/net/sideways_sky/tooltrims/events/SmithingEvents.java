package net.sideways_sky.tooltrims.events;

import net.sideways_sky.tooltrims.ToolTrim;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

public class SmithingEvents implements Listener {
    @EventHandler
    public static void PrepareSmithingEvent(PrepareSmithingEvent e){
        if(
                e.getInventory().getRecipe() == null ||
                e.getResult() == null ||
                e.getInventory().getInputTemplate() == null ||
                e.getInventory().getInputEquipment() == null ||
                e.getInventory().getInputMineral() == null
        )
        {return;}
        ItemStack item = e.getResult();

        if(ToolTrim.hasTrim(e.getInventory().getInputEquipment())){
            e.setResult(new ItemStack(Material.AIR));
        } else if(ToolTrim.hasTrim(item)){
            return;
        }

        for (ToolTrim trim : ToolTrim.Trims) {
            if(trim.IsMyRecipe(e.getInventory())){
                e.setResult(trim.Transform(item));
                return;
            }
        }

    }
}
