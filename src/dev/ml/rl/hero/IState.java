package dev.ml.rl.hero;

public interface IState {

    String[] actions();
    double doAction(int actionId);

    boolean isWin();
    boolean isFail();

    IStateKey stateKey();
}
