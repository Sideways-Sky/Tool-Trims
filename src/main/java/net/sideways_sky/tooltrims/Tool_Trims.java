package net.sideways_sky.tooltrims;

import net.sideways_sky.tooltrims.commands.GiveToolTrimItems;
import net.sideways_sky.tooltrims.events.SmithingEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Tool_Trims extends JavaPlugin {


    public static Tool_Trims Instance;

    public static void ConsoleSend(String message){
        Bukkit.getConsoleSender().sendMessage("[Tool Trims]: " + message);
    }

    @Override
    public void onEnable() {

        Instance = this;

        List<String> tool_materials = List.of("NETHERITE", "DIAMOND", "GOLDEN", "IRON", "STONE", "WOODEN");
        List<String> tools = List.of("SWORD", "PICKAXE", "AXE", "SHOVEL", "HOE");

        for (String tool: tools) {
            for (String tool_material: tool_materials) {
                int model_data = 311000;
                for (ToolTrimSmithingTemplate trim: ToolTrimSmithingTemplate.values()) {
                    for (ToolTrimMaterial trim_material: ToolTrimMaterial.values()) {
                        model_data++;
                        Material base = Material.getMaterial(tool_material + "_" + tool);
                        if(base == null){
                            ConsoleSend("Unknown Material Base: " + tool_material + "_" + tool);
                            continue;
                        }
                        String key = (tool_material + "_" + tool + "_" + trim.name() + "_" + trim_material.name()).toLowerCase();
                        new ToolTrim(
                                key, base, trim_material, trim, model_data
                        );
                    }
                }
            }
        }

        getServer().getPluginManager().registerEvents(new SmithingEvents(), this);
        getCommand("givetooltrimitems").setExecutor(new GiveToolTrimItems());
    }
}
