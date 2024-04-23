package lab4;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class Philosopher extends Thread {
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;
    List<Long> executionTimes = new ArrayList<>();

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Filozof " + id + " myśli.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        System.out.println("Filozof " + id + " je.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    @Override
    public void run() {
        try {
            int counter = 0;
            while (true) {
                long start = System.currentTimeMillis();
                think();
                leftFork.acquire();
                rightFork.acquire();
                eat();
                long end = System.currentTimeMillis();
                executionTimes.add(end - start);

                rightFork.release();
                leftFork.release();
                counter += 1;
                if (counter == 10) {
                    System.out.println("Filozof " + id + " zjadł 100 razy.");
                    System.out.println("Filozof " + id + " średni czas jedzenia: " + executionTimes.stream().mapToLong(Long::longValue).sum() / executionTimes.size());
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



class Philosopher2 extends Thread {
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;
    List<Long> executionTimes = new ArrayList<>();
    public Philosopher2(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Filozof " + id + " myśli.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        System.out.println("Filozof " + id + " je.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    @Override
    public void run() {
        try {
            int counter = 0;
            while (true) {
                long start = System.currentTimeMillis();
                think();
                leftFork.acquire();
                if (rightFork.tryAcquire()) {
                    eat();
                    long end = System.currentTimeMillis();
                    executionTimes.add(end - start);
                    rightFork.release();
                }
                leftFork.release();
                counter += 1;
                if (counter == 10) {
                    System.out.println("Filozof " + id + " średni czas jedzenia: " + executionTimes.stream().mapToLong(Long::longValue).sum() / executionTimes.size());
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Philosopher3 extends Thread {
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;
    List<Long> executionTimes = new ArrayList<>();
    public Philosopher3(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Filozof " + id + " myśli.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        System.out.println("Filozof " + id + " je.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    @Override
    public void run() {
        try {
            int counter = 0;
            while (true) {
                long start = System.currentTimeMillis();
                think();
                if (id % 2 == 0) {
                    rightFork.acquire();
                    leftFork.acquire();
                } else {
                    leftFork.acquire();
                    rightFork.acquire();
                }
                eat();
                long end = System.currentTimeMillis();
                executionTimes.add(end - start);
                rightFork.release();
                leftFork.release();
                counter += 1;
                if (counter == 10) {
                    System.out.println("Filozof " + id + " średni czas jedzenia: " + executionTimes.stream().mapToLong(Long::longValue).sum() / executionTimes.size());
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Philosopher4 extends Thread {
    private Semaphore arbiter;
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;
    List<Long> executionTimes = new ArrayList<>();

    public Philosopher4(int id, Semaphore leftFork, Semaphore rightFork, Semaphore arbiter) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.arbiter = arbiter;
    }

    private void think() throws InterruptedException {
        System.out.println("Filozof " + id + " myśli.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        System.out.println("Filozof " + id + " je.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    @Override
    public void run() {
        try {
            int counter = 0;
            while (true) {
                long start = System.currentTimeMillis();
                think();
                arbiter.acquire();
                leftFork.acquire();
                if (rightFork.tryAcquire()) {
                    eat();
                    long end = System.currentTimeMillis();
                    executionTimes.add(end - start);
                    rightFork.release();
                }
                leftFork.release();
                arbiter.release();
                counter += 1;
                if (counter == 10) {
                    System.out.println("Filozof " + id + " średni czas jedzenia: " + executionTimes.stream().mapToLong(Long::longValue).sum() / executionTimes.size());
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class Main {
    public static void main(String[] args) {
        final int n = 5;
        final Semaphore[] forks = new Semaphore[n];
        for (int i = 0; i < n; i++) {
            forks[i] = new Semaphore(1);
        }
        final Semaphore arbiter = new Semaphore(n - 1);
        final Philosopher4[] philosophers = new Philosopher4[n];
        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher4(i, forks[i], forks[(i + 1) % n], arbiter);
            philosophers[i].start();
        }

    }
}