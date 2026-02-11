package review;

import java.util.ArrayDeque;
import java.util.Queue;

public class App {
    private static class A {}
    private static class B extends A {}
    private static class C extends B {}
    private static Queue<A> queue = new ArrayDeque<>();
    public static void main(String[] args) throws Exception {
        Box<Integer> intBox = new Box<>(1);
        BoxNoGenerics otherIntBox = new BoxNoGenerics(1);
        useBoxNoGenerics(otherIntBox);
        useBox(intBox);
        queue.add(new A());
        queue.add(new B());
        queue.add(new C());
        proceesQueue(queue);
        Queue<B> otherQueue = new ArrayDeque<>();
        addToQueue(queue, new A());
        addToQueue(queue, new B());
        addToQueue(queue, new C());
        addToQueue(otherQueue, new A());
        addToQueue(otherQueue, new B());
        addToQueue(otherQueue, new C());
    }

    private static <O> void addToQueue(Queue<? super O> queue, O o) {
        queue.add(o);
    }

    private static <O> void pollFromQueue(Queue<? extends O> queue) {
        
    }

    public static void proceesQueue(Queue<A> queue) {
        while(queue.size() > 0) {
            System.out.println(queue.poll());
        }
    }

    private static void useBox(Box<Integer> intBox) {
        System.out.println(5 + intBox.returnValue());
        Box<String> stringBox = intBox.map((integer) -> {
            return intger.toString();
        });
        System.out.println("stringBox: " + stringBox.returnValue());
    }

    private static void useBoxNoGenerics(BoxNoGenerics intBox) {
        System.out.println(5 + (Integer)intBox.getObject());
        Object o = intBox.getObject();
        Integer i = (Integer)o;
        String s = i.toString();
        System.out.println("boxNogenerics: " + s);
    }
}
