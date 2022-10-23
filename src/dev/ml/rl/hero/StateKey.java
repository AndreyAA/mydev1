package dev.ml.rl.hero;

import java.util.Objects;

public class StateKey implements IStateKey {
    private final int hero;
    private final int base;
    private final int tower;

    public StateKey(int hero, int base, int tower) {
        this.hero = hero;
        this.base = base;
        this.tower = tower;
    }

    @Override
    public String toString() {
        return "StateKey{" +
                "hero=" + hero +
                ", base=" + base +
                ", tower=" + tower +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateKey stateKey = (StateKey) o;
        return hero == stateKey.hero && base == stateKey.base && tower == stateKey.tower;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hero, base, tower);
    }

    @Override
    public String key() {
        return String.valueOf(hero) + String.valueOf(base) + String.valueOf(tower);
    }
}
