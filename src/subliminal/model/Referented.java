package subliminal.model;

import subliminal.view.MainAppController;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum Referented {

    SLON("sounds/slon.wav"),
    PINGVIN("sounds/pingvin.wav"),
    LEV("sounds/lev.wav"),
    JIRAF("sounds/jiraf.wav"),
    ZEBRA("sounds/zebra.wav"),
    TEST("sounds/test.wav");

    public String getName() {
        switch (this) {
            case SLON: return "Слон";
            case PINGVIN: return "Пингвин";
            case LEV: return "Лев";
            case JIRAF: return "Жираф";
            case ZEBRA: return "Зебра";
            default: return "undefined";
        }
    }

    private static final int SIZE = values().length - 1;
    private static final Random RANDOM = new Random();

    private static boolean tested = false;

    private static float volume;
    private static float upThreshold;
    private static float downThreshold;

    public String getStringVolume() {
        return String.format("%.2f", (volControl.getValue()-volControl.getMaximum())*(0 - 100)/(volControl.getMinimum()-volControl.getMaximum())+100) + "%";
    }

    private Clip clip;
    private FloatControl volControl;

    Referented(String fileName) {
        File file = new File(fileName);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(ais);
            volControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void startTest() {
        if (!this.equals(Referented.TEST)) {
            System.err.println("Метод startTest перечисления Referented применим только к тестовому звуку!");
            System.exit(-1);
        }
        for (Referented r : values()) {
            r.clip.stop();
            r.clip.setFramePosition(0);
        }
        MainAppController.stopNoise();
        upThreshold = volControl.getMaximum();
        downThreshold = volControl.getMinimum();
        volume = upThreshold;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopTest() {
        clip.stop();
    }

    public boolean isTestEnded() {
        if (!tested&&(upThreshold-downThreshold)<volControl.getPrecision()) {
            clip.stop();
            volume = downThreshold;
            tested = true;
        }
        return tested;
    }

    public boolean isTested() {
        return tested;
    }

    public void increaseVolume() {
        if (!this.equals(Referented.TEST)) {
            System.err.println("Метод increaseVolume перечисления Referented применим только к тестовому звуку!");
            System.exit(-1);
        }
        clip.stop();
        clip.setFramePosition(0);
        downThreshold = volume;
        volume = volume + (upThreshold-downThreshold)/2.0f;
        volControl.setValue(volume);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void decreaseVolume() {
        if (!this.equals(Referented.TEST)) {
            System.err.println("Метод decreaseVolume перечисления Referented применим только к тестовому звуку!");
            System.exit(-1);
        }
        clip.stop();
        clip.setFramePosition(0);
        upThreshold = volume;
        volume = volume - (upThreshold-downThreshold)/2.0f;
        volControl.setValue(volume);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static Referented enigmate() {
        for (Referented r : values()) {
            r.clip.stop();
            r.clip.setFramePosition(0);
        }
        Referented ref = values()[RANDOM.nextInt(SIZE)];
        ref.volControl.setValue(volume);
        ref.play();
        return ref;
    }
}
