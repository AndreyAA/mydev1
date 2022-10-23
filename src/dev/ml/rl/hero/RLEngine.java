package dev.ml.rl.hero;

import java.util.*;

public class RLEngine {
    private final Random rnd = new Random();
    private final StateProvider stateProvider;
    private final int episodes;
    private final double alfa;

    private IState currentState;
    private final Map<String, List<MutableDouble>> rewardsToState = new HashMap<>();

    private final List<IState> states = new ArrayList<>();
    private final double alfa2;
    private int wins = 0;
    private int defeats = 0;
    private boolean debug = true;

    public RLEngine(int episodes, double alfa, StateProvider stateProvider) {
        this.stateProvider = stateProvider;
        this.episodes = episodes;
        this.alfa = alfa;
        this.alfa2 = 2*alfa;
    }

    public void start() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < episodes; i++) {
            log("start new episode");
            currentState = stateProvider.createState();

            while (true) {
                IStateKey stateKey = currentState.stateKey();
                log(currentState + " " + stateKey);
                int currentAction = selectAction(stateKey);
                log(currentState.actions()[currentAction]);

                double currentReward = currentState.doAction(currentAction);

                log("reward: " + currentReward + ", " + currentState);
                List<MutableDouble> rewards = rewardsToState.computeIfAbsent(stateKey.stateKey(), __ -> {
                    return createEmptyList();
                });
                rewards.get(currentAction).add(currentReward);

                if (currentState.isWin()) {
                    log("win");
                    wins++;
                    break;
                } else if (currentState.isFail()) {
                    log("fail");
                    defeats++;
                    break;
                }
            }

            printRewards(false);

        }
    long stop=System.currentTimeMillis();
        long delta = stop - start;
        log(episodes + " episodes took: " + delta + " ms,  " + (double)delta/ episodes + " ms/episode", true);
        log("wins:" + wins + ", defeats:" + defeats + ", " + ((double)wins*100/(wins+defeats)) +"%", true);
        printRewards(true);
    }

    private List<MutableDouble> createEmptyList() {
        List<MutableDouble> list = new ArrayList<>();
        for (int i = 0; i < currentState.actions().length; i++) {
            list.add(new MutableDouble(0.0));
        }
        return list;
    }

    private void printRewards(boolean debug) {
        log("current rewards:" + Arrays.toString(currentState.actions()), debug);
        rewardsToState.forEach((key, list) -> {
            log(key + ": " + list, debug);
        });
    }

    private int selectAction(IStateKey stateKey) {
        double rndValue = rnd.nextDouble();
        log("rnd:" + rndValue);
        if ((alfa < rndValue) && (rndValue < alfa2)) {
            //random
            log("random choice");

            return rnd.nextInt(currentState.actions().length);
        } else {
            //chose the best for state
            log("best choice");
            List<MutableDouble> reward = rewardsToState.computeIfAbsent(stateKey.stateKey(), __ -> {
                return createEmptyList();
            });
            int maxPos = 0;
            MutableDouble maxReward = reward.get(0);
            for (int i = 0; i < reward.size(); i++) {
                if (maxReward.getValue() < reward.get(i).getValue()) {
                    maxReward = reward.get(i);
                    maxPos = i;
                }
            }
            return maxPos;
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void log(String message) {
        log(message, debug);
    }
    private void log(String message, boolean debugOn) {
        if (debugOn) {
            System.out.println(message);
        }
    }
}
