package models;

public class AuditLogEntry {
    private String timestamp;
    private String action;
    private String partCode;
    private int quantity;

    public AuditLogEntry(String timestamp, String action, String partCode, int quantity) {
        this.timestamp = timestamp;
        this.action = action;
        this.partCode = partCode;
        this.quantity = quantity;
    }

    public String getTimestamp() { return timestamp; }
    public String getAction() { return action; }
    public String getPartCode() { return partCode; }
    public int getQuantity() { return quantity; }
}