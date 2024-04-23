public class Counter {

    private int count;

    public Counter() {
        count = 0;
    }

    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        Counter c1 = new Counter();

        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                c1.increment();
            }
            System.out.println("The value of count for " + Thread.currentThread().getName() + " is " + c1.getCount());
        });
        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                c1.decrement();
            }
            System.out.println("The value of count for " + Thread.currentThread().getName() + " is " + c1.getCount());
        });
        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
            System.out.println("Final value is: " + c1.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

