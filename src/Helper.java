import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa pomocnicza zawierająca metody statyczne
 * wykorzystywane w całej aplikacji
 */
public class Helper {
    public static BufferedImage loadImage(String fileName) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(Helper.class.getResourceAsStream("images/" + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return img;
    }

    public static AudioInputStream loadSound(String fileName) {
        AudioInputStream audioStream = null;

        try {
            InputStream audioSrc = Helper.class.getResourceAsStream("sounds/" + fileName);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);

        } catch (UnsupportedAudioFileException | IOException ex) {
            ex.printStackTrace();
        }

        return audioStream;
    }

    public static void createDebugPiles(Game game) {
        Card card;

        // generate deck
        CardsPile debug1 = game.allPiles.get(0);
        CardsPile debug2 = game.allPiles.get(1);
        CardsPile debug3 = game.allPiles.get(2);
        CardsPile debug4 = game.allPiles.get(3);
        CardsPile debug5 = game.allPiles.get(4);
        CardsPile debug6 = game.allPiles.get(5);
        CardsPile debug7 = game.allPiles.get(6);
        CardsPile debug8 = game.allPiles.get(7);
        CardsPile debug9 = game.allPiles.get(8);
        CardsPile debug10 = game.allPiles.get(9);

        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug1.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug2.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug3.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug4.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug5.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug6.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug7.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug8.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug9.addCard(card.getUnlockedAndVisible());
        }
        for (int i = 13; i > 0; i -= 1) {
            card = (Card) game.cards.get(new CardID(1, i)).clone();
            debug10.addCard(card.getUnlockedAndVisible());
        }
    }
}
