package cz.uhk.fim.rssfeeder.gui;

import cz.uhk.fim.rssfeeder.model.RSSItem;
import cz.uhk.fim.rssfeeder.model.RSSItemsList;
import cz.uhk.fim.rssfeeder.model.RSSSource;
import cz.uhk.fim.rssfeeder.utils.FileUtils;
import cz.uhk.fim.rssfeeder.utils.RSSParser;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainFrame extends JFrame {

    private RSSItemsList rssList;

    public void init() {
        setTitle("RSSfeeder");
        setSize(725, 825);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initContentUI();
    }

    private void initContentUI() {
        MainFrame self = this;
        JPanel controlPanel = new JPanel(new BorderLayout());

        Vector sourcesItems = new Vector();
        final DefaultComboBoxModel comboModel = new DefaultComboBoxModel(sourcesItems);
        JComboBox<RSSSource> comboBoxRss = new JComboBox<>(comboModel);

        List<RSSSource> rsslist = null;
        try {
            rsslist = FileUtils.loadSources();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (RSSSource rss : rsslist) {
            comboModel.addElement(rss);
        }

        JPanel northPanel = new JPanel(new BorderLayout());
        controlPanel.add(northPanel, BorderLayout.NORTH);

        // buttony - vytvoreni
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton load = new JButton("Load Example items");
        JButton remove = new JButton("Remove");

        // buttony - pridani do boxu
        Box boxButton = Box.createHorizontalBox();
        boxButton.add(add);
        boxButton.add(edit);

        boxButton.add(remove);

        // pridani do northPanelu
        northPanel.add(comboBoxRss, BorderLayout.NORTH);
        northPanel.add(boxButton, BorderLayout.SOUTH);

        //The buttons
        JRadioButton prvniRB = new JRadioButton("Theme 1");
        JRadioButton druhyRB = new JRadioButton("Theme 2");
        JRadioButton tretiRB = new JRadioButton("Theme 3");

        //The Group, make sure only one button is selected at a time in the group
        ButtonGroup editableGroup = new ButtonGroup();
        editableGroup.add(prvniRB);
        editableGroup.add(druhyRB);
        editableGroup.add(tretiRB);

        prvniRB.setSelected(true);

        boxButton.add(prvniRB, BorderLayout.SOUTH);
        boxButton.add(druhyRB, BorderLayout.SOUTH);
        boxButton.add(tretiRB, BorderLayout.SOUTH);

        // Unikátní část kódu 1.část
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Jste si jistí s ukončením programu?", "Exit Program Message Box",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WindowRSS dlg = new WindowRSS(self, "Add New RSS");
                RSSSource result = dlg.showDialog();
                //               System.out.println("name " + result.getName());
//                System.out.println("text " + result.getSource());
                if (result.isValidRSS()) {
                    sourcesItems.addElement(result);
                    FileUtils.saveSources(sourcesItems);
                    // TODO: validate if zero exists
                    comboBoxRss.setSelectedIndex(sourcesItems.size() - 1);
                }
            }
        });

        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (comboBoxRss.getSelectedIndex() == -1) {
                    return;
                }
                RSSSource item = (RSSSource) comboBoxRss.getSelectedItem();
                WindowRSS dlg = new WindowRSS(self, "Edit New RSS", item);
                RSSSource result = dlg.showDialog();
//                System.out.println("edit name " + result.getName());
//                System.out.println("edit text " + result.getSource());
                comboBoxRss.setSelectedIndex(comboBoxRss.getSelectedIndex());
                comboBoxRss.validate();
                comboBoxRss.repaint();
                FileUtils.saveSources(sourcesItems);

            }
        });

        load.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RSSSource s1 = new RSSSource("name file", "rss.xml");
                RSSSource s2 = new RSSSource("name web", "http://www.canaltrans.com/podcast/rssaudio.xml");
                List sources = new ArrayList<RSSSource>();

                sources.add(s1);
                sources.add(s2);
                FileUtils.saveSources(sources);
            }

        });


        JPanel contentPanel = new JPanel(new WrapLayout());

        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = comboBoxRss.getSelectedIndex();
                sourcesItems.remove(selected);
                if (sourcesItems.size() == 0) {

                    fillRSSData(contentPanel, comboBoxRss, getThemeSelected(new JRadioButton[]{prvniRB, druhyRB, tretiRB}));
                    comboBoxRss.setSelectedIndex(-1);
                } else {
                    comboBoxRss.setSelectedIndex(0);
                }
                FileUtils.saveSources(sourcesItems);
                // TODO: validate if zero exists

            }
        });

        comboBoxRss.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillRSSData(contentPanel, comboBoxRss, getThemeSelected(new JRadioButton[]{prvniRB, druhyRB, tretiRB}));
            }
        });
        add(controlPanel, BorderLayout.NORTH);
        fillRSSData(contentPanel, comboBoxRss, getThemeSelected(new JRadioButton[]{prvniRB, druhyRB, tretiRB}));

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        // Unikátní část kódu 2.část

        prvniRB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCardsBackground(contentPanel, 0);
            }
        });
        druhyRB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCardsBackground(contentPanel, 1);
            }
        });
        tretiRB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCardsBackground(contentPanel, 2);

            }
        });
    }
    // Unikátní část kódu 2.část

    private int getThemeSelected(JRadioButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                return i;
            }
        }
        // default theme
        return 0;
    }
    // Unikátní část kódu 2.část

    private void updateCardsBackground(JPanel contentPanel, int option) {

        for (Component component : contentPanel.getComponents()) {
            if (component.getClass().equals(CardView.class)) {
                ((CardView) component).repaintCard(option);
            }
        }
    }

    private void fillRSSData(JPanel contentPanel, JComboBox comboModel, int colorSchema) {
        if (comboModel.getItemCount() == 0) {
            contentPanel.removeAll();
            contentPanel.add(new JLabel("No Data."));
            contentPanel.validate();
            contentPanel.repaint();
            return;
        }
        try {
            rssList = new RSSParser().getParsedRSS(((RSSSource) comboModel.getSelectedItem()).getSource());

            contentPanel.removeAll();
            for (RSSItem item : rssList.getAllItem()) {
                CardView cardView = new CardView(item, colorSchema);
                cardView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (e.getClickCount() == 2) {
                                SwingUtilities.invokeLater(() -> new DetailFrame(item).setVisible(true));
                            }
                        }
                    }
                });
                contentPanel.add(cardView);
            }
            contentPanel.validate();
            contentPanel.repaint();
//            contentPanel.paint();
        } catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }
    }

    public MainFrame() {
        init();
    }
}
