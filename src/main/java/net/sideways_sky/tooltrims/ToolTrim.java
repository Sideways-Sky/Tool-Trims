package net.sideways_sky.tooltrims;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolTrim {
    private final SmithingTransformRecipe Recipe;
    private final ToolTrimMaterial TrimMaterial;
    private final ToolTrimSmithingTemplate TrimTemplate;
    private final int ModelData;
    private static final NamespacedKey ItemPDCKey = new NamespacedKey(Tool_Trims.Instance, "tool_trim");
    public final String Key;
    private final Material Base;
    public static Map<String, ToolTrim> Trims = new HashMap<>();
    public static boolean hasTrim(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().has(ItemPDCKey);
    }

    public static ToolTrim getTrim(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        String Key = itemMeta.getPersistentDataContainer().get(ItemPDCKey, PersistentDataType.STRING);
        return Trims.get(Key);
    }

    public ToolTrim(String UKey, Material base, ToolTrimMaterial trimMaterial, ToolTrimSmithingTemplate trimTemplate, int modelData) {
        Recipe = new SmithingTransformRecipe(
                new NamespacedKey(Tool_Trims.Instance, UKey + "_recipe"),
                new ItemStack(base),
                new RecipeChoice.MaterialChoice(Material.STRUCTURE_BLOCK),
                new RecipeChoice.MaterialChoice(base),
                new RecipeChoice.MaterialChoice(trimMaterial.material));
        TrimMaterial = trimMaterial;
        TrimTemplate = trimTemplate;
        ModelData = modelData;
        Base = base;
        Key = UKey;
        Bukkit.addRecipe(Recipe);
        Trims.put(UKey, this);
    }

    public boolean IsMyRecipe(SmithingInventory inventory){
        if(
                inventory.getRecipe() == null ||
                inventory.getInputTemplate() == null ||
                inventory.getInputEquipment() == null ||
                inventory.getInputMineral() == null
        )
        {return false;}
        return inventory.getRecipe().getResult().equals(Recipe.getResult()) &&
                Recipe.getTemplate().test(inventory.getInputTemplate()) &&
                TrimTemplate.isMyTemplate(inventory.getInputTemplate().getItemMeta()) &&
                Recipe.getAddition().test(inventory.getInputMineral()) &&
                Recipe.getBase().test(inventory.getInputEquipment());
    }

    private List<? extends net.kyori.adventure.text.Component> getLore(){
        return List.of(
                Component.translatable("item.tooltrims.smithing_template.upgrade", Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)),
                Component.text(" ").append(Component.translatable("tool_trim_pattern.tooltrims."+TrimTemplate.name().toLowerCase(), Style.style(TrimMaterial.color).decoration(TextDecoration.ITALIC, false))),
                Component.text(" ").append(Component.translatable("trim_material.tooltrims."+TrimMaterial.name().toLowerCase(), Style.style(TrimMaterial.color).decoration(TextDecoration.ITALIC, false)))
        );
    }

    public ItemStack Transform(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasLore()){
            List<Component> lore = itemMeta.lore();
            lore.addAll(getLore());
            itemMeta.lore(lore);
        } else {
            itemMeta.lore(getLore());
        }
        itemMeta.setCustomModelData(ModelData);
        itemMeta.getPersistentDataContainer().set(ItemPDCKey, PersistentDataType.STRING, Key);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack UndoTransform(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasLore()){
            List<Component> lore = itemMeta.lore();
            lore.removeAll(getLore());
            itemMeta.lore(lore);
        }
        itemMeta.setCustomModelData(null);
        itemMeta.getPersistentDataContainer().remove(ItemPDCKey);
        item.setItemMeta(itemMeta);
        return item;
    }
}
