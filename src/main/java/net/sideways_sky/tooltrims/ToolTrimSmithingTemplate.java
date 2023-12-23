package net.sideways_sky.tooltrims;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public enum ToolTrimSmithingTemplate {
    LINEAR(313001),
    TRACKS(313002),
    CHARGE(313003),
    FROST(313004);
    public final ItemStack item;

    public String getDisplayName(){
        String lower = this.name().toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

    ToolTrimSmithingTemplate(int modelData) {
        ItemStack x = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta xMeta = x.getItemMeta();
        xMeta.setCustomModelData(modelData);
        xMeta.displayName(Component.translatable("item.tooltrims.smithing_template", Style.style(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));
        xMeta.lore(List.of(
                Component.translatable("tool_trim_pattern.tooltrims."+this.name().toLowerCase(), Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)),
                Component.empty(),
                Component.translatable("item.tooltrims.smithing_template.applies_to", Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)),
                Component.text(" ").append(Component.translatable("item.tooltrims.smithing_template.tool_trim.applies_to", Style.style(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))),
                Component.translatable("item.tooltrims.smithing_template.ingredients", Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)),
                Component.text(" ").append(Component.translatable("item.tooltrims.smithing_template.tool_trim.ingredients", Style.style(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)))
        ));
        x.setItemMeta(xMeta);
        this.item = x;
    }
}
