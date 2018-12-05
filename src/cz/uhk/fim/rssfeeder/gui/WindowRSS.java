package cz.uhk.fim.rssfeeder.gui;


import cz.uhk.fim.rssfeeder.model.RSSSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowRSS extends JDialog {

    String textNazev;
    String textLink;
    RSSSource updatingRSS;

    public WindowRSS(MainFrame mf, String title) {
        this(mf, title, new RSSSource());
    }

    public WindowRSS(MainFrame mf, String title, RSSSource selected) {
        super(mf, title, true);
        updatingRSS = selected;
        setLocationRelativeTo(mf);
        setSize(500, 150);
        textNazev = updatingRSS.getName();
        textLink = updatingRSS.getSource();
        initContentUI();
    }

    private void initContentUI() {
        JDialog self = this;
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        JButton buttonOK = new JButton("Ok");
        JButton buttonCancel = new JButton("Cancel");
        JLabel linkNazev = new JLabel("Název RSS");
        JLabel linkLink = new JLabel("Link");
        JTextField textFieldNazev = new JTextField(textNazev);
        JTextField textFieldLink = new JTextField(textLink);

        Box southBox = Box.createHorizontalBox();
        southBox.add(Box.createHorizontalGlue());
        southBox.add(buttonOK);
        southBox.add(Box.createHorizontalGlue());
        southBox.add(buttonCancel);
        southBox.add(Box.createHorizontalGlue());

        northPanel.add(linkNazev, BorderLayout.NORTH);
        northPanel.add(textFieldNazev, BorderLayout.SOUTH);

        centerPanel.add(linkLink, BorderLayout.NORTH);
        centerPanel.add(textFieldLink, BorderLayout.CENTER);

        southPanel.add(southBox, BorderLayout.SOUTH);

        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                self.setVisible(false);
                self.dispose();
            }
        });
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((new RSSSource(textFieldNazev.getText(), textFieldLink.getText()).isValidRSS())) {
                    updatingRSS.setName(textFieldNazev.getText());
                    updatingRSS.setSource(textFieldLink.getText());
                }
                self.setVisible(false);
                self.dispose();
            }
        });

    }

    public RSSSource showDialog() {
        setVisible(true);
        return updatingRSS;
    }
}
