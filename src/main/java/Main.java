import structures.FibonacciHeap;
import utils.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        testUnion();
        testDeleteMin();
        testInsert();
        testDecreaseKey();
        testDelete();
    }


    public static FibonacciHeap<Integer> generateRandomHeap(int elementCount) {
        FibonacciHeap<Integer> heap = FibonacciHeap.makeHeap();

        for (int i = 0; i < elementCount; i++) {
            heap.insert((int) (ThreadLocalRandom.current().nextDouble() * 100));
        }

        return heap;
    }

    public static void testInsert() {
        FibonacciHeap<Integer> heap = FibonacciHeap.makeHeap();
        int[] numbers = {5, 2, 3, 4, 1, 6, 11, 0, 9, 10};

        Logger.printHeader("Insert test");
        Logger.printDetails("Initial heap");
        heap.display();
        for (int number : numbers) {
            Logger.printDetails("Inserting: " + number);
            heap.insert(number);
            heap.display();
        }
        Logger.printDivider();

    }

    public static void testDeleteMin() {
        final int count = 10;
        FibonacciHeap<Integer> heap = generateRandomHeap(count);

        Logger.printHeader("DeleteMin test");
        for (int i = 0; i < count + 1; i++) {
            Logger.printDetails("Minimum: " + (heap.minimum() == null ? "-" : heap.minimum()));
            heap.display();
            heap.deleteMin();
        }
        Logger.printDivider();
    }

    public static void testDecreaseKey() {
        FibonacciHeap<Integer> heap = FibonacciHeap.makeHeap();
        List<FibonacciHeap.Node<Integer>> nodes = heap.insertAll(5, 2, 3, 4, 1, 1, 10, 9, 8, 7, 6);

        nodes.remove(heap.deleteMin());

        Logger.printHeader("DecreaseKey test");
        heap.display();
        nodes.forEach(x -> {
            Logger.printDetails("Decreasing key " + x.getElement() + " to " + (x.getElement() - 1));
            heap.decreaseKey(x, x.getElement() - 1);
            heap.display();
        });
        Logger.printDivider();

    }

    public static void testUnion() {

        FibonacciHeap<Integer> heap1 = generateRandomHeap(10);
        FibonacciHeap<Integer> heap2 = generateRandomHeap(5);

        Logger.printDivider();
        Logger.printHeader("Union test");
        Logger.printDetails("Heap 1");
        heap1.display();

        Logger.printDetails("Heap 2");
        heap2.display();

        Logger.printDetails("Union heap");
        heap1.union(heap2);
        heap1.display();
        Logger.printDivider();

    }

    public static void testDelete() {
        FibonacciHeap<Integer> heap = FibonacciHeap.makeHeap();
        List<FibonacciHeap.Node<Integer>> nodes = heap.insertAll(5, 2, 3, 4, 1, 1, 10, 9, 8, 7, 6);

        nodes.remove(heap.deleteMin());
        Collections.shuffle(nodes);

        Logger.printHeader("Delete test");
        Logger.printDetails("Initial heap");
        heap.display();

        nodes.forEach(x -> {
            Logger.printDetails("Deleting " + x.getElement());
            heap.delete(x);
            heap.display();
        });
        Logger.printDivider();


    }


}