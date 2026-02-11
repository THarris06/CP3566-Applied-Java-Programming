package main.java.review;

import java.util.function.Function;

public class Box<E> {
    private E e;
    public Box(E e) {
        this.e = e;
    }

    public <O> Box<O> map(Function<E, O> f) {
        return new Box<O>(f.apply(e));
    }
    public E returnValue() {
        return e;
    }
}
