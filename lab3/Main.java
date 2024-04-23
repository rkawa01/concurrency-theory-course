package lab3;

import java.util.LinkedList;

class Buffer {
    private LinkedList<Integer> buffer = new LinkedList<>();
    private LinkedList<Integer> buffer2 = new LinkedList<>();
//    there we need to create list with n-1 buffors for n processors
    // private LinkedList<Integer> list_of_buffers = new LinkedList<>();
    private int capacity, capacity2;

    public Buffer(int capacity, int capacity2) {
        this.capacity = capacity;
        this.capacity2 = capacity2;
//        for (int i = 0; i < 10; i++){
//            list_of_buffers.add(new LinkedList<>());
//        }
    }

    public void produce(int item) throws InterruptedException {
        synchronized (this) {
            while (buffer.size() == capacity) {
                // Bufor jest pełny, producent czeka.
                System.out.println("Producent czeka.");
                wait();
                System.out.println("Producent został obudzony.");
            }
            buffer.add(item);
            System.out.println("Producent wyprodukował: " + item);
            notify(); // Budzenie konsumenta.
        }
    }

    public void process() throws InterruptedException {
        synchronized (this){
            while (buffer.isEmpty() || buffer2.size() == capacity2) {
                System.out.println("Procesor do repro czeka.");
                wait();
                System.out.println("Procesor do repro został obudzony.");
            }
            int item = buffer.remove();
            long processedItem = item + Math.round(Math.random() * 200);
            buffer2.add((int) processedItem);
            System.out.println("Procesor do repro przetworzył: " + processedItem);
            notify();
        }
    }

    public int consume() throws InterruptedException {
        synchronized (this) {
            while (buffer2.isEmpty()) {
                // Bufor jest pusty, konsument czeka.
                System.out.println("Konsument czeka.");
                wait();
                System.out.println("Konsument został obudzony.");

            }
            int item = buffer2.remove();
            System.out.println("Konsument skonsumował: " + item);
            notify(); // Budzenie producenta.
            return item;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        final Buffer buffer = new Buffer(5,5);
        int n1 = 1;
        int n2 = 2;
        int n3 = 3;

        for (int i = 0; i < n1; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
                        buffer.produce(j);
                        Thread.sleep((long) (Math.random() * 200));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        for (int i = 0; i < n2; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
// in case we want n processors we can pass to process integers i and i + 1 like .process(i, i + 1)
                        buffer.process();
                        Thread.sleep((long) (Math.random() * 200));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        for (int i = 0; i < n3; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
                        buffer.consume();
//                        random sleep
                        Thread.sleep((long) (200 + Math.random() * 200));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
