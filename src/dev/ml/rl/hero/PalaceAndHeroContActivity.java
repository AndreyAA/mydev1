package dev.ml.rl.hero;

public class PalaceAndHeroContActivity {

    public static void main(String[] args) {
        RLEngine rlEngine = new RLEngine(1000, 0.05, ()->new State(10, 2, 1, 1), 50, 0.6);
        rlEngine.setDebug(false);
        rlEngine.start();
    }

    public static class State extends PalaceAndHeroActivity.State {

        public State(int base, int heroHealth, int heroDamage, int baseDemage) {
            super(base, heroHealth, heroDamage, baseDemage);
        }

        @Override
        public IStateKey stateKey() {
            return new IStateKey() {
                @Override
                public String key() {
                    return String.valueOf(heroHealth)+String.valueOf(base);
                }

            };

        }

    }

}
