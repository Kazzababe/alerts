package ravioli.gravioli.alerts;

import org.bukkit.plugin.java.JavaPlugin;
import ravioli.gravioli.alerts.command.AlertsCommand;

public class Alerts extends JavaPlugin {
    private final AlertsManager alertsManager;

    public Alerts() {
        this.alertsManager = new AlertsManager();
    }

    @Override
    public void onEnable() {
        this.getCommand("alerts").setExecutor(new AlertsCommand(this));
    }

    public AlertsManager getAlertsManager() {
        return this.alertsManager;
    }
}
