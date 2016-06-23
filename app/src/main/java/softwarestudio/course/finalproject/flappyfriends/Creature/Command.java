package softwarestudio.course.finalproject.flappyfriends.Creature;

import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/21.
 */
public class Command {

    private int commandTarget = Utility.TARGET_HOST;

    private float targetY;
    private float targetAngle;

    public Command(int target, float targetY, float targetAngle) {
        setCommandTarget(target, targetY, targetAngle);
    }

    public void setCommandTarget(int commandTarget, float targetY, float targetAngle) {
        if (commandTarget < Utility.MAX_PLAYERS)
            this.commandTarget = commandTarget;
        else
            this.commandTarget = Utility.TARGET_NULL;

        this.targetY = targetY;
        this.targetAngle = targetAngle;
    }

    public int getCommandTarget() { return commandTarget; }
    public float getTargetY() { return targetY; }
    public float getTargetAngle() { return targetY; }
}
