package lab6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MonolithicLockNode {
    Object value;
    MonolithicLockNode next;

    public MonolithicLockNode(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
        this.next = null;
    }
}

public class MonolithicLockLinkedList {
    private MonolithicLockNode head;
    private final Lock lock;

    public MonolithicLockLinkedList() {
        this.head = new MonolithicLockNode("dummy"); // Dummy head node
        this.lock = new ReentrantLock();
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        lock.lock();
        try {
            MonolithicLockNode curr = head.next;
            while (curr != null) {
                if (value.equals(curr.value)) {
                    return true;
                }
                curr = curr.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        lock.lock();
        try {
            MonolithicLockNode pred = head;
            MonolithicLockNode curr = head.next;

            while (curr != null) {
                if (value.equals(curr.value)) {
                    pred.next = curr.next;
                    return true;
                }
                pred = curr;
                curr = curr.next;
            }

            return false;
        } finally {
            lock.unlock();
        }
    }

    public void add(Object value, int sleepTime) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        MonolithicLockNode newNode = new MonolithicLockNode(value);

        lock.lock();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            MonolithicLockNode curr = head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newNode;
        } finally {
            lock.unlock();
        }
    }
}
