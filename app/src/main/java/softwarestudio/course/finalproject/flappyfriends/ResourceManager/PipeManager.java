package softwarestudio.course.finalproject.flappyfriends.ResourceManager;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;
import java.util.List;

import softwarestudio.course.finalproject.flappyfriends.Creature.Pipe;
import softwarestudio.course.finalproject.flappyfriends.Creature.PipePair;
import softwarestudio.course.finalproject.flappyfriends.GameActivity;
import softwarestudio.course.finalproject.flappyfriends.Receiver.ReceiveDataStorage;
import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/21.
 * Data interact interface between game activity and static storage of receiver
 * Manage and Synchronize data of sprite from raw data
 */
public class PipeManager {

    private final static String LOG_TAG = PipeManager.class.getSimpleName();

    // parameter used only as game host
    private int pipePairTail = 0;

    private int pipePairCounter = 0;
    private final static float PIPEPAIR_INTERVAL = 100;

    /**
     * Inner class to help managing synchronization between sprite data and raw data
     */
    private static class PipePairSprite {

        private PipePair pipePair;

        private Sprite upperSprite;
        private Sprite lowerSprite;

        private boolean passCount = false;

        public PipePairSprite(
                PipePair pipePair,
                Sprite upperSprite,
                Sprite lowerSprite
        ) throws IllegalArgumentException{
            if (upperSprite == null || lowerSprite == null)
                throw new IllegalArgumentException("Null Sprite");
            this.pipePair = pipePair;
            this.upperSprite = upperSprite;
            this.lowerSprite = lowerSprite;
            setPipePairSpritePosition();
        }

        public void modifyPipePair(PipePair newdata) {
            if (newdata == null) return;
            if (pipePair == null)
                pipePair = newdata;
            else
                pipePair.ReplaceData(newdata);
            setPipePairSpritePosition();
        }

        public void setPipePairSpritePosition() {
            setPipePairX();
            setPipePairY();
        }

        public boolean isCollided(Sprite sprite) {
            return upperSprite.collidesWith(sprite)
                    || lowerSprite.collidesWith(sprite);
        }

        public boolean isPassed(Sprite sprite) {
            if (!passCount) {
                if (sprite.getX() > getAlignX()) {
                    passCount = true;
                    return true;
                }
            }

            return false;
        }

        public boolean outofLeftScreenBound() {
            if (pipePair.getUpperPipe().getX() <= -1*Pipe.getPipeWidth()-10)
                return true;
            return false;
        }

        public boolean outofRightScreenBound() {
            if (pipePair.getUpperPipe().getX()
                    >= GameActivity.getCameraWidth() + Pipe.getPipeWidth()+10)
                return true;
            return false;
        }

        public boolean outofScreenBound() {
            return outofLeftScreenBound() || outofRightScreenBound();
        }

        public void setPipePairAlignX(float alignX) {
            pipePair.setAlignX(alignX);
            setPipePairX();
        }

        public void moveoutofRightBound() {
            setPipePairAlignX(
                    GameActivity.getCameraWidth() + 2*Pipe.getPipeWidth());
        }

        public void movetoReadyPosition() {
            passCount = false;
            setPipePairAlignX(
                    GameActivity.getCameraWidth() + Pipe.getPipeWidth());
        }

        public void setPipePairSpawnPoint(float spawnPoint) {
            pipePair.setSpawnPoint(spawnPoint);
            setPipePairY();
        }

        private void setPipePairX() {
            upperSprite.setX(pipePair.getUpperPipe().getX());
            lowerSprite.setX(pipePair.getLowerPipe().getX());
        }

        private void setPipePairY() {
            upperSprite.setY(pipePair.getUpperPipe().getY());
            lowerSprite.setY(pipePair.getLowerPipe().getY());
        }

        public Sprite getUpperSprite() {
            return upperSprite;
        }
        public Sprite getLowerSprite() {
            return lowerSprite;
        }
        public PipePair getPipePair() {
            return pipePair;
        }
        public float getAlignX() {
            return pipePair.getAlignX();
        }
    }

    private List<PipePairSprite> pairPipeSprites;

    /**
     * Build unset pipe pair initially
     * @param context {game activity}
     * @param imageManager {image manager}
     * @param pipenum {number of pipe pair}
     * @throws IllegalArgumentException
     */
    public PipeManager(
            SimpleBaseGameActivity context,
            ImageManager imageManager,
            int pipenum
    ) throws IllegalArgumentException{
        if (context == null || imageManager == null)
            throw new IllegalArgumentException("Null Argument");
        if (pipenum <= 0)
            throw new IllegalArgumentException("Illegal Player Number");

        pairPipeSprites = new ArrayList<PipePairSprite>();

        for (int i=0; i<pipenum; i++) {
            Log.d(LOG_TAG, (i+1) + "th pipe pair building");
            Sprite[] pairsprites =
                    imageManager.buildPipePairSprites(context);
            PipePairSprite newPipePairSprite =
                    new PipePairSprite(
                            new PipePair(new Pipe(), new Pipe()),
                            pairsprites[0],
                            pairsprites[1]
                    );
            newPipePairSprite.moveoutofRightBound();
            pairPipeSprites.add(
                    newPipePairSprite
            );
        }

    }

