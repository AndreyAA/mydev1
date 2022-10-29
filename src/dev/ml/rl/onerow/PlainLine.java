package dev.ml.rl.onerow;

import java.util.Arrays;
import java.util.Random;

public class PlainLine {
    //https://huggingface.co/blog/deep-rl-intro
    //https://ru.wikipedia.org/wiki/Q-%D0%BE%D0%B1%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5

    private final double alpha = 0.1;
    private final double gamma = 0.9;
    private final int statesCount;
    private final int actions = 2;// 0 - left and 1- right
    private final int[] actionsValue = new int[]{-1, 1};// -1 - left and +1- right
    final int[][] rewards;
    final double[][] Q;

    public PlainLine(int statesCount) {
        this.statesCount = statesCount;
        rewards = new int[statesCount][actions];// only one reward: go right from the last right position
        Q = new double[statesCount][actions];
        rewards[this.statesCount - 2][1] = 100;//win
    }


    public static void main(String[] args) {
        int statesCount = 4;
        PlainLine plainLine = new PlainLine(statesCount);
        plainLine.calculateQ();
        plainLine.printQ();
    }

    private void printQ() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statesCount; i++) {
            sb.append(Arrays.toString(Q[i])).append(";");
        }
        System.out.println(sb.toString());
    }


    void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) { // Train cycles

            // Select random initial state
            int crtState = rand.nextInt(statesCount);
            while (!isFinalState(crtState)) {
                int[] currentStateActions = possibleActionsFromState(crtState);
                // Pick a random action from the ones possible
                int index = rand.nextInt(currentStateActions.length);
                int curAction = currentStateActions[index];
                // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * MaxQ(next state, all actions) - Q(state,action))
                double q = Q[crtState][curAction];
                int nextState = calcNextState(crtState, curAction);
                double maxQ = maxQ(nextState);
                int r = rewards[crtState][curAction];

                double value = q + alpha * (r + gamma * maxQ - q);
                Q[crtState][curAction] = value;
                crtState = nextState;
            }
        }
    }

    private int calcNextState(int crtState, int curAction) {
        return crtState + actionsValue[curAction];
    }

    private double maxQ(int nextState) {
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < actions; i++) {
            if (max < Q[nextState][i]) {
                max = Q[nextState][i];
            }
        }
        return max;
    }

    private int[] possibleActionsFromState(int crtState) {
        if (crtState == 0) return new int[]{1};//it's left position
        return new int[]{0, 1};
    }

    private boolean isFinalState(int crtState) {
        return crtState == (statesCount - 1);
    }


}
