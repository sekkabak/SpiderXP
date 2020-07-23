import org.junit.jupiter.api.Test;

import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    @Test
    void addLose() {
        Statistics statistics = new Statistics(Preferences.userRoot().node("testPrefs"));
        statistics.reset();

        statistics.addLose(2);
        assertEquals(1, statistics.getPrefValue(2, "l"));
        assertEquals(1, statistics.getPrefValue(2, "sl"));
        assertEquals(0, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));

        statistics.addLose(2);
        statistics.addLose(2);
        assertEquals(3, statistics.getPrefValue(2, "l"));
        assertEquals(3, statistics.getPrefValue(2, "sl"));
        assertEquals(0, statistics.getPrefValue(2, "s"));
        assertEquals(3, statistics.getPrefValue(2, "sa"));

        // złamanie kolejki przegranych
        statistics.addWin(2);
        assertEquals(3, statistics.getPrefValue(2, "l"));
        assertEquals(3, statistics.getPrefValue(2, "sl"));
        assertEquals(1, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));

        statistics.addLose(2);
        assertEquals(4, statistics.getPrefValue(2, "l"));
        assertEquals(3, statistics.getPrefValue(2, "sl"));
        assertEquals(0, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));
    }

    @Test
    void addWin() {
        Statistics statistics = new Statistics(Preferences.userRoot().node("testPrefs"));
        statistics.reset();

        statistics.addWin(2);
        assertEquals(1, statistics.getPrefValue(2, "w"));
        assertEquals(1, statistics.getPrefValue(2, "sw"));
        assertEquals(1, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));

        statistics.addWin(2);
        statistics.addWin(2);
        assertEquals(3, statistics.getPrefValue(2, "w"));
        assertEquals(3, statistics.getPrefValue(2, "sw"));
        assertEquals(1, statistics.getPrefValue(2, "s"));
        assertEquals(3, statistics.getPrefValue(2, "sa"));

        // złamanie kolejki wygranych
        statistics.addLose(2);
        assertEquals(3, statistics.getPrefValue(2, "w"));
        assertEquals(3, statistics.getPrefValue(2, "sw"));
        assertEquals(0, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));

        statistics.addWin(2);
        assertEquals(4, statistics.getPrefValue(2, "w"));
        assertEquals(3, statistics.getPrefValue(2, "sw"));
        assertEquals(1, statistics.getPrefValue(2, "s"));
        assertEquals(1, statistics.getPrefValue(2, "sa"));
    }

    @Test
    void submitScore() {
        Statistics statistics = new Statistics(Preferences.userRoot().node("testPrefs"));

        statistics.submitScore(2, 500);
        assertEquals(500, statistics.getPrefValue(2, "hs"));
        statistics.submitScore(2, 200);
        assertEquals(500, statistics.getPrefValue(2, "hs"));
        statistics.submitScore(2, 600);
        assertEquals(600, statistics.getPrefValue(2, "hs"));
    }
}