    /**
     * Attach all pair pipes to scene
     * @param scene {scene}
     */
    public void AttachToScene(Scene scene) {
        if (scene == null) return;

        if (pairPipeSprites != null) {
            int size = pairPipeSprites.size();
            for (int i=0; i<size; i++) {
                PipePairSprite cur = pairPipeSprites.get(i);
                scene.attachChild(cur.getUpperSprite());
                scene.attachChild(cur.getLowerSprite());
            }
        }
    }

    /**
     * Receive command
     * As a multi-player game participate,
     * function sends command to receiver and send to Game Host
     * As a game host not matter at multi-game or not,
     * function deals with command and pair pipe sprites
     * Commands are defined by res->value->gamestate.xml
     */
    public void receiveCommand() {
        movePipePairSprites();

        // send back data to receiver storage
        // if at multi-player game
        if (ReceiveDataStorage.getConnection())
            FeedBackPipePairData();
    }

    public void setReadyPosition() {
        if (pairPipeSprites == null || pairPipeSprites.size() == 0)
            return;
        int size = pairPipeSprites.size();
        for (int i=0; i<size; i++) {
            pairPipeSprites.get(i).moveoutofRightBound();
        }
        if (ReceiveDataStorage.getConnection())
            FeedBackPipePairData();
    }

    public boolean isCollided(Sprite sprite) {
        if (sprite == null) return false;
        if (pairPipeSprites == null) return false;
        if (pairPipeSprites.size() == 0) return false;

        boolean check = false;
        int size = pairPipeSprites.size();
        for (int i=0; i<size && !check; i++)
            check |= pairPipeSprites.get(i).isCollided(sprite);
        return check;
    }

    public boolean isPassed(Sprite sprite) {
        if (sprite == null) return false;
        if (pairPipeSprites == null) return false;
        if (pairPipeSprites.size() == 0) return false;

        boolean check = false;
        int size = pairPipeSprites.size();
        for (int i=0; i<size && !check; i++)
            check |= pairPipeSprites.get(i).isPassed(sprite);
        return check;
    }

    /**
     * As a multi-player participant,
     * function fetches from {@link softwarestudio.course.finalproject.flappyfriends.Receiver.ReceiveDataStorage}
     * As a game host
     * function deals with data directly thus there is no need of usage
     */
    private void FetchPipePairData() {
        // If as a multi-player game participant
        List<PipePair> newdata = ReceiveDataStorage.getPipePairs();
        int originsize = pairPipeSprites.size();
        int newsize = newdata.size();
        int size = Math.min(originsize, newsize);
        for (int i=0; i<size; i++) {
            pairPipeSprites.get(i).modifyPipePair(
                    newdata.get(i)
            );
        }
    }

    private void FeedBackPipePairData() {
        if (pairPipeSprites == null)
            return;
        List<PipePair> data = new ArrayList<>();
        int size = pairPipeSprites.size();
        for (int i=0; i<size; i++) {
            data.add(pairPipeSprites.get(i).getPipePair());
        }
        ReceiveDataStorage.setPipePairsData(data);
    }

    /**
     * Move all pipe pair
     * Call as a game host
     */
    private void movePipePairSprites() {
        if(pairPipeSprites == null) return;
        int num = pairPipeSprites.size();
        nextPipePairTail();
        for (int i=0; i<num; i++) {
            PipePairSprite cur = pairPipeSprites.get(i);
            if (cur!= null && !cur.outofScreenBound()) {
                // move from right to left
                cur.setPipePairAlignX(
                        cur.getAlignX() - 4.f
                );
            } else if (cur != null && cur.outofLeftScreenBound()) {
                // reset position out of right bound
                cur.moveoutofRightBound();
            }
        }
    }

    /**
     * Set next pipe pair to get ready
     * Call as a game host
     */
    private void nextPipePairTail() {
        pipePairCounter++;
        if (pipePairCounter > PIPEPAIR_INTERVAL) {
            pipePairCounter = 0;
            if (pipePairTail >= 0
                    && pipePairTail < pairPipeSprites.size()) {
                PipePairSprite cur = pairPipeSprites.get(pipePairTail);
                if (cur.outofRightScreenBound()) {
                    float spawnpoint = 0;
                    if (ReceiveDataStorage.getConnection())
                        spawnpoint = ReceiveDataStorage.getSpawnPointFromQueue();
                    else
                        spawnpoint = Utility.randomSpwanPostion();
                    cur.setPipePairSpawnPoint(spawnpoint);
                    cur.movetoReadyPosition();
                    pipePairTail++;
                    pipePairTail %= pairPipeSprites.size();
                }
            }
        }
    }
}
