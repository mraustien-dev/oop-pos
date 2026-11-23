public class Transaction {
    private String id;
    private String dateTime;
    private String cashier;
    private double total;
    private double cash;
    private String items;

    public Transaction(String id, String dateTime, String cashier, double total, double cash, String items) {
        this.id = id;
        this.dateTime = dateTime;
        this.cashier = cashier;
        this.total = total;
        this.cash = cash;
        this.items = items;
    }

    public double getChange() {
        return Math.round((cash - total) * 100) / 100.0;
    }

    public String getReceipt() {
        String text = "        JavaPOS Store\n";
        text += "Receipt : " + id + "\n";
        text += "Date    : " + dateTime + "\n";
        text += "Cashier : " + cashier + "\n";
        text += "------------------------------\n";
        for (String line : items.split(";")) {
            text += line + "\n";
        }
        text += "------------------------------\n";
        text += "Total   : PHP " + String.format("%.2f", total) + "\n";
        text += "Cash    : PHP " + String.format("%.2f", cash) + "\n";
        text += "Change  : PHP " + String.format("%.2f", getChange()) + "\n";
        return text;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCashier() {
        return cashier;
    }

    public double getTotal() {
        return total;
    }

    public double getCash() {
        return cash;
    }

    public String getItems() {
        return items;
    }
}
