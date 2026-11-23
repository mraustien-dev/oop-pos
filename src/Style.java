import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

// One place for all colors, fonts and ready-made styled parts.
// Every screen uses these, so the whole app looks the same.
// All methods are "static": you call them like Style.button("Save", Style.BLUE).
public class Style {
    // Colors
    public static final Color BLUE = new Color(37, 99, 235);
    public static final Color GREEN = new Color(22, 163, 74);
    public static final Color RED = new Color(220, 38, 38);
    public static final Color GRAY = new Color(100, 116, 139);
    public static final Color DARK = new Color(30, 41, 59);
    public static final Color LIGHT = new Color(241, 245, 249);   // window background
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color WHITE = Color.WHITE;

    // Fonts
    public static final String FONT_NAME = "Segoe UI";

    public static Font font(int size) {
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

    public static Font boldFont(int size) {
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    // Call once at the start of the program.
    public static void setup() {
        UIManager.put("swing.boldMetal", false);
        UIManager.put("Label.font", font(14));
        UIManager.put("Button.font", boldFont(13));
        UIManager.put("TextField.font", font(14));
        UIManager.put("PasswordField.font", font(14));
        UIManager.put("Table.font", font(14));
        UIManager.put("TableHeader.font", boldFont(13));
        UIManager.put("TabbedPane.font", boldFont(14));
        UIManager.put("TabbedPane.selected", WHITE);
        UIManager.put("OptionPane.messageFont", font(14));
        UIManager.put("OptionPane.buttonFont", boldFont(13));
        UIManager.put("Panel.background", LIGHT);
        UIManager.put("OptionPane.background", LIGHT);
    }

    // A colored button with white text.
    public static JButton button(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // hand pointer on hover
        return button;
    }

    // A text box with a light border and some space inside.
    public static JTextField textField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    public static void styleField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    // A big bold heading.
    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(boldFont(18));
        label.setForeground(DARK);
        return label;
    }

    // A small gray label.
    public static JLabel smallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(13));
        label.setForeground(GRAY);
        return label;
    }

    // A white box with a title at the top. Add your content to its CENTER / SOUTH.
    public static JPanel card(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        card.add(title(title), BorderLayout.NORTH);
        return card;
    }

    // A small white box that shows a name and a big number (used in History).
    public static JPanel statCard(String name, JLabel valueLabel, Color color) {
        valueLabel.setFont(boldFont(24));
        valueLabel.setForeground(color);
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, color), // colored strip on the left
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.add(smallLabel(name));
        card.add(valueLabel);
        return card;
    }

    // Makes a table look clean: taller rows, colored header, no cell editing.
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null); // read-only
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(DARK);
        table.getTableHeader().setBackground(DARK);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 32));
        table.setFillsViewportHeight(true); // white below the last row, not gray
        table.setBackground(WHITE);
    }

    // Puts a table inside a scroll box with a soft border.
    public static JScrollPane scroll(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        return scrollPane;
    }

    // A box that shows receipt text in a fixed-width font.
    public static JTextArea receiptBox(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return area;
    }
}
