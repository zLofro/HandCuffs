package me.lofro.handcuffs.utils;

import java.util.Objects;

public class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public void set(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?>) {
            Pair<?, ?> other = (Pair<?, ?>) obj;

            return Objects.equals(first, other.first) && Objects.equals(second, other.second);
        } else {
            return false;
        }
    }

    public boolean contains(Object object) {
        return object == first || object == second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

}
