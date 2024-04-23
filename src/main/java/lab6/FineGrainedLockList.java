package lab6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class Node {
    Object value;
    Node next;
    Lock lock;

    public Node(Object value) {
        this.value = value;
        this.next = null;
        this.lock = new ReentrantLock();
    }
}

class FineGrainedLockList {
    private Node head;

    public FineGrainedLockList() {
        this.head = new Node(null); // Dummy head node
    }

    public boolean contains(Object o) {
        Node pred = head;
        pred.lock.lock();
        try {
            Node curr = pred.next;
            if (curr != null) {
                curr.lock.lock();
            try {
                while (curr != null) {
                    if (o.equals(curr.value)) {
                        return true;
                    }
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null){
                        curr.lock.lock();
                    }
                }
                return false;
            } finally {
                if (curr != null) {
                    curr.lock.unlock();
                }
            }
            } else {
                return false;
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(Object o) {
        Node pred = head;
        pred.lock.lock();
        try {
            Node curr = pred.next;

            if (curr != null) {
                curr.lock.lock();

                try {
                    while (curr != null) {
                        if (o.equals(curr.value)) {
                            pred.next = curr.next;
                            return true;
                        }
                        pred.lock.unlock();
                        pred = curr;
                        curr = curr.next;
                        curr.lock.lock();
                    }
                    return false;
                } finally {
                    curr.lock.unlock();
                }
            }  else {
                return false;
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean add(Object o,int sleepTime) {
        Node newNode = new Node(o);
        Node pred = head;
        pred.lock.lock();
        try {
            Node curr = pred.next;
            if (curr != null) {
                curr.lock.lock();
                // sleep for sleepTime milliseconds
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    while (curr != null) {
                        pred.lock.unlock();
                        pred = curr;
                        curr = curr.next;
                        if (curr != null) {
                            curr.lock.lock();
                        }
                    }
                    pred.next = newNode;
                    return true;
                } finally {
                    if (curr != null) {
                        curr.lock.unlock();
                    }
                }
            } else {
                pred.next = newNode;
                return true;
            }

        } finally {
            pred.lock.unlock();
        }
    }
}