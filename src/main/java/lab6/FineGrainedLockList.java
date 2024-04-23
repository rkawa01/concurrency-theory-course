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
            curr.lock.lock();
            try {
                while (curr.value != null) {
                    if (o.equals(curr.value)) {
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
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(Object o) {
        Node pred = head;
        pred.lock.lock();
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.value != null) {
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
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean add(Object o) {
        Node newNode = new Node(o);
        Node pred = head;
        pred.lock.lock();
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.value != null) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                pred.next = newNode;
                return true;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }
}

class FineGrainedLockReadWrite {
    private FineGrainedLockList list;
    private int readers;
    private Lock globalLock;
    private Condition noWriters;

    public FineGrainedLockReadWrite() {
        this.list = new FineGrainedLockList();
        this.readers = 0;
        this.globalLock = new ReentrantLock();
        this.noWriters = globalLock.newCondition();
    }

    public void startReading() {
        globalLock.lock();
        try {
            while (readers == -1) {
                noWriters.await();
            }
            readers++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            globalLock.unlock();
        }
    }

    public void endReading() {
        globalLock.lock();
        try {
            readers--;
            if (readers == 0) {
                noWriters.signal();
            }
        } finally {
            globalLock.unlock();
        }
    }

    public void startWriting() {
        globalLock.lock();
        try {
            while (readers != 0) {
                noWriters.await();
            }
            readers = -1; // Block all readers
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            globalLock.unlock();
        }
    }

    public void endWriting() {
        globalLock.lock();
        try {
            readers = 0;
            noWriters.signal();
        } finally {
            globalLock.unlock();
        }
    }
}