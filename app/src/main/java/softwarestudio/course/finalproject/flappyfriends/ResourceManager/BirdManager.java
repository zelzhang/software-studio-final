package softwarestudio.course.finalproject.flappyfriends.ResourceManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;
import java.util.List;

import softwarestudio.course.finalproject.flappyfriends.Creature.Bird;
import softwarestudio.course.finalproject.flappyfriends.Creature.Command;
import softwarestudio.course.finalproject.flappyfriends.GameActivity;
import softwarestudio.course.finalproject.flappyfriends.Receiver.ReceiveDataStorage;
import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/21.
 * Data interact interface between game activity and static storage of receiver
 * Manage and Synchronize data of sprite from raw data
 */
public class BirdManager {

    private final static String LOG_TAG = BirdManager.class.getSimpleName();

    // parameter used by game host
    private static List<Command> commands;

    public boolean checkBirdCollision(PipeManager pipeManager, int label) {
        if (pipeManager == null) return false;
        if (label > ReceiveDataStorage.getPlayerNum()) return false;
        BirdSprite birdSprite = birdSprites.get(label);
        boolean check = pipeManager.isCollided(birdSprite.getAnimatedSprite())
                || birdSprite.outofLowerBound();
        if (check) birdSprite.StopAnimation();
        return check;
    }

    private static class BirdSprite{

        Bird bird;

        AnimatedSprite animatedSprite;

        public BirdSprite(
                Bird bird,
                AnimatedSprite animatedSprite
        ) throws IllegalArgumentException{
            if (animatedSprite == null)
                throw new IllegalArgumentException("Null Sprite");
            this.bird = bird;
            this.animatedSprite = animatedSprite;
            setBirdSpritePosition();
        }

        public void modifyBird(Bird bird) {
            if (bird == null) return;
            if (this.bird == null)
                this.bird = bird;
            else
                this.bird.ReplaceData(bird);
            setBirdSpritePosition();
        }

        public void StopAnimation() {
            if (animatedSprite.isAnimationRunning())
                animatedSprite.stopAnimation();
        }

        public void AnimationOn() {
            if (!animatedSprite.isAnimationRunning())
                animatedSprite.animate(30);
        }

        public void setBirdSpritePosition() {
            if (animatedSprite == null || bird == null)
                return;
            animatedSprite.setX(bird.getX());
            animatedSprite.setY(bird.getY());
            animatedSprite.setRotation(bird.getAngle());
        }

        public void moveoutofRightBound() {
            bird.setX(
                    GameActivity.getCameraWidth() + Bird.getBirdWith() * 2);
            setBirdSpritePosition();
        }

        public void BirdJump() {
            bird.BirdJump();
            setBirdSpritePosition();
        }

        public void move() {
            bird.move();
            setBirdSpritePosition();
        }

        public void setX(float x) {
            bird.setX(x);
            setBirdSpritePosition();
        }

        public void setY(float y) {
            bird.setY(y);
            setBirdSpritePosition();
        }

        public void setAngle(float angle) {
            bird.setAngle(angle);
            setBirdSpritePosition();
        }

        public void setSpeed(float speed) {
            bird.setSpeed(speed);
        }

        public boolean outofVerticalBound() {
            return bird.outofVerticalBound();
        }

        public boolean outofLowerBound() {
            return bird.outofLowerBound();
        }

        public boolean outofUpperBound() {
            return bird.outofLowerBound();
        }

        public Bird getBird() { return bird; }

        public AnimatedSprite getAnimatedSprite() { return animatedSprite; }
    }

    private List<BirdSprite> birdSprites;

    /**
     * Build array list of birds. Initial new array list
     * Number of bird is determined by "playernum"
     * Animated spite returns form {@link ImageManager}
     * @param context {actitivity}
     * @param imageManager {image manager}
     * @param playernum {number of players}
     * @throws IllegalArgumentException
     */
    public BirdManager(
            SimpleBaseGameActivity context,
            ImageManager imageManager,
            int playernum
    ) throws IllegalArgumentException{
        if (context == null || imageManager == null)
            throw new IllegalArgumentException("Null Argument");
        if (playernum <= 0)
            throw new IllegalArgumentException("Illegal Player Number");

        birdSprites = new ArrayList<>();

        for (int i=0; i<playernum; i++) {
            AnimatedSprite animatedSprite =
                    imageManager.buildAnimatedBirdSprite(
                            context,
                            i
                    );
            BirdSprite birdSprite = new BirdSprite(
                    new Bird(),
                    animatedSprite
            );
            birdSprite.moveoutofRightBound();
            birdSprites.add(birdSprite);
        }
    }

