package softwarestudio.course.finalproject.flappyfriends;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import org.andengine.engine.handler.timer.ITimerCallback;

import softwarestudio.course.finalproject.flappyfriends.Creature.PipePair;

/**
 * Created by lusa on 2016/06/18.
 */
public class Utility {

    public final static int MAX_PIPEPAIRS = 4;
    public final static float SPAWN_UPPERBOUND = 700 - PipePair.ENTRANCE_HEIGHT / 2;
    public final static float SPAWN_LOWERBOUND = 100 + PipePair.ENTRANCE_HEIGHT / 2;

    public final static int TARGET_HOST = 0;
    public final static int TARGET_NULL = Utility.MAX_PLAYERS;

    public final static int GAMESTATE_ONIDLE = 0;
    public final static int GAMESTATE_ONPREPARE = 1;
    public final static int GAMESTATE_ONOPERATE = 2;
    public final static int GAMESTATE_ONSTOP = 3;
    public final static int GAMESTATE_NULL = 4;

    public final static int MAX_PLAYERS = 2;
    public final static float FLOOR_HEIGHT = 76;
    public final static int YELLOW_BIRD_SPRITE = 0;
    public final static int BLUE_BIRD_SPRITE = 1;
    public final static int RED_BIRD_SPRITE = 2;

    public final static int MAX_SCORE = 9999;

    public static float calculateScreenWidth(Activity context, float windowHeight){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int realHeight = dm.heightPixels;
        final int realWidth = dm.widthPixels;
        float ratio = (float)realWidth / (float)realHeight;
        return windowHeight * ratio;
    }

    public static float randomSpwanPostion() {
        float spawn = (float)Math.random() * GameActivity.getCameraHeight();
        if (spawn < SPAWN_LOWERBOUND)
            spawn = SPAWN_LOWERBOUND;
        if (spawn > SPAWN_UPPERBOUND)
            spawn = SPAWN_UPPERBOUND;
        return  spawn;
    }

    public static int GetBestScore(Context context){
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName() + ".score", Context.MODE_PRIVATE);

        return prefs.getInt("bestscore", 0);
    }

    public static void SetBestScore(Context context, int newScore){
        if (context == null) return;
        if(newScore > GetBestScore(context)){

            SharedPreferences prefs = context.getSharedPreferences(
                    context.getPackageName() + ".score", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("bestscore", newScore);
            editor.commit();
        }
    }
}
