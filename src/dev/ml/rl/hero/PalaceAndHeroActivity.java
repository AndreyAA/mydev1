package dev.ml.rl.hero;

import java.util.Arrays;
import java.util.function.Consumer;

public class PalaceAndHeroActivity {
    public PalaceAndHeroActivity() {
        //no op
    }

    public static void main(String[] args) {
        RLEngine rlEngine = new RLEngine(1000, 0.1,
                () -> new State(100, 30, 5, 5),
                60, 0.8);
        rlEngine.setDebug(false);
        rlEngine.start();
    }

    enum Action {
        ATTACK("attack", (state) -> {
            if (state.base == 0) {
                return;
            } else {
                state.base = state.base - state.heroDamage;
                if (state.base <= 0) {
                    return;
                } else {
                    state.heroHealth -= state.baseDemage;
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

    public static class State implements IState {
        private final String[] actions = createActions();
        int base;
        int heroHealth;
        int maxHeroHealth;
        int heroDamage;
        int baseDemage;
        double healReward = 2;
        int healStep = 2;
        private int step = 0;

        private String[] createActions() {
            return Arrays.stream(Action.values()).map(v -> v.name).toArray(String[]::new);
        }

        public State(int base, int heroHealth, int heroDamage, int baseDemage) {
            this.base = base;
            this.heroHealth = heroHealth;
            this.maxHeroHealth = heroHealth;
            this.heroDamage = heroDamage;
            this.baseDemage = baseDemage;
        }

        @Override
        public IState copy() {
            return new State(base, heroHealth, heroDamage, baseDemage);
        }

        @Override
        public String[] actions() {
            return actions;
        }

        public void doAction(int actionId) {
            step++;
            Action action = Action.values()[actionId];
            action.process(this);
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
            String b = (base <= 0) ? "0" : "1";
            String h = (heroHealth < 15) ? "0" : "1";
            return new IStateKey() {
                @Override
                public String key() {
                    return h + b;
                }

                @Override
                public String keyDescription() {
                    return "hb";
                }
            };
        }

        @Override
        public String toString() {
            return "State{" +
                    "step=" + step +
                    ", base=" + base +
                    ", heroHealth=" + heroHealth +
                    '}';
        }
    }

}
