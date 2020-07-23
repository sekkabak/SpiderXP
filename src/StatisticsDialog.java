import javax.swing.*;
import java.awt.*;
import java.io.*;

public class StatisticsDialog {
    Game game;
    Statistics statistics;

    public StatisticsDialog(Game game) {
        this.game = game;
        statistics = game.statistics;
    }

    public void showStatistics() {
        final JPanel panel = new JPanel();

        // tworzenie panelu z zakładkami
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(200, 300));

        // tworzenie panelu z danymi do późniejszego kopiowania
        JPanel statPage = new JPanel();
        statPage.setLayout(new GridLayout(3, 1));

        // segment 1
        JPanel statSegment1 = new JPanel();
        statSegment1.setLayout(new GridLayout(1, 2));
        statSegment1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "High Score"));
        JLabel highScoreLabel = new JLabel();
        statSegment1.add(new JLabel());
        statSegment1.add(highScoreLabel);

        // segment 2
        JPanel statSegment2 = new JPanel();
        statSegment2.setLayout(new GridLayout(3, 2));
        statSegment2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Percentage"));

        statSegment2.add(new JLabel("Wins:    "));
        JLabel wins = new JLabel();
        statSegment2.add(wins);

        statSegment2.add(new JLabel("Losses:    "));
        JLabel losses = new JLabel();
        statSegment2.add(losses);

        statSegment2.add(new JLabel("Win Rate:    "));
        JLabel winRate = new JLabel();
        statSegment2.add(winRate);


        // segment 3
        JPanel statSegment3 = new JPanel();
        statSegment3.setLayout(new GridLayout(3, 2));
        statSegment3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Streaks"));

        statSegment3.add(new JLabel("Most Wins:    "));
        JLabel mostWins = new JLabel();
        statSegment3.add(mostWins);

        statSegment3.add(new JLabel("Most Losses:    "));
        JLabel mostLosses = new JLabel();
        statSegment3.add(mostLosses);

        statSegment3.add(new JLabel("Current:    "));
        JLabel current = new JLabel();
        statSegment3.add(current);

        // złaczenie segmentów
        statPage.add(statSegment1);
        statPage.add(statSegment2);
        statPage.add(statSegment3);

        // wypełnienie wszystkich tabów
        JPanel[] panels = {new JPanel(), new JPanel(), new JPanel()};
        String[] tabNames = {"Easy", "Medium", "Hard"};
        int[] difficulties = {2, 3, 5};
        for (int i = 0; i < 3; i++) {
            // segment 1
            highScoreLabel.setText(String.valueOf(statistics.getPrefValue(difficulties[i], "hs")));

            // segment 2
            int winsNumber = statistics.getPrefValue(difficulties[i], "w");
            int lossesNumber = statistics.getPrefValue(difficulties[i], "l");
            wins.setText(String.valueOf(winsNumber));
            losses.setText(String.valueOf(lossesNumber));
            try {
                winRate.setText((winsNumber / (lossesNumber + winsNumber)) * 100 + "%");
            } catch (Exception ignored) {
                winRate.setText("0%");
            }

            // segment 3
            mostWins.setText(String.valueOf(statistics.getPrefValue(difficulties[i], "sw")));
            mostLosses.setText(String.valueOf(statistics.getPrefValue(difficulties[i], "sl")));
            String type = statistics.getPrefValue(difficulties[i], "s") == 1 ? " Wins" : " Losses";
            current.setText(statistics.getPrefValue(difficulties[i], "sa") + type);

            // ustawienie wymiarów i dodanie segmentów
            panels[i].setPreferredSize(new Dimension(100, 100));
            panels[i].add(cloneComponent(statPage));
            tabbedPane.addTab(tabNames[i], panels[i]);
        }

        // tab dla poziomu łatwego
//        JPanel easyPanel = new JPanel();
//        easyPanel.setPreferredSize(new Dimension(100, 100));
//        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(2, "hs")));

//        int winsNumber = statistics.getPrefValue(2, "w");
//        int lossesNumber = statistics.getPrefValue(2, "l");
//        wins.setText(String.valueOf(winsNumber));
//        losses.setText(String.valueOf(lossesNumber));
//        try {
//            winRate.setText((winsNumber / lossesNumber) + "%");
//        } catch (Exception ignored) {
//            winRate.setText("0%");
//        }

//        mostWins.setText(String.valueOf(statistics.getPrefValue(2, "sw")));
//        mostLosses.setText(String.valueOf(statistics.getPrefValue(2, "sl")));
//        String type = statistics.getPrefValue(2, "s") == 1 ? " Wins" : " Losses";
//        current.setText(statistics.getPrefValue(2, "sa") + type);

//        easyPanel.add(cloneComponent(statPage));
//        tabbedPane.addTab("Easy", easyPanel);

        // tab dla poziomu średniego
//        JPanel mediumPanel = new JPanel();
//        mediumPanel.setPreferredSize(new Dimension(100, 100));
//        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(3, "hs")));
//        mediumPanel.add(cloneComponent(statPage));
//        tabbedPane.addTab("Medium", mediumPanel);
//
//        // tab dla poziomu trudnego
//        JPanel hardPanel = new JPanel();
//        hardPanel.setPreferredSize(new Dimension(100, 100));
//        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(5, "hs")));
//        hardPanel.add(cloneComponent(statPage));
//        tabbedPane.addTab("Hard", hardPanel);

        panel.add(tabbedPane);

        Object[] options = {"OK", "Reset"};
        int result = JOptionPane.showOptionDialog(null, panel, "Statistics", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);
        if (result == JOptionPane.NO_OPTION) { // Reset
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset all game statistics?", "Reset",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            statistics.reset();
        }
    }

    private Component cloneComponent(Component component) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out;
            out = new ObjectOutputStream(bos);
            out.writeObject(component);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (Component) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
