package dev.ml.rl.hero;

import java.util.Arrays;
import java.util.function.Consumer;

public class PalaceTowerAndHeroActivity {

    public static void main(String[] args) {
        RLEngine rlEngine = new RLEngine(1000, 0.1,
                ()->new State(100, 50, 30, 5, 5),
                60, 0.8);
        rlEngine.setDebug(false);
        rlEngine.start();
    }

    enum Action {
        ATTACK("attackBase", (state) -> {
            if (state.base == 0) {
                return;
            } else {
                state.base = state.base - state.heroDamage;
                if (state.base <= 0) {
                    return;
                } else {
                    if (state.base > 0) {
                        state.heroHealth -= state.towerDemage;
                        if (state.heroHealth <= 0) {
                            return;
                        }
                    }
                    return;
                }
            }
        }),
        ATTACK_TOWER("attackTower", (state) -> {
            if (state.tower == 0) {
                return;
            } else {
                state.tower = state.tower - state.heroDamage;
                if (state.tower <= 0) {
                    return;
                } else {
                    state.heroHealth -= state.towerDemage;
                    if (state.heroHealth <= 0) {
                        return;
                    }
                    return;
                }
            }
        }),
        HEAL("heal", (state) -> {
            if (state.heroHealth == state.maxHeroHealth) {
                return;
            }
            state.heroHealth = state.heroHealth + state.healStep;
            if (state.heroHealth > state.maxHeroHealth) {
                state.heroHealth = state.maxHeroHealth;
            }
        });
        Consumer<State> consumer;
        String name;

        Action(String name, Consumer<State> consumer) {
            this.name = name;
            this.consumer = consumer;
        }

        void process(State state) {
            consumer.accept(state);
        }
    }

    public PalaceTowerAndHeroActivity() {
        //no op
    }



    public static class State implements IState {
        private final String[] actions = createActions();
        int base;
        int tower;
        int heroHealth;
        int maxHeroHealth;
        int heroDamage;
        int towerDemage;
        int healReward = 2;
        int healStep = 2;
        private int step = 0;

        private String[] createActions() {
            return Arrays.stream(Action.values()).map(v -> v.name).toArray(String[]::new);
        }

        public State(int base, int tower, int heroHealth, int heroDamage, int towerDemage) {
            this.base = base;
            this.tower = tower;
            this.heroHealth = heroHealth;
            this.maxHeroHealth = heroHealth;
            this.heroDamage = heroDamage;
            this.towerDemage = towerDemage;
        }

        @Override
        public IState copy() {
            return new State(base, tower, heroHealth, heroDamage, towerDemage);
        }

        @Override
        public String[] actions() {
            return actions;
        }

        public void doAction(int actionId) {
            step++;
            Action.values()[actionId].process(this);
        }

        @Override
        public boolean isWin() {
            return base <= 0;
        }

        @Override
        public boolean isFail() {
            return heroHealth <= 0;
        }

        @Override
        public IStateKey stateKey() {
            return new IStateKey() {
                @Override
                public String key() {
                    return ((heroHealth < 16) ? "0" : "1") + ((base <= 0) ? "0" : "1") + ((tower <= 0) ? "0" : "1");
                }

                @Override
                public String keyDescription() {
                    return "hbt";
                }
            };
        }

        @Override
        public String toString() {
            return "State{" +
                    "base=" + base +
                    ", heroHealth=" + heroHealth +
                    '}';
        }
    }

}
