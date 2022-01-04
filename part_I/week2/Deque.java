import java.util.Iterator;
import java.util.NoSuchElementException;

// Dequeue. A double-ended queue or deque (pronounced “deck”)
// a generalization of a stack and a queue that supports
// adding and removing items from either the front or the back of the data structure.
public class Deque<Item> implements Iterable<Item> {
    private static class Node<Item> {
        private final Item val;
        private Node<Item> prev;
        private Node<Item> next;

        public Node(Item val, Node<Item> prev, Node<Item> next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }

        public Item getVal() {
            return val;
        }

        public Node<Item> getPrev() {
            return prev;
        }

        public void setPrev(Node<Item> prev) {
            this.prev = prev;
        }

        public Node<Item> getNext() {
            return next;
        }

        public void setNext(Node<Item> next) {
            this.next = next;
        }
    }

    private Node<Item> front;
    private Node<Item> back;
    private int N;

    // construct an empty deque
    public Deque() {
        front = null;
        back = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return front == null && back == null;
    }

    // return the number of items on the deque
    public int size() {
        if (this.isEmpty()) {
            return 0;
        }
        return N;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }
        Node<Item> tmp = front;
        front = new Node<Item>(item, null, tmp);
        if (tmp != null) {
            tmp.setPrev(front);
        }
        if (back == null) {
            back = front;
        }
        N++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }
        Node<Item> last = new Node<Item>(item, back, null);
        if (back != null) {
            back.setNext(last);
        }
        back = last;
        if (front == null) {
            front = back;
        }
        N++;
    }

    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty");
        }
        Item first = front.getVal();
        if (front == back) {
            back = front.getNext();
        }
        front = front.getNext();
        if (front != null) { // delete prev reference to deleted item
            front.setPrev(null);
        }
        N--;
        return first;
    }

    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty");
        }
        Item last = back.getVal();
        if (front == back) {
            front = null;
            back = null;
        } else {
            back = back.getPrev();
            back.setNext(null);
        }
        N--;
        return last;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = front;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("DequeIterator does not support remove()");
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No next item present");
            }
            Item item = current.getVal();
            current = current.getNext();
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deq = new Deque<Integer>();

        deq.addLast(1);
        System.out.println("Empty(): " + deq.isEmpty());
        System.out.println("iterator(): " + deq.iterator());
        System.out.println("RemoveFirst(): " +  deq.removeFirst());

        System.out.println("Size(): " +  deq.size());

        for (int i = 0; i < 10; i++) {
            deq.addFirst(i);
        }
        for (Integer i : deq) { System.out.print(" " + i + ","); } System.out.println();

        for (int i = 0; i < 10; i++) {
            deq.addLast(100-i);
        }
        for (Integer i : deq) { System.out.print(" " + i + ","); } System.out.println();

        System.out.println("Size: " + deq.size());
        System.out.println("RemoveFirst(): " + deq.removeFirst());
        for (Integer i : deq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("RemoveLast(): " + deq.removeLast());
        for (Integer i : deq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("RemoveFirst(): " + deq.removeFirst());
        for (Integer i : deq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("RemoveLast(): " + deq.removeLast());
        for (Integer i : deq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("isEmpty(): " + deq.isEmpty());
    }

}
