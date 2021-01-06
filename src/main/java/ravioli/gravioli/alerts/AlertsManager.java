package ravioli.gravioli.alerts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlertsManager {
    private static long nextId = 1;

    private final Map<Long, Alert> alerts = new LinkedHashMap<>();

    public void addAlert(String text) {
        this.alerts.put(nextId, new Alert(nextId, text));
        nextId++;
    }

    public boolean removeAlert(Long id) {
        return this.alerts.remove(id) != null;
    }

    public ArrayList<Alert> getAlerts() {
        return new ArrayList<>(this.alerts.values());
    }

    public static class Alert {
        private final long id;
        private final String text;

        private Alert(long id, String text) {
            this.id = id;
            this.text = text;
        }

        public long getId() {
            return this.id;
        }

        public String getText() {
            return this.text;
        }
    }
}
