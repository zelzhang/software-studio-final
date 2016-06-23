package softwarestudio.course.finalproject.flappyfriends.ResourceManager;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

/**
 * Created by lusa on 2016/06/21.
 */
public class AudioManager {

    private final static String AUDIO_PATH = "audio/";

    private final static String JUMP_AUDIO_NAME = "jump.ogg";
    private final static String SCOREUP_AUDIO_NAME = "scoreup.ogg";
    private final static String GAMEOVER_AUDIO_NAME = "gameover.ogg";
    private final static String BGM_AUDIO_NAME = "bgm.ogg";

    public final static int AUDIOLABEL_JUMP = 1;
    public final static int AUDIOLABEL_SCOREUP = 2;
    public final static int AUDIOLABEL_GAMEOVER = 3;
    public final static int AUDIOLABEL_BGM = 0;
    public final static int AUDIOLABEL_NULL = 4;

    private Sound jump;
    private Sound scoreup;
    private Sound gameover;

    private Music backgroundMusic;

    public AudioManager(SimpleBaseGameActivity context)
        throws NullPointerException{
        if (context == null)
            throw new NullPointerException("Null Context");
        SoundFactory.setAssetBasePath(AUDIO_PATH);
        MusicFactory.setAssetBasePath(AUDIO_PATH);
        BuildAudioResource(context);
    }

    public void playAudio(int label) {
        if (label >= AUDIOLABEL_NULL)
            return;
        switch (label) {
            case AUDIOLABEL_BGM:
                if (!backgroundMusic.isPlaying())
                    backgroundMusic.play();
                break;
            case AUDIOLABEL_JUMP:
                jump.play();
                break;
            case AUDIOLABEL_SCOREUP:
                scoreup.play();
                break;
            case AUDIOLABEL_GAMEOVER:
                gameover.play();
                break;
            default: break;
        }
    }

    public void pauseAudio(int label) {
        if (label >= AUDIOLABEL_NULL)
            return;
        switch (label) {
            case AUDIOLABEL_BGM:
                if (backgroundMusic.isPlaying()) {
                    backgroundMusic.pause();
                    backgroundMusic.seekTo(0);
                }
                break;
            default: break;
        }
    }

    private void BuildAudioResource(SimpleBaseGameActivity context) {
        jump = BuildSound(context, JUMP_AUDIO_NAME);
        scoreup = BuildSound(context, SCOREUP_AUDIO_NAME);
        gameover = BuildSound(context, GAMEOVER_AUDIO_NAME);

        backgroundMusic = BuildMusic(context, BGM_AUDIO_NAME);
        backgroundMusic.setLooping(true);
        //backgroundMusic.setVolume(0.1f);
    }

    private Sound BuildSound(
            SimpleBaseGameActivity context,
            String audioName
    ) {
        Sound sound = null;
        try {
            sound = SoundFactory.createSoundFromAsset(
                    context.getSoundManager(),
                    context,
                    audioName
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sound;
    }

    private Music BuildMusic(
            SimpleBaseGameActivity context,
            String audioName
    ) {
        Music music = null;
        try {
            music = MusicFactory.createMusicFromAsset(
                    context.getMusicManager(),
                    context,
                    audioName
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return music;
    }
}
