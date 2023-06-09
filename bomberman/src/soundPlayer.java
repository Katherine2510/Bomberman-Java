import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class soundPlayer {
    public static Clip clip;
    public static void play(String name){
        try{
            File f = new File(name);
            AudioInputStream stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();

        }catch (Exception e){
            System.out.println("File " + name + " Not Found");
            e.printStackTrace();
        }
    }
    public static void stop() {
        clip.stop();
        clip.close();
    }
}
