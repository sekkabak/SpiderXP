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
//        statPage.setLayout(new BoxLayout(statPage, BoxLayout.PAGE_AXIS));
        statPage.setLayout(new GridLayout(3,1));

        JPanel statSegment1 = new JPanel();
//        statSegment1.setPreferredSize(new Dimension(200, 50));
        statSegment1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "High Score"));
        JLabel highScoreLabel = new JLabel("highScoreLabel");
        statSegment1.add(highScoreLabel);

        JPanel statSegment2 = new JPanel();
        statSegment2.setLayout(new BoxLayout(statSegment2, BoxLayout.PAGE_AXIS));
//        statSegment2.setPreferredSize(new Dimension(200, 200));
        statSegment2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Percentage"));
        JLabel wins = new JLabel("Wins:     ");
        JLabel losses = new JLabel("Losses:     ");
        JLabel winRate = new JLabel("Win Rate:     ");
        statSegment2.add(wins);
        statSegment2.add(losses);
        statSegment2.add(winRate);

        JPanel statSegment3 = new JPanel();
        statSegment3.setLayout(new BoxLayout(statSegment3, BoxLayout.PAGE_AXIS));
//        statSegment3.setPreferredSize(new Dimension(200, 200));
        statSegment3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Streaks"));
        JLabel mostWins = new JLabel("Most Wins:     ");
        JLabel mostLosses = new JLabel("Most Losses:     ");
        JLabel current = new JLabel("Current:     ");
        statSegment3.add(mostWins);
        statSegment3.add(mostLosses);
        statSegment3.add(current);
        statPage.add(statSegment1);
        statPage.add(statSegment2);
        statPage.add(statSegment3);

        // tab dla poziomu łatwego
        JPanel easyPanel = new JPanel();
        easyPanel.setPreferredSize(new Dimension(100, 100));
        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(2, "hs")));

        int winsNumber = statistics.getPrefValue(2, "w");
        int lossesNumber = statistics.getPrefValue(2, "l");
        wins.setText("Wins:     " + winsNumber);
        losses.setText("Losses:     " + lossesNumber);
        try {
            winRate.setText("Win Rate:     " + (winsNumber / lossesNumber) + "%");
        } catch (Exception ignored) {
            winRate.setText("Win Rate:     0%");
        }

        easyPanel.add(cloneComponent(statPage));
        tabbedPane.addTab("Easy", easyPanel);

        // tab dla poziomu średniego
        JPanel mediumPanel = new JPanel();
        mediumPanel.setPreferredSize(new Dimension(100, 100));
        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(3, "hs")));
        mediumPanel.add(cloneComponent(statPage));
        tabbedPane.addTab("Medium", mediumPanel);

        // tab dla poziomu trudnego
        JPanel hardPanel = new JPanel();
        hardPanel.setPreferredSize(new Dimension(100, 100));
        highScoreLabel.setText(String.valueOf(statistics.getPrefValue(5, "hs")));
        hardPanel.add(cloneComponent(statPage));
        tabbedPane.addTab("Hard", hardPanel);

        panel.add(tabbedPane);

        Object[] options = {"OK", "Reset"};
        int result = JOptionPane.showOptionDialog(null, panel, "Statistics", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);
        if(result == JOptionPane.NO_OPTION) { // Reset
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
