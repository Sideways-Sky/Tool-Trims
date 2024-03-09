package net.sideways_sky.tooltrims;

import net.sideways_sky.tooltrims.geyser.GeyserHook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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


        int model_data = 311000;
        for (ToolTrimSmithingTemplate trim: ToolTrimSmithingTemplate.values()) {
            for (ToolTrimMaterial trim_material: ToolTrimMaterial.values()) {

                model_data++;
                String trimName = (trim.name() + "_" + trim_material.name()).toLowerCase();
                Events.ItemCompatibility.modalDataToTrimName.put(model_data, trimName);

                for (String tool: tools) {
                    for (String tool_material: tool_materials) {

                        String materialBaseName = tool_material + "_" + tool;
                        Material base = Material.getMaterial(materialBaseName);
                        if(base == null){
                            ConsoleSend("Unknown Material Base: " + materialBaseName);
                            continue;
                        }
                        String key = (materialBaseName + "_" + trimName).toLowerCase();
                        new ToolTrim(
                                key, base, trim_material, trim, model_data
                        );
                    }
                }
            }
        }

        for (ToolTrimSmithingTemplate trim: ToolTrimSmithingTemplate.values()) {
            Bukkit.addRecipe(trim.dupeRecipe);
        }

        saveDataPack();

        try {
            new GeyserHook();
            ConsoleSend("Geyser found - hooked");
        } catch (ClassNotFoundException exception) {
            ConsoleSend("Geyser not found - disabling hook");
        }

        getServer().getPluginManager().registerEvents(new Events(), this);
    }
    public void saveDataPack(){
        File file = new File(getServer().getWorldContainer().toPath().resolve("world"+File.separator+"datapacks").toString(),"Tool-Trims(with plugin).zip");
        if(!file.exists()){

            InputStream in = getResource("Tool-Trims(with plugin).zip");
            if(in == null){
                getLogger().severe("Missing resource: Datapack");
                return;
            }

            try {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                getLogger().severe("Unable to install datapack. Please manually install (grab from plugin folder)");
                saveResource("Tool-Trims(with plugin).zip", true);
            }
        }
    }
}
