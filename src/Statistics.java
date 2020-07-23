import java.util.prefs.Preferences;

public class Statistics {
    private final Preferences preferences;

    public Statistics(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Zwraca wartość zapisanej statystyki na podstawie jej skrótu oraz poziomu trudności<br>
     * <p>
     * w  - wygrane<br>
     * sw - streak wygranych<br>
     * l  - przegrane<br>
     * sl - streak przegranych<br>
     * hs - high score<br>
     * s  - typ kolejki(kolejka zwycieztw lub przegranych) 1-wygrana 0-przegrana <br>
     * sa - liczba wygranych z kolei
     *
     * @return wartość podanej preferencji
     */
    public int getPrefValue(int difficulty, String shortcut) {
        try {
            // jeśli preferencja to highscore to początkowa wartość to 500
            if (shortcut.equals("hs"))
                return preferences.getInt(diffToString(difficulty) + "-" + shortcut, 500);

            return preferences.getInt(diffToString(difficulty) + "-" + shortcut, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Dodaje przegraną na danym poziomie trudności
     * <p>
     * 2 - e - easy
     * 3 - m - medium
     * 5 - h - hard
     */
    public void addLose(int difficulty) {
        try {
            String diff = diffToString(difficulty);
            int actual = preferences.getInt(diff + "-l", 0);
            preferences.putInt(diff + "-l", actual + 1);

            handleStreak(0, diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodaje zwycięztwo na danym poziomie trudności
     * <p>
     * 2 - e - easy
     * 3 - m - medium
     * 5 - h - hard
     */
    public void addWin(int difficulty) {
        try {
            String diff = diffToString(difficulty);
            int actual = preferences.getInt(diff + "-w", 0);
            preferences.putInt(diff + "-w", actual + 1);

            handleStreak(1, diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kolejke wygranych oraz przegranych + zapisuje jej maksymalne wartości
     *
     * @param result int 1 - wygrana, 0 - przegrana
     * @param diff String poziom trudności
     */
    private void handleStreak(int result, String diff) {
        // jeśli wynik gry zgadza się z poprzednim
        int streakAmount = preferences.getInt(diff + "-sa", 0) + 1;
        String type = diff + (result == 1 ? "-sw" : "-sl");
        if (preferences.getInt(diff + "-s", 0) == result) {
            preferences.putInt(diff + "-sa", streakAmount);
        } else {
            // jeśli wynik gry różni się od poprzedniego to zmieniamy kolejkę
            preferences.putInt(diff + "-s", result);
            preferences.putInt(diff + "-sa", 1);
        }

        // zapisujemy wynik kolejki jeśli jest większy niż poprzedni
        if(preferences.getInt(type, 0) < streakAmount) {
            preferences.putInt(type, streakAmount);
        }
        // pierwsze wystąpienie
        else if(preferences.getInt(type, 0) == 0) {
            preferences.putInt(type, 1);
        }
    }

    /**
     * Sprawdza czy podany wynik jest największy w danej kategorii
     * jeśli tak zapisuje go
     * <p>
     * 2 - e - easy
     * 3 - m - medium
     * 5 - h - hard
     */
    public void submitScore(int difficulty, long score) {
        try {
            String diff = diffToString(difficulty);
            int actual = preferences.getInt(diff + "-hs", 500);

            if (actual < score)
                preferences.putInt(diff + "-hs", (int) score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resetuje wszystkie statystyki
     */
    public void reset() {
        String[] diffs = new String[]{"e", "m", "h"};
        for (String diff : diffs) {
            preferences.putInt(diff + "-w", 0);
            preferences.putInt(diff + "-sw", 0);
            preferences.putInt(diff + "-l", 0);
            preferences.putInt(diff + "-sl", 0);
            preferences.putInt(diff + "-hs", 0);

            // win streak - 1
            // lose streak - 0
            preferences.putInt(diff + "-s", 0);
            preferences.putInt(diff + "-sa", 0);
        }
    }

    /**
     * Zwraca skrót poziomu trudności na podstawie jego kodu
     * <p>
     * 2 - e - easy
     * 3 - m - medium
     * 5 - h - hard
     */
    private String diffToString(int difficulty) throws Exception {
        switch (difficulty) {
            case 2:
                return "e";
            case 3:
                return "m";
            case 5:
                return "h";
        }

        throw new Exception("Bad difficulty signature");
    }
}
