package dev.ml.rl.hero;

class MutableDouble {
    private double value;

    public MutableDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void add(double toAdd) {
        value += toAdd;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
