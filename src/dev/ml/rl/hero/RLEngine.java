package dev.ml.rl.hero;

import java.util.*;

public class RLEngine {
    private Random rnd = new Random();
    private final StateProvider stateProvider;
    private final int episodes;
    private final double alfa;
    private final double gamma;

    private IState currentState;
    private final Map<String, List<MutableDouble>> rewardsToState = new HashMap<>();

    private final List<StateAndAction> states = new ArrayList<>();
    private final double alfa2;
    private int wins = 0;
    private int defeats = 0;
    private int maxSteps = 0;
    private boolean debug = true;

    public RLEngine(int episodes, double alfa, StateProvider stateProvider, int maxSteps, double gamma) {
        this.stateProvider = stateProvider;
        this.episodes = episodes;
        this.alfa = alfa;
        this.alfa2 = 2*alfa;
        this.maxSteps = maxSteps;
        this.gamma = gamma;
    }

    public void start() {
        long start = System.currentTimeMillis();
        rnd = new Random();
        for (int i = 0; i < episodes; i++) {
            log("start new episode");
            currentState = stateProvider.createState();
            states.clear();

            double reward;
            int steps = 0;
            while (true) {
                steps++;
                IStateKey stateKey = currentState.stateKey();
                log(currentState + " " + stateKey);
                int currentAction = selectAction(stateKey);
                log(currentState.actions()[currentAction]);
                states.add(new StateAndAction(currentState.copy(), currentAction));

                currentState.doAction(currentAction);

                if (currentState.isWin()) {
                    log("win");
                    reward = 1000;
                    wins++;
                    break;
                } else if (currentState.isFail()) {
                    log("fail");
                    defeats++;
                    reward = -1000;
                    break;
                } else if (steps > maxSteps) {
                    log("fail");
                    reward = -200;
                    break;
                }
            }

            // propagade reward
            reward = reward / steps;
            processFinalReward(reward);// /steps
            printRewards(false);
        }

        long stop=System.currentTimeMillis();
        long delta = stop - start;
        log(episodes + " episodes took: " + delta + " ms,  " + (double)delta/ episodes + " ms/episode", true);
        log("wins:" + wins + ", defeats:" + defeats + ", " + ((double)wins*100/(wins+defeats)) +"%", true);
        printRewards(true);
    }

    private void processFinalReward(double reward) {
        //log("reward: " + currentReward + ", " + currentState);

        for (int i=states.size()-1; i>=0; i--) {
            StateAndAction state = states.get(i);
            reward=reward*gamma;
            List<MutableDouble> rewards = rewardsToState.computeIfAbsent(state.getState().stateKey().key(), __ -> {
                return createEmptyList();
            });
            rewards.get(state.getAction()).add(reward);
        }
    }

    private List<MutableDouble> createEmptyList() {
        List<MutableDouble> list = new ArrayList<>();
        for (int i = 0; i < currentState.actions().length; i++) {
            list.add(new MutableDouble(0.0));
        }
        return list;
    }

    private void printRewards(boolean debug) {
        log(currentState.stateKey().keyDescription() + " current rewards:" + Arrays.toString(currentState.actions()), debug);
        rewardsToState.forEach((key, list) -> {
            double sum = list.stream().mapToDouble(v->Math.abs(v.getValue())).sum();
            StringBuilder sb = new StringBuilder();
            sb.append(key).append(": ").append("[");
            for (MutableDouble mutableDouble : list) {
                sb.append(String.format("%.2f", mutableDouble.getValue())).append("(")
                        .append(String.format("%.2f", 100*mutableDouble.getValue() / sum)).append(")%,");
            }
            sb.append("]");
            log(sb.toString(), debug);
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
            List<MutableDouble> reward = rewardsToState.computeIfAbsent(stateKey.key(), __ -> {
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

    private static class StateAndAction {
        private final IState state;
        private final int action;

        public StateAndAction(IState state, int action) {
            this.state = state;
            this.action = action;
        }

        public IState getState() {
            return state;
        }

        public int getAction() {
            return action;
        }
    }
}
