package net.sideways_sky.tooltrims;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.sideways_sky.tooltrims.geyser.GeyserEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

import java.util.ArrayList;
import java.util.List;

import static net.sideways_sky.tooltrims.Tool_Trims.ConsoleSend;

public class ToolTrim {
    private final SmithingTransformRecipe Recipe;
    private final ToolTrimMaterial TrimMaterial;
    private final ToolTrimSmithingTemplate TrimTemplate;
    private final int ModelData;
    private static final NamespacedKey ItemPDCKey = new NamespacedKey(Tool_Trims.Instance, "tool_trim");
    private final String Key;
    private final Material Base;
    public static List<ToolTrim> Trims = new ArrayList<>();
    public static boolean hasTrim(ItemStack item){
        ItemMeta x = item.getItemMeta();
        return x.getPersistentDataContainer().has(ItemPDCKey);
    }

//    public static ToolTrim getTrim(ItemStack item){
//        ItemMeta itemMeta = item.getItemMeta();
//        String Key = itemMeta.getPersistentDataContainer().get(ItemPDCKey, PersistentDataType.STRING);
//        for (ToolTrim trim : Trims) {
//            if(trim.IsMyKey(Key)){return trim;}
//        }
//        return null;
//    }

    public ToolTrim(String UKey, Material base, ToolTrimMaterial trimMaterial, ToolTrimSmithingTemplate trimTemplate, int modelData) {
        Recipe = new SmithingTransformRecipe(
                new NamespacedKey(Tool_Trims.Instance, UKey + "_recipe"),
                new ItemStack(base),
                new RecipeChoice.ExactChoice(trimTemplate.item),
                new RecipeChoice.MaterialChoice(base),
                new RecipeChoice.MaterialChoice(trimMaterial.material));
        TrimMaterial = trimMaterial;
        TrimTemplate = trimTemplate;
        ModelData = modelData;
        Base = base;
        Key = UKey;
        Bukkit.addRecipe(Recipe);
        Trims.add(this);
    }
    public GeyserEvents.CustomGeyserItem geyserItem(){
        return new GeyserEvents.CustomGeyserItem(
                CustomItemData.builder().customItemOptions(
                        CustomItemOptions.builder().customModelData(ModelData).build()
                ).name(Key).displayName(Base.translationKey()).allowOffhand(true).build(),
                Base.getKey().asString()
        );
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
                Recipe.getAddition().test(inventory.getInputMineral()) &&
                Recipe.getBase().test(inventory.getInputEquipment());
    }

//    public boolean IsMyKey(String testKey){
//        return Key.equalsIgnoreCase(testKey);
//    }

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

//    public ItemStack UndoTransform(ItemStack item){
//        ItemMeta itemMeta = item.getItemMeta();
//        if(itemMeta.hasLore()){
//            List<Component> lore = itemMeta.lore();
//            lore.removeAll(getLore());
//            itemMeta.lore(lore);
//        }
//        itemMeta.setCustomModelData(null);
//        itemMeta.getPersistentDataContainer().remove(ItemPDCKey);
//        item.setItemMeta(itemMeta);
//        return item;
//    }
}
