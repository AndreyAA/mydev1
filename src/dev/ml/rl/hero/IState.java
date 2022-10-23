package dev.ml.rl.hero;

public interface IState {

    IState copy();
    String[] actions();
    void doAction(int actionId);

    boolean isWin();
    boolean isFail();

    IStateKey stateKey();
}
