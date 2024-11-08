package net.sideways_sky.tooltrims;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Set;

public final class Tool_Trims extends JavaPlugin {
    public static Tool_Trims Instance;

    private boolean justInstalledDataPack = false;

    @Override
    public void onLoad() {
        Instance = this;
        File file = new File(getServer().getWorldContainer().toPath().resolve("world"+File.separator+"datapacks").toString(), "Tool Trims (plugin) DP.zip");
        if(!file.exists()){

            InputStream in = getResource("Tool Trims (plugin) DP.zip");
            if(in == null){
                getLogger().severe("Missing resource: Datapack");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            try {
                getLogger().info("Installing datapack...");
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                justInstalledDataPack = true;
            } catch (IOException e) {
                getLogger().severe("Unable to install datapack. Please manually install (grab from plugin folder)");
                saveResource("Tool Trims (plugin) DP.zip", true);
            }
        } else {
            getLogger().info("Datapack found. skipping installation");
        }
    }

    @Override
    public void onEnable() {
        if(justInstalledDataPack){
            getLogger().warning("Reloading data...");
            getServer().reloadData();
            getLogger().info("Data reloaded. Finished installing datapack");
        }

        Set<Material> tools = Set.of(
                Material.NETHERITE_SWORD,       Material.DIAMOND_SWORD,     Material.GOLDEN_SWORD,      Material.IRON_SWORD,    Material.STONE_SWORD,   Material.WOODEN_SWORD,
                Material.NETHERITE_PICKAXE,     Material.DIAMOND_PICKAXE,   Material.GOLDEN_PICKAXE,    Material.IRON_PICKAXE,  Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_AXE,         Material.DIAMOND_AXE,       Material.GOLDEN_AXE,        Material.IRON_AXE,      Material.STONE_AXE,     Material.WOODEN_AXE,
                Material.NETHERITE_SHOVEL,      Material.DIAMOND_SHOVEL,    Material.GOLDEN_SHOVEL,     Material.IRON_SHOVEL,   Material.STONE_SHOVEL,  Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE,         Material.DIAMOND_HOE,       Material.GOLDEN_HOE,        Material.IRON_HOE,      Material.STONE_HOE,     Material.WOODEN_HOE,
                Material.BOW, Material.CROSSBOW, Material.MACE);


        int model_data = 311000;
        for (ToolTrimSmithingTemplate trim: ToolTrimSmithingTemplate.values()) {
            for (ToolTrimMaterial trim_material: ToolTrimMaterial.values()) {

                model_data++;
                String trimName = (trim.name() + "_" + trim_material.name()).toLowerCase();
                Events.ItemCompatibility.modalDataToTrimName.put(model_data, trimName);

                for (Material tool: tools) {
                    String key = (tool.name() + "_" + trimName).toLowerCase();
                    new ToolTrim(
                            key, tool, trim_material, trim, model_data
                    );
                }
            }
        }

        for (ToolTrimSmithingTemplate trim: ToolTrimSmithingTemplate.values()) {
            Bukkit.addRecipe(trim.dupeRecipe);
        }

        getServer().getPluginManager().registerEvents(new Events(), this);
    }

}
