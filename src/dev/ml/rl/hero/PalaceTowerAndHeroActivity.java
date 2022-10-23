package dev.ml.rl.hero;

public class PalaceTowerAndHeroActivity {

    public PalaceTowerAndHeroActivity() {
        //no op
    }

    public static void main(String[] args) {
        RLEngine rlEngine = new RLEngine(1000, 0.01, ()->new State(100, 50, 30, 5, 5));
        rlEngine.setDebug(false);
        rlEngine.start();
    }


    public static class State implements IState {
        private static final double BASE_DESTROY_REWARD = 10000;
        private static final double TOWER_DESTROY_REWARD = 5000;
        private static final double BASE_DEMAGE_REWARD = 10;
        private static final double TOWER_DEMAGE_REWARD = 10;
        private static final double DEATH_REWARD = -10000;
        int base;
        int tower;
        int heroHealth;
        int maxHeroHealth;
        int heroDamage;
        int towerDemage;
        int healReward = 2;
        int healStep = 2;
        private int step = 0;

        public State() {

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
        public IState clone() {
            return new State(base, tower, heroHealth, heroDamage, towerDemage);
        }

        @Override
        public String[] actions() {
            return new String[]{"heal", "attack base", "attack tower"};
        }

        public double doAction(int actionId) {
            step++;
            if (actionId == 0) {
                return healHero();
            } else if (actionId == 1) {
                return attackBase();
            } else if (actionId == 2) {
                return attackTower();
            } else {
                throw new IllegalArgumentException(String.valueOf(actionId));
            }
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
            return () -> (((heroHealth < 16) ? "0" : "1") + ((base <= 0) ? "0" : "1") + ((tower <= 0) ? "0" : "1"));
        }

        private double attackTower() {
            if (tower == 0) {
                return 0;
            } else {
                tower = tower - heroDamage;
                if (tower <= 0) {
                    return TOWER_DESTROY_REWARD;
                } else {
                    heroHealth -= towerDemage;
                    if (heroHealth <= 0) {
                        return DEATH_REWARD;
                    }
                    return TOWER_DEMAGE_REWARD;
                }
            }
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
                    if (base > 0) {
                        heroHealth -= towerDemage;
                        if (heroHealth <= 0) {
                            return DEATH_REWARD;
                        }
                    }
                    return BASE_DEMAGE_REWARD;
                }
            }
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
