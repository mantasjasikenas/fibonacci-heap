package structures;

import utils.Logger;

import java.util.*;

public class FibonacciHeap<E extends Comparable<E>> implements MergeableHeap<E> {

    /**
     * The min node in the heap.
     */
    private Node<E> min;

    /**
     * Size of the heap.
     */
    private int size;

    /**
     * Comparator used to compare elements.
     */
    private final Comparator<? super E> comparator;

    /**
     * Constructs a new FibonacciHeap with the default comparator.
     */
    public FibonacciHeap() {
        comparator = Comparator.naturalOrder();
    }

    /**
     * Constructs a new FibonacciHeap with the given comparator.
     *
     * @param comparator the comparator to use.
     */
    public FibonacciHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Creates a new heap.
     *
     * <p>Running time: O(1)</p>
     *
     * @param <E> the type of the elements in the heap.
     * @return a new heap.
     */
    public static <E extends Comparable<E>> FibonacciHeap<E> makeHeap() {
        return new FibonacciHeap<>();
    }

    /**
     * Creates a new heap with the given comparator.
     *
     * <p>Running time: O(1)</p>
     *
     * @param comparator the comparator to use.
     * @return a new heap.
     */
    public static <E extends Comparable<E>> FibonacciHeap<E> makeHeap(Comparator<E> comparator) {
        return new FibonacciHeap<>(comparator);
    }

    /**
     * Merges two heaps.
     *
     * <p>Running time: O(1)</p>
     *
     * @param heap the heap to unite.
     */
    @Override
    public void union(FibonacciHeap<E> heap) {
        // If the other heap is empty, do nothing.
        if (heap == null || heap.isEmpty())
            return;

        // If this heap is empty, make it the other heap.
        if (isEmpty()) {
            min = heap.min;
        } else { // Otherwise, merge the two heaps.

            min.right.left = heap.min.left;
            heap.min.left.right = min.right;
            min.right = heap.min;
            heap.min.left = min;

            if (comparator.compare(heap.min.element, min.element) < 0) {
                min = heap.min;
            }
        }

        // Update the size.
        size += heap.size();

    }

    /**
     * Clears the heap.
     */
    @Override
    public void clear() {
        min = null;
        size = 0;
    }

    /**
     * Returns heap size
     *
     * @return heap size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return true if the heap is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return min == null;
    }

    /**
     * Inserts a new element into the heap.
     *
     * <p>Running time: O(1)</p>
     *
     * @param element the element to insert.
     */
    @Override
    public Node<E> insert(E element) {
        Node<E> insNode = new Node<>(element);
        if (isEmpty()) {
            min = insNode;
        } else {
            insert(min, insNode);
            if (comparator.compare(insNode.element, min.element) < 0)
                min = insNode;
        }

        size++;
        return insNode;
    }

    /**
     * Inserts all elements into the heap.
     *
     * @param elements the elements to insert.
     * @return a list of the nodes inserted.
     */

    @SafeVarargs
    public final List<Node<E>> insertAll(E... elements) {
        List<Node<E>> nodes = new ArrayList<>(elements.length);
        for (E element : elements) {
            nodes.add(insert(element));
        }
        return nodes;
    }

    /**
     * Returns minimum element in the heap.
     *
     * <p>Running time: O(1)</p>
     *
     * @return minimum element in the heap.
     */
    @Override
    public Node<E> minimum() {
        return min;
    }

    /**
     * Removes and returns minimum element in the heap.
     *
     * <p>Running time: O(log n)</p>
     *
     * @return minimum element in the heap.
     */
    @Override
    public Node<E> deleteMin() {

        Node<E> extractNode = min;

        if (extractNode != null) {
            int childrenCount = extractNode.degree;
            Node<E> childNode = extractNode.child;
            Node<E> rightNode;

            // every 'min' node childrenCount should be transferred into heap's root list
            while (childrenCount > 0) {
                rightNode = childNode.right;

                // delete from childrenCount list
                removeLeftRightLinks(childNode);

                // add into heap's root list
                insert(min, childNode);

                // reset old parent data
                childNode.parent = null;
                childNode = rightNode;

                childrenCount--;
            }

            // delete 'min' node
            removeLeftRightLinks(extractNode);

            // if link sends into itself, then no other nodes are left
            if (extractNode == extractNode.right) min = null;
            else {
                min = extractNode.right;
                consolidate();
            }

            size--;
        }

        return extractNode;
    }

    /**
     * Deletes a node from the heap given the reference to the node.
     * The trees in the heap will be consolidated, if necessary.
     *
     * <p>Running time: O(log n)</p>
     *
     * @param node node to remove from heap
     * @return the element of the removed node
     */
    @Override
    public Node<E> delete(Node<E> node) {

        Node<E> parent = node.parent;

        if ((parent != null)) {
            cut(node, parent);
            cascadingCut(parent);
        }
        min = node;

        // remove the smallest
        return deleteMin();
    }

    /**
     * Decreases the key value for a heap node.
     *
     * <p>Running time: O(1)</p>
     *
     * @param node       the node to decrease the key of.
     * @param element the new key value for node x.
     */
    @Override
    public void decreaseKey(Node<E> node, E element) {
        if (comparator.compare(node.element, element) < 0) {
            throw new IllegalArgumentException(
                    "decreaseKey() got larger key value");
        }

        node.element = element;
        Node<E> parent = node.parent;

        // if node is not root and node's key is less than parent's key
        if ((parent != null) && (comparator.compare(node.element, parent.element) < 0)) {
            cut(node, parent);
            cascadingCut(parent);
        }

        // if new key is smaller than min, update min
        if (comparator.compare(node.element, min.element) < 0) {
            min = node;
        }
    }