    /**
     * Attach all bird sprites to scene
     * @param scene {scene}
     */
    public void AttachToScene(Scene scene) {
        if (scene == null) return;

        int size = birdSprites.size();
        for (int i=0; i<size; i++) {
            scene.attachChild(birdSprites.get(i).getAnimatedSprite());
        }
    }

    public void setReadyPosition() {
        LineUpBirds(GameActivity.getCameraWidth() / 4);
        AlignOnY(GameActivity.getCameraHeight() / 2);
        if (ReceiveDataStorage.getConnection())
            FeedBackBirdData();
    }

    public void setAtVerticalMiddle() {
        LineUpBirds(GameActivity.getCameraWidth() / 2);
        AlignOnY(GameActivity.getCameraHeight() / 2);
        if (ReceiveDataStorage.getConnection())
            FeedBackBirdData();
    }

    private void LineUpBirds(float start) {
        if (birdSprites == null) return;
        int size = birdSprites.size();
        for (int i=0; i<size; i++) {
            BirdSprite cur = birdSprites.get(i);
            cur.setX(start);
            cur.setAngle(0);
            cur.setSpeed(0);
            start -= (Bird.getBirdWith()+10);
        }
    }

    private void AlignOnY(float y) {
        if (birdSprites == null) return;
        int size = birdSprites.size();
        for (int i=0; i<size; i++) {
            BirdSprite cur = birdSprites.get(i);
            cur.setY(y);
            cur.setAngle(0);
            cur.setSpeed(0);
        }
    }

    public boolean checkSelfBirdCollision(PipeManager pipeManager) {
        if (pipeManager == null)
            return false;

        BirdSprite me = birdSprites.get(ReceiveDataStorage.getPlayerLabel());
        boolean check = pipeManager.isCollided(me.getAnimatedSprite())
                || me.outofLowerBound();
        if (check) me.StopAnimation();
        return check;
    }
    public void setBirdReady(){
        BirdSprite me = birdSprites.get(ReceiveDataStorage.getPlayerLabel());
        me.setX(GameActivity.getCameraWidth()/4);
        me.setY(GameActivity.getCameraHeight()/2);
    }

    public boolean checkSelfBirdPassPipePair(PipeManager pipeManager) {
        if (pipeManager == null) return false;

        BirdSprite me = birdSprites.get(ReceiveDataStorage.getPlayerLabel());
        return pipeManager.isPassed(me.getAnimatedSprite());
    }

    public void SendCommand() {
        Bird me = birdSprites.get(ReceiveDataStorage.getPlayerLabel()).getBird();
        ReceiveDataStorage.addCommandToCommandQueue(
                new Command(
                        ReceiveDataStorage.getPlayerLabel(),
                        me.getY(),
                        me.getAngle()
                )
        );
        ExecuteCommands();
    }

    private void FetchBirdData() {
        List<Bird> newdata = ReceiveDataStorage.getBirds();
        if (newdata == null || newdata.size() == 0)
            return;
        int originsize = birdSprites.size();
        int newsize = newdata.size();
        int size = Math.min(originsize, newsize);

        for (int i=0; i<size; i++) {
            BirdSprite cur = birdSprites.get(i);
            cur.modifyBird(newdata.get(i));
        }
    }

    private void FeedBackBirdData() {
        List<Bird> data = new ArrayList<>();
        int size = birdSprites.size();
        for (int i=0; i<size; i++) {
            data.add(i, birdSprites.get(i).getBird());
        }
        ReceiveDataStorage.setBirdsData(data);
    }

    public void FetchCommand() {
        ReceiveDataStorage.FetchCommandQueueToCommandList();
        commands = ReceiveDataStorage.getCommandListCopyAndClearOrigin();
        ExecuteCommands();
        moveBirds();
        if (ReceiveDataStorage.getConnection())
            FeedBackBirdData();
    }

    private void ExecuteCommands() {
        if (commands == null) return;
        if (commands.size() <= 0)return;

        int size = commands.size();
        for (int i=0; i<size; i++) {
            Command command = commands.get(i);
            int target = commands.get(i).getCommandTarget();
            if (target < Utility.TARGET_NULL) {
                BirdSprite me = birdSprites.get(target);
                me.setY(command.getTargetY());
                me.setAngle(command.getTargetAngle());
                birdSprites.get(target).BirdJump();
            }
        }

        /*
        if (birdSprites == null
                && birdSprites.size() < ReceiveDataStorage.getPlayerLabel())
            return;
        birdSprites.get(
                ReceiveDataStorage.getPlayerLabel()
        ).BirdJump();
        */
    }


    private void moveBirds() {
        int size = birdSprites.size();
        for (int i=0; i<size; i++)
            birdSprites.get(i).move();
    }
}
