package cz.uhk.fim.rssfeeder.gui;

import cz.uhk.fim.rssfeeder.model.RSSItem;

import javax.swing.*;
import java.awt.*;


public class CardView extends JPanel {

    private static final int ITEM_WIDTH = 180;
    private static final int COMPONENT_WIDTH = 160;
    private static final int HEIGHT = 1;

    final String startHtml = "<html><p style='width:" + COMPONENT_WIDTH + " px'>";
    final String endHtml = "</p></html>";
    String title;
    private Color textColor;

    public CardView(RSSItem item, int schema) {
        setLayout(new WrapLayout());
        setSize(ITEM_WIDTH, HEIGHT);
        title = item.getTitle();
        setTitle(title, schema);
        setDescription(item.getDescrition());
        setInfo(String.format("%s - %s", item.getPubDate(), item.getAuthor()));
        setBackground(textColor);
    }

    public void repaintCard(int schema){
        Color color = getRandomBgColor(title, schema);
        setInverseTextColor(color);
        setBackground(textColor);
    }

    private void setInfo(String format) {
        JLabel lblInfo = new JLabel();
        lblInfo.setSize(COMPONENT_WIDTH, HEIGHT);
        lblInfo.setFont(new Font("Courier New", Font.ITALIC, 10)); // Zdroje: (http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm)
        lblInfo.setText(String.format("%s%s%s", startHtml, format, endHtml));
        add(lblInfo);
    }

    private void setDescription(String descrition) {
        JLabel lblDestription = new JLabel();
        lblDestription.setSize(COMPONENT_WIDTH, HEIGHT);
        lblDestription.setFont(new Font("Courier New", Font.PLAIN, 11)); // Zdroje: (http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm)
        lblDestription.setText(String.format("%s%5.35s%s%s", startHtml, descrition, "...", endHtml));
        add(lblDestription);
    }

    private void setTitle(String title, int schema) {
        JLabel lblTitle = new JLabel();
        lblTitle.setSize(COMPONENT_WIDTH, HEIGHT);
        lblTitle.setFont(new Font("Courier New", Font.BOLD, 12)); // Zdroje: (http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm)
        lblTitle.setText(String.format("%s%s%s", startHtml, title, endHtml));
        add(lblTitle);
        Color color = getRandomBgColor(title, schema);
        setInverseTextColor(color);
    }

    private Color getRandomBgColor(String title, int schema) {
        int length = title.length();
        String[] parts = new String[3];
        int[] colors = new int[3];

        switch (schema) {
            case 1:
                // ruzovy
                parts[0] = title.substring(9, length / 3);
                parts[1] = title.substring(length / 4, 2 * (length / 3));
                parts[2] = title.substring(2 * (length / 3), 2*(length/3));
                break;
            case 2:
                // oranzovy
                parts[0] = title.substring(9, length / 4);
                parts[1] = title.substring(length / 5, 2 * (length / 3));
                parts[2] = title.substring(2 * (length / 4), 2*(length/3));
                break;
            default:
                // klasicky
                parts[0] = title.substring(3, length / 1);
                parts[1] = title.substring(length / 3, 2 * (length / 2));
                parts[2] = title.substring(2 * (length / 3), length);
        }
       // System.out.println(parts[0] + " : " + parts[1] + " : " + parts[2]);
        colors[0] = Math.abs(parts[0].hashCode() / 10000000);
        colors[1] = Math.abs(parts[1].hashCode() / 10000000);
        colors[2] = Math.abs(parts[2].hashCode() / 10000000);
        Color color = new Color(colors[0], colors[1], colors[2]);
        return color;
    }

    private void setInverseTextColor(Color bgcolor) {
        textColor = new Color(255 - bgcolor.getRed(), 255 - bgcolor.getGreen(), 255 - bgcolor.getBlue());
    }
}