    /**
     * Consolidates the heap.
     */
    private void consolidate() {
        int arraySize = (int) (Math.floor(Math.log(size) * (1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0))) + 1);
        Node<E>[] array = new Node[arraySize];

        // Find the number of root nodes
        int roots = 0;
        Node<E> minNode = min;

        if (minNode != null) {
            roots++;
            minNode = minNode.right;

            while (minNode != min) {
                roots++;
                minNode = minNode.right;
            }
        }

        while (roots > 0) {

            // Get minNode's degree for later actions
            int d = minNode.degree;
            Node<E> next = minNode.right;

            // and see if there's another of the same degree.
            while (true) {
                Node<E> y = array[d];
                if (y == null) {
                    break;
                }

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (comparator.compare(y.element, minNode.element) < 0) {
                    Node<E> temp = y;
                    y = minNode;
                    minNode = temp;
                }

                // FibonacciHeapNode<T> newChild disappears from root list.
                link(y, minNode);

                // We've handled this degree, go to next one.
                array[d] = null;
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array[d] = minNode;

            // Move forward through list.
            minNode = next;
            roots--;
        }

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        min = null;
        Node<E> y;

        for (Node<E> eNode : array) {
            y = eNode;
            if (y == null) continue;

            // We've got a live one, add it to root list.
            if (min != null) {
                // First remove node from root list.
                removeLeftRightLinks(y);

                // Now add to root list, again.
                insert(min, y);

                // Check if this is a new min.
                if (comparator.compare(y.element, min.element) < 0) min = y;
            } else min = y;
        }
    }

    /**
     * Inserts node after the given node.
     *
     * @param prevNode the node to insert after.
     * @param insNode  the node to be inserted.
     */
    private void insert(Node<E> prevNode, Node<E> insNode) {
        insNode.left = prevNode;
        insNode.right = prevNode.right;
        prevNode.right = insNode;
        insNode.right.left = insNode;
    }

    /**
     * Links two nodes.
     *
     * @param child  the node to link to.
     * @param parent the node to link from.
     */
    private void link(Node<E> child, Node<E> parent) {

        // remove y from root list of heap
        removeLeftRightLinks(child);

        child.parent = parent;

        if (parent.child == null) {
            parent.child = child;
            child.left = child;
            child.right = child;
        } else {
            child.left = parent.child;
            child.right = parent.child.right;
            parent.child.right = child;
            child.right.left = child;
        }

        parent.degree++;
        child.mark = false;
    }

    /**
     * Performs a cascading cut operation. This cuts node from its parent and then
     * does the same for its parent, and so on up the tree.
     *
     * <p>Running time: O(log n); O(1) excluding the recursion</p>
     *
     * @param node node to perform cascading cut on
     */
    private void cascadingCut(Node<E> node) {
        Node<E> parent = node.parent;

        // if there's a parent...
        if (parent != null) {
            // if node is unmarked, set it marked
            if (!node.mark) {
                node.mark = true;
            } else {
                // it's marked, cut it from parent
                cut(node, parent);

                // cut its parent as well
                cascadingCut(parent);
            }
        }
    }

    /**
     * Cuts a node from its parent.
     *
     * <p>Running time: O(1)</p>
     *
     * @param child node to cut from its parent
     * @param parent node's parent
     */
    private void cut(Node<E> child, Node<E> parent) {
        // remove child from child-list of parent and decrement degree[parent]
        removeLeftRightLinks(child);
        parent.degree--;

        // reset parent.child if necessary
        if (parent.child == child) {
            parent.child = child.right;
        }

        if (parent.degree == 0) {
            parent.child = null;
        }

        // add child to root list of heap
        insert(min, child);

        // set parent[child] to null
        child.parent = null;

        // set mark[child] to false
        child.mark = false;
    }

    /**
     * Deletes the provided node from its list
     *
     * @param node Node to delete
     */
    private void removeLeftRightLinks(Node<E> node) {
        node.left.right = node.right; // Remove left node's link
        node.right.left = node.left; // Remove right node's link
    }

    /**
     * Displays the heap.
     */
    public void display() {
        if (isEmpty()) {
            Logger.printWarning("Heap is empty!");
            return;
        }

        Logger.printTitle("* HEAP * " + size + " entries * " + (min == null ? "-" : min.element) + " minimum *");
        display(min, "");
    }

    /**
     * Displays the heap.
     */
    private void display(Node<E> node, String prefix) {
        if (node == null) return;
        Node<E> temp = node;
        Node<E> k;
        do {
            Logger.printDebug(prefix +
                    "-> ELEMENT: " +
                    temp.element
            );
            k = temp.child;
            display(k, prefix + "   ");
            temp = temp.right;
        } while (temp != node);
    }


    /**
     * A node in the Fibonacci heap.
     * <p>
     * the type of the key.
     * the type of the value.
     */
    public static class Node<E> {
        /**
         * Data stored in the node.
         */
        private E element;
        /**
         * Parent node.
         */
        private Node<E> parent;
        /**
         * First child node.
         */
        private Node<E> child;
        /**
         * Right sibling node.
         */
        private Node<E> right;
        /**
         * Left sibling node.
         */
        private Node<E> left;
        /**
         * Number of children of this node.
         */
        private int degree;
        /**
         * True if this node has had a child removed since this node was
         * added to its parent.
         */
        private boolean mark;

        public Node(E element) {
            this.element = element;
            this.degree = 0;
            this.mark = false;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
        }

        /**
         * Returns the data stored in the node.
         *
         * @return the data stored in the node.
         */
        public E getElement() {
            return element;
        }

        /**
         * ToString override.
         * @return String representation of the node.
         */
        @Override
        public String toString() {
            return element == null ? null : element.toString();
        }
    }
}
