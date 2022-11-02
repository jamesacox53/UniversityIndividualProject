package CNFSATSolver.CDCLSolver.CoreClasses;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class MinHeap<T> {

    private ArrayList<T> heap;
    private int firstElement = 1;
    private int size = 0;
    private Comparator<T> comparator;

    public MinHeap(Comparator<T> comparator) {
        heap = new ArrayList<>();
        heap.add(null);
        this.comparator = comparator;
    }

    public void add(T elem) {
        size++;
        while (heap.size() - 1 < size) {
            heap.add(null);
        }

        heap.set(size, elem);
        setLocation(elem, size);

        if (heap.size() > 2) {
            siftUp(size, elem);
        }
    }

    protected T removeMin() {

        if (heap.size() <= 1) {
            return null;
        }

        T res = heap.get(firstElement);
        setLocation(res, -1);

        T lastElem = null;
        if (heap.size() > 2) {
            lastElem = heap.get(size);
            heap.set(firstElement, lastElem);
            setLocation(lastElem, firstElement);
        }

        size--;

        while(heap.size() -1 > size) {
            heap.remove(heap.size() - 1);
        }
        if (heap.size() > 2) {
            siftDown(firstElement, lastElem);
        }
        return res;
    }

    public void removeIndex(int elemIndex) {

        if (heap.size() <= 1 || elemIndex <= 0 || elemIndex > size) {
            return;
        }

        T elem = heap.get(elemIndex);
        setLocation(elem, -1);

        T lastElem = null;
        if (elemIndex < size && heap.size() > 2) {
            lastElem = heap.get(size);
            heap.set(elemIndex, lastElem);
            setLocation(lastElem, elemIndex);
        }

        size--;

        while(heap.size() -1 > size) {
            heap.remove(heap.size() - 1);
        }
        if (elemIndex <= size && heap.size() > 2) {
            siftDown(elemIndex, lastElem);

            int newElemIndex = getLocation(lastElem);

            if (newElemIndex == elemIndex) {
                siftUp(newElemIndex, lastElem);
            }
        }
    }


    protected void siftUp(int location, T elem) {

        if (location <= 1 || location > size) {
            return;
        }

        boolean stopped = false;

        while (!stopped && location != firstElement) {

            int parentIndex = parentIndex(location);
            T parentElement = heap.get(parentIndex);
            int compareParent = comparator.compare(elem, parentElement);

            if (compareParent < 0) {

                heap.set(location, parentElement);
                setLocation(parentElement, location);

                location = parentIndex;
                heap.set(location, elem);
                setLocation(elem, location);

            } else {
                stopped = true;
            }
        }
    }

    protected void siftDown(int location, T elem) {

        if (location <= 0 || location > size) {
            return;
        }

        boolean stopped = false;

        while(!stopped && !isLeaf(location)) {

            int leftChildIndex = leftChildIndex(location);
            boolean hasLeftChild = false;
            T leftChild = null;
            int compareLeftChild = -1;

            if (leftChildIndex <= size) {
                hasLeftChild = true;
                leftChild = heap.get(leftChildIndex);
                compareLeftChild = comparator.compare(elem, leftChild);
            }

            int rightChildIndex = rightChildIndex(location);
            boolean hasRightChild = false;
            T rightChild = null;
            int compareRightChild = -1;

            if (rightChildIndex <= size) {
                hasRightChild = true;
                rightChild = heap.get(rightChildIndex);
                compareRightChild = comparator.compare(elem, rightChild);
            }

            if (compareLeftChild > 0 || compareRightChild > 0) {

                if (hasLeftChild && hasRightChild) {
                    location = siftDownHasBothChildren(location, elem, leftChildIndex, leftChild, rightChildIndex, rightChild);

                } else if (hasLeftChild) {
                    location = siftDownLeftChild(location, elem, leftChildIndex, leftChild);

                } else {
                    location = siftDownRightChild(location, elem, rightChildIndex, rightChild);
                }

            } else {
                stopped = true;
            }
        }
    }

    private int siftDownHasBothChildren(int location, T elem, int leftChildIndex, T leftChild, int rightChildIndex, T rightChild) {

        int compareChildren = comparator.compare(leftChild, rightChild);
        // left child better
        if (compareChildren < 0) {
            return siftDownLeftChild(location, elem, leftChildIndex, leftChild);
        } else {
            return siftDownRightChild(location, elem, rightChildIndex, rightChild);
        }
    }

    private int siftDownLeftChild(int location, T elem, int leftChildIndex, T leftChild) {

        heap.set(location, leftChild);
        setLocation(leftChild, location);

        location = leftChildIndex;
        heap.set(location, elem);
        setLocation(elem, location);
        return location;
    }

    private int siftDownRightChild(int location, T elem, int rightChildIndex, T rightChild) {

        heap.set(location, rightChild);
        setLocation(rightChild, location);

        location = rightChildIndex;
        heap.set(location, elem);
        setLocation(elem, location);
        return location;
    }

    private int parentIndex(int elemIndex) {
        return elemIndex / 2;
    }

    private int leftChildIndex(int elemIndex) {
        return (2 * elemIndex);
    }

    private int rightChildIndex(int elemIndex) {
        return (2 * elemIndex) + 1;
    }

    private boolean isLeaf(int elemIndex) {

        if ((elemIndex > (size / 2)) && (elemIndex <= size)) {
            return true;
        } else {
            return false;
        }
    }

    protected int getLocation(T elem) {
        return -1;
    }

    protected void setLocation(T elem, int loc) {
    }

}
