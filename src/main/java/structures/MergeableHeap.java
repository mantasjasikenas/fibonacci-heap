package structures;

public interface MergeableHeap<E extends Comparable<E>> {
    /**
     * Clears the heap.
     */
    void clear();

    /**
     * Returns heap size
     * @return heap size
     */
    int size();

    /**
     * Checks if the heap is empty.
     * @return true if the heap is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Inserts a new element into the heap.
     * @param element the element to insert.
     */
    FibonacciHeap.Node<E> insert(E element);

    /**
     * Union two heaps.
     * @param heap the heap to union with.
     */
    void union(FibonacciHeap<E> heap);

    /**
     * Returns minimum element in the heap.
     * @return minimum element in the heap.
     */
    FibonacciHeap.Node<E> minimum();

    /**
     * Removes and returns minimum element in the heap.
     * @return minimum element in the heap.
     */
    FibonacciHeap.Node<E> deleteMin();

    /**
     * Deletes the given node from the heap.
     * @param node the node to delete.
     * @return the deleted node.
     */
    FibonacciHeap.Node<E> delete(FibonacciHeap.Node<E> node);

    /**
     * Decreases the key of the given node.
     * @param node the node to decrease the key of.
     * @param element the new key.
     */
    void decreaseKey(FibonacciHeap.Node<E> node, E element);
}
