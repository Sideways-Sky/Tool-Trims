package net.sideways_sky.tooltrims.commands;

import net.sideways_sky.tooltrims.ToolTrimSmithingTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveToolTrimItems implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Player Only Command");
            return true;
        }

        if(command.getName().equalsIgnoreCase("givetooltrimitems")) {
            player.getInventory().addItem(
                    ToolTrimSmithingTemplate.LINEAR.item,
                    ToolTrimSmithingTemplate.TRACKS.item,
                    ToolTrimSmithingTemplate.CHARGE.item,
                    ToolTrimSmithingTemplate.FROST.item
            );
        }

        return true;
    }
}
