package part_I.week2;

import java.util.Stack;

class StackMax<item extends Comparable<item>> {
    private final Stack<item> stack;
    private final Stack<item> max;

    public StackMax() {
        stack = new Stack<item>();
        max = new Stack<item>();
    }

    public void push(item e) {
        stack.push(e);
        if (max.isEmpty()) {
            max.push(e);
        }
        item currentMax = max.peek();
        if (e.compareTo(currentMax) > 0) {
            max.push(e);
        } else {
            max.push(currentMax);
        }
    }

    public item pop() {
        max.pop();
        return stack.pop();
    }

    public item getMax() {
        if (max.isEmpty())
            return null;
        return max.peek();
    }
}

public class QueueStack<item> {
    // a queue implementation with two stacks so that each queue operations takes a constant amortized number of stack operations.

    private final Stack<item> inbox;
    private final Stack<item> outbox;

    public QueueStack() {
        inbox = new Stack<item>();
        outbox = new Stack<item>();
    }

    public void enque(item e) {
        inbox.push(e);
    }

    public item dequeu() {
        if (outbox.isEmpty()) {
            while (!inbox.isEmpty()) {
                outbox.push(inbox.pop());
            }
        }
        return outbox.pop();
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("QueueStack");
        QueueStack<Integer> qs = new QueueStack<Integer>();

        qs.enque(1);
        qs.enque(2);
        qs.enque(3);
        System.out.println(qs.dequeu());
        qs.enque(4);
        qs.enque(69);
        qs.enque(5);
        System.out.println(qs.dequeu());
        qs.enque(6);
        System.out.println(qs.dequeu());

        System.out.println("MaxStack");
        StackMax<Integer> sm = new StackMax<Integer>();

        System.out.println("Max: " + sm.getMax());
        sm.push(35);
        System.out.println("Max: " + sm.getMax());
        sm.push(12);
        System.out.println("Max: " + sm.getMax());
        sm.push(57);
        System.out.println("Max: " + sm.getMax());
        System.out.println("Pop: " + sm.pop());
        sm.push(24);
        System.out.println("Max: " + sm.getMax());
        sm.push(37);
        System.out.println("Max: " + sm.getMax());
        sm.push(67);
        System.out.println("Max: " + sm.getMax());
        System.out.println("Pop: " + sm.pop());
        sm.push(385);
        System.out.println("Max: " + sm.getMax());
        sm.push(465);
        System.out.println("Max: " + sm.getMax());
    }
}
