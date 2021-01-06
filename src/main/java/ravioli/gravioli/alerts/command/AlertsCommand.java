package ravioli.gravioli.alerts.command;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.primitives.Longs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ravioli.gravioli.alerts.Alerts;
import ravioli.gravioli.alerts.AlertsManager;
import ravioli.gravioli.alerts.util.ItemBuilder;

import java.util.Arrays;
import java.util.List;

public class AlertsCommand implements CommandExecutor {
    private final Alerts plugin;

    public AlertsCommand(Alerts plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                this.openAlertsMenu((Player) commandSender);
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l! &cThis command can only be sent by players."));
            }
        } else if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("add")) {
                this.plugin.getAlertsManager().addAlert(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l! &aYou have added a new alert. You can use the command &e/alerts&a to view all alerts."));
            } else if (args[0].equalsIgnoreCase("remove") && args.length == 2) {
                Long alertId = Longs.tryParse(args[1]);
                if (alertId != null) {
                    if (this.plugin.getAlertsManager().removeAlert(alertId)) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l! &aYou have successfully removed this alert."));
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l! &cThere is no alert found with that id."));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l! &cPlease input a valid number for the alert id."));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l! &cInvalid command usage: /alert [add,remove] [alert,id]."));
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l! &cInvalid command usage: /alert [add,remove] [alert,id]."));
        }
        return true;
    }

    private void openAlertsMenu(Player player) {
        List<AlertsManager.Alert> alerts = this.plugin.getAlertsManager().getAlerts();

        ChestGui menu = new ChestGui(4, "Alerts");

        PaginatedPane basePane = new PaginatedPane(0, 0, 9, 4);

        for (int i = 0; i <= alerts.size() / 27; i++) {
            StaticPane contentsPane = new StaticPane(0, 0, 9, 3);

            outerLoop:
            for (int y = 0, k = i * 27; y < 3; y++) {
                for (int x = 0; x < 9; x++, k++) {
                    if (k >= alerts.size()) {
                        break outerLoop;
                    }
                    final AlertsManager.Alert alert = alerts.get(k);
                    GuiItem item = new GuiItem(
                            new ItemBuilder(Material.PAPER)
                                    .setDisplayName(ChatColor.YELLOW + "Alert #" + alert.getId())
                                    .setLore(
                                            "",
                                            ChatColor.YELLOW + "Alert message:",
                                            ChatColor.GRAY + "\"" + ChatColor.ITALIC + alert.getText() + "\""
                                    )
                                    .build(),
                            (event) -> {
                                event.setCancelled(true);
                            });
                    contentsPane.addItem(item, x, y);
                }
            }
            contentsPane.fillWith(new ItemStack(Material.AIR));

            StaticPane navigationPane = new StaticPane(0, 3, 9, 1);
            GuiItem backItem = new GuiItem(
                    new ItemBuilder(Material.ARROW)
                            .setDisplayName(ChatColor.WHITE + "← Previous page")
                            .build(),
                    (event) -> {
                        if (basePane.getPage() > 0) {
                            basePane.setPage(basePane.getPage() - 1);
                            menu.update();
                        } else {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.8F, 0.5F);
                        }
                        event.setCancelled(true);
                    });
            GuiItem nextItem = new GuiItem(
                    new ItemBuilder(Material.ARROW)
                            .setDisplayName(ChatColor.WHITE + "Next page →")
                            .build(),
                    (event) -> {
                        if (basePane.getPage() < (alerts.size() - 1) / 27) {
                            basePane.setPage(basePane.getPage() + 1);
                            menu.update();
                        } else {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.8F, 0.5F);
                        }
                        event.setCancelled(true);
                    });
            navigationPane.addItem(backItem, 0, 0);
            navigationPane.addItem(nextItem, 8, 0);

            basePane.addPane(i, contentsPane);
            basePane.addPane(i, navigationPane);
        }
        menu.addPane(basePane);
        menu.show(player);
    }
}
