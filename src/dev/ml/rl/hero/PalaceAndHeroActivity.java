package dev.ml.rl.hero;

public class PalaceAndHeroActivity {
    public PalaceAndHeroActivity() {
        //no op
    }

    public static void main(String[] args) {
        RLEngine rlEngine = new RLEngine(100, 0.05, ()->new State(100, 30, 5, 5));
        rlEngine.setDebug(false);
        rlEngine.start();
    }

    public static class State implements IState {
        private static final double BASE_DESTROY_REWARD = 10000;
        private static final double BASE_DEMAGE_REWARD = 10;
        private static final double DEATH_REWARD = -10000;
        int base;
        int heroHealth;
        int maxHeroHealth;
        int heroDamage;
        int baseDemage;
        int healReward = 2;
        int healStep = 2;
        private int step = 0;

        public State(int base, int heroHealth, int heroDamage, int baseDemage) {
            this.base = base;
            this.heroHealth = heroHealth;
            this.maxHeroHealth = heroHealth;
            this.heroDamage = heroDamage;
            this.baseDemage = baseDemage;
        }

        @Override
        public String[] actions() {
            return new String[]{"heal", "attack"};
        }

        public double doAction(int actionId) {
            step++;
            if (actionId == 0) {
                return healHero();
            } else if (actionId == 1) {
                return attackBase();
            } else {
                throw new IllegalArgumentException(String.valueOf(actionId));
            }
        }

        @Override
        public boolean isWin() {
            return base<=0;
        }

        @Override
        public boolean isFail() {
            return heroHealth<=0;
        }

        @Override
        public IStateKey stateKey() {
            String b=(base<=0)?"0":"1";
            String h=(heroHealth<15)?"0":"1";
            return new IStateKey() {
                @Override
                public String stateKey() {
                    return h+b;
                }

            };
        }


        private int healHero() {
            if (heroHealth == maxHeroHealth) {
                return 0;
            }
            heroHealth = heroHealth + healStep;
            if (heroHealth > maxHeroHealth) {
                heroHealth = maxHeroHealth;
            }
            return healReward;
        }

        public double attackBase() {
            if (base == 0) {
                return 0;
            } else {
                base = base - heroDamage;
                if (base <= 0) {
                    return BASE_DESTROY_REWARD;
                } else {
                    heroHealth -= baseDemage;
                    if (heroHealth <= 0) {
                        return DEATH_REWARD;
                    }
                    return BASE_DEMAGE_REWARD;
                }
            }
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
