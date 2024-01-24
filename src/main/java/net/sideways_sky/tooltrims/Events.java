package net.sideways_sky.tooltrims;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;

import static net.sideways_sky.tooltrims.Tool_Trims.ConsoleSend;

public class Events implements Listener {
    @EventHandler
    public static void onPrepareSmithing(PrepareSmithingEvent e){
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
            e.setResult(ItemStack.empty());
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
}
