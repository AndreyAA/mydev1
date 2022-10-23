package dev.ml.rl.hero;

public interface IState {

    IState clone();
    String[] actions();
    double doAction(int actionId);

    boolean isWin();
    boolean isFail();

    IStateKey stateKey();
}
