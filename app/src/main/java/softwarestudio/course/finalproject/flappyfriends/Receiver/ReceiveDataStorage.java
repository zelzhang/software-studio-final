package softwarestudio.course.finalproject.flappyfriends.Receiver;

import android.content.Context;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

import softwarestudio.course.finalproject.flappyfriends.Creature.Bird;
import softwarestudio.course.finalproject.flappyfriends.Creature.Command;
import softwarestudio.course.finalproject.flappyfriends.Creature.PipePair;
import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/20.
 * :: Data path ::
 * As a game host(if at multi-player game)--
 *            addCommandToCommandQueue(...)             FetchCommandQueueToCommandList()
 * BirdManger==============================>DataStorage=================================>DataStorage
 *          addCommandToCommandList(...)             getCommandListCopyAndClearOrigin()
 * Receiver=============================>DataStorage===================================>BirdManager
 *             setBirdsData(...)             getBirds()
 * BirdManager==================>DataStorage===========>Receiver
 *             setPipePairsData(...)             getPipePairs()
 * PipeManager======================>DataStorage===============>Receiver
 *
 * As a multi-player participant--
 *            addCommandToCommandQueue(...)             getCommandFromCommandQueue()
 * BirdManger==============================>DataStorage=============================>Receiver
 *          setPipePairData(...)             getBirds()
 * Receiver=====================>DataStorage===========>BirdManager
 *          setPipePairsData(...)             getPipePairs()
 * Receiver======================>DataStorage===============>PipeManager
 *
 * :: Caution ::
 * Command List is assessed only by "GAME HOST"
 * Command Queue can be assessed "EITHER"
 * Set the static parameter "PLAYER_LABER" for recognition as a host or participant
 * (0:host >0:participant defined in Utility)
 * Set the static parameter "IS_CONNECTED" for recognition of wifi connection
 * (true:connected false:disconnected)
 */
public class ReceiveDataStorage {

    public static int PLAYER_LABEL = 0;
    public static int PLAYER_NUM = 1;
    public static boolean IS_CONNECTED = false;
    public static boolean GAME_ACTIVE = false;
    public static int GAME_STATE = Utility.GAMESTATE_ONIDLE;
    public static final int MAX_PIPE_NUM = 40;
    public static boolean needPipes = true;
    public static boolean partnerDead = false;
    public static int MYSCORE = 0;
    public static float[] defaultPipes = new float[MAX_PIPE_NUM];
    public static int bestScore = 0;
    private static List<PipePair> pipePairs = new ArrayList<>();

    private static List<Bird> birds = new ArrayList<>();

    public static List<Command> commands = new ArrayList<>();
    private static Queue<Command> commandQueue = new ArrayDeque<>();

    public static Queue<Float> spawnPointQueue = new ArrayDeque<>();

    public static void refreshSpawn(){
        Log.d("Rec", "refresh");
        spawnPointQueue.clear();
        for(int i=0; i<MAX_PIPE_NUM; i++){
            spawnPointQueue.add(defaultPipes[i]);
        }
        for(float f:spawnPointQueue){
            Log.d("Rec", "refresh = "+f);
        }
    }
    public static void setPlayerLabel(int label) {
        if (label < Utility.TARGET_NULL)
            PLAYER_LABEL = label;
    }

    public static void setPlayerNum(int num) {
        if (num > Utility.MAX_PLAYERS)
            PLAYER_NUM = Utility.MAX_PLAYERS;
        PLAYER_NUM = num;
    }

    public static void setGameActivation(boolean isActive) {
        GAME_ACTIVE = isActive;
    }

    public static void setConnection(boolean connection) {
        IS_CONNECTED = connection;
    }

    public static void setGameState(int gameState) {
        if (gameState >= Utility.GAMESTATE_NULL)
            return;
        GAME_STATE = gameState;
    }

    public static void MyscoreIncremnet() {
        if (MYSCORE <= Utility.MAX_SCORE)
            MYSCORE++;
    }

    public static void setMyscoreZero() {
        MYSCORE = 0;
    }

    public static void UpdateMyscoreToSharedPerf(Context context) {
        Utility.SetBestScore(context, MYSCORE);
    }

    /**
     * * Input new bird list
     * If the size of new data does not fetch to original list
     * data in original list clears and takes in elements in new data
     * As a game host,
     * function called by {@link softwarestudio.course.finalproject.flappyfriends.ResourceManager.PipeManager}
     * As a mulit-player pairticipant,
     * function called by Receiver
     * @param newdata
     */
    public static void setPipePairsData(List<PipePair> newdata) {
        if (newdata == null) return;
        int originsize = pipePairs.size();
        int newsize = newdata.size();

        if (newsize == 0) return;

        if (originsize == newsize) {
            for (int i=0; i<originsize; i++) {
                pipePairs.get(i).ReplaceData(
                        newdata.get(i)
                );
            }
        } else {
            pipePairs.clear();
            int size = newsize > Utility.MAX_PIPEPAIRS ?
                    Utility.MAX_PIPEPAIRS : newsize;
            for (int i=0; i<size; i++) {
                pipePairs.add(newdata.get(i));
            }
        }
    }

