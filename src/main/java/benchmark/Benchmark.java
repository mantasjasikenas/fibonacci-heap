package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import structures.FibonacciHeap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class Benchmark {

    @State(Scope.Benchmark)
    public static class FullHeap {

        FibonacciHeap<Integer> heap;

        List<Integer> items;

        List<FibonacciHeap.Node<Integer>> nodes;

        @Setup(Level.Iteration)
        public void generateIdsAndCars(BenchmarkParams params) {
            items = Benchmark.generateElements(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillHeap(BenchmarkParams params) {
            heap = FibonacciHeap.makeHeap();
            nodes = new LinkedList<>();
            items.forEach(x -> {
                nodes.add(heap.insert(x));
            });
        }
    }

    @Param({"10000", "20000", "40000", "80000", "160000"})
    public int elementCount;

    List<Integer> elements;

    @Setup(Level.Iteration)
    public void generateItems() {
        elements = generateElements(elementCount);
    }

    public static List<Integer> generateElements(int elementCount) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            numbers.add(ThreadLocalRandom.current().nextInt());
        }
        return numbers;
    }


    @org.openjdk.jmh.annotations.Benchmark
    public void insert() {
        FibonacciHeap<Integer> heap = new FibonacciHeap<>();
        elements.forEach(heap::insert);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void deleteMin(FullHeap fullHeap) {
        for (int i = 0; i < elementCount; i++) {
            fullHeap.heap.deleteMin();
        }
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void decreaseKey(FullHeap fullHeap) {
        fullHeap.nodes.forEach(node -> fullHeap.heap.decreaseKey(node, node.getElement() - 1));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void union(FullHeap fullHeap1, FullHeap fullHeap2) {
        fullHeap1.heap.union(fullHeap2.heap);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void delete(FullHeap fullHeap) {
        fullHeap.nodes.forEach(fullHeap.heap::delete);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
