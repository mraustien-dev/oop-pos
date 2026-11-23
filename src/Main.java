public class Main {
    public static void main(String[] args) {
        Style.setup();
        Store store = new Store();
        LoginWindow loginWindow = new LoginWindow(store);
        loginWindow.setVisible(true);
    }
}