    public static void addSpawnPointsToQueue(float[] spawnPoints) {
        if (spawnPoints == null) return;
        if (spawnPoints.length == 0) return;

        int size = spawnPoints.length;
        for (int i=0; i<size; i++) {
            spawnPointQueue.add(spawnPoints[i]);
        }
    }

    public static void addSpawnPointsToQueue(float spawnPoint) {

        spawnPointQueue.add(spawnPoint);
    }

    /**
     * Input new bird list
     * If the size of new data does not fetch to original list
     * data in original list clears and takes in elements in new data
     * As a game host,
     * function called by {@link softwarestudio.course.finalproject.flappyfriends.ResourceManager.PipeManager}
     * As a mulit-player pairticipant,
     * function called by Receiver
     * @param newdata
     */
    public static void setBirdsData(List<Bird> newdata) {
        if (newdata == null) return;
        int originsize = birds.size();
        int newsize = newdata.size();

        if (newsize == 0) return;

        if (originsize == newsize) {
            for (int i=0; i<originsize; i++) {
                birds.get(i).ReplaceData(
                        newdata.get(i)
                );
            }
        } else {
            birds.clear();
            int size = newsize > Utility.MAX_PLAYERS ?
                    Utility.MAX_PLAYERS : newsize;
            for (int i=0; i<size; i++) {
                birds.add(newdata.get(i));
            }
        }
    }

    /**
     * Add new command as queue
     * Function can be called either game host or participant
     * @param command
     */
    public static void addCommandToCommandQueue(Command command) {
        if (command == null) return;
        commandQueue.add(command);
    }

    public static void addCommandToCommandList(Command command) {
        if (command == null) return;
        commands.add(command);
    }

    /**
     * Input new command list
     * If the size of new data does not fetch to original list
     * data in original list clears and takes in elements in new data
     * New data will be discarded as command target points over TARGET_NULL{@link Utility}
     * Called only by game host in Reciever
     * @param newdata
     */
    /*
    public static void setCommandsData(List<Command> newdata) {
        if (newdata == null) return;
        int originsize = commands.size();
        int newsize = newdata.size();

        if (newsize == 0) return;

        if (originsize == newsize) {
            for (int i=0; i<originsize; i++) {
                int target = newdata.get(i).getCommandTarget();
                if (target < Utility.TARGET_NULL)
                    commands.get(i).setCommandTarget(target);
            }
        } else {
            commands.clear();
            for (int i=0; i<newsize; i++) {
                int target = newdata.get(i).getCommandTarget();
                if (target < Utility.TARGET_NULL)
                    commands.add(newdata.get(i));
            }
        }
    }
    */

    /**
     * Write command in queue back to list
     * Call only as one-player mode
     */
    public static void FetchCommandQueueToCommandList() {
        if (commandQueue == null || commandQueue.size() == 0)
            return;
        int size = commandQueue.size();
        for (int i=0; i<size; i++) {
            commands.add(commandQueue.poll());
        }
    }

    public static int getPlayerLabel() { return PLAYER_LABEL; }

    public static int getPlayerNum() { return PLAYER_NUM; }

    public static boolean getConnection() { return IS_CONNECTED; }

    public static boolean getGameActivation() { return GAME_ACTIVE; }

    public static int getGameState() { return GAME_STATE; }

    public static int getMyscore() { return MYSCORE; }

    /**
     * Assessed by {@link softwarestudio.course.finalproject.flappyfriends.ResourceManager.BirdManager}
     * as a multi-player game participant
     * Or assessed by Receriver
     * as a game host
     * @return list of pair pipes
     */
    public static List<PipePair> getPipePairs() {
        return pipePairs;
    }

    public static float getSpawnPointFromQueue() { return spawnPointQueue.poll(); }

    /**
     * Assessed by {@link softwarestudio.course.finalproject.flappyfriends.ResourceManager.BirdManager}
     * as a multi-player game participant
     * Or assessed by Receriver
     * as a game host
     * @return list of birds
     */
    public static List<Bird> getBirds() {
        return birds;
    }

    /**
     *  Return the copy of command list and clear original one
     *  Call only by game host
     * @return copy of command list
     */
    public static List<Command> getCommandListCopyAndClearOrigin() {
        List<Command> copy = new ArrayList<>();
        int size = commands.size();
        for (int i=0; i<size; i++) {
            copy.add(commands.get(i));
        }
        commands.clear();
        return copy;
    }

    /**
     * Take first command in command queue and pop it
     * * Function can be called either game host or participant
     * @return Command
     */
    public static Command getCommandFromCommandQueue() {
        return commandQueue.poll();
    }
}
