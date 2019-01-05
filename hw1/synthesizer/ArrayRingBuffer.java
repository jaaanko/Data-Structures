package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>{

    private int first;
    private int last;
    private T[] rb;

    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public Iterator<T> iterator(){
        return new BufferIterator();
    }

    public class BufferIterator implements Iterator<T>{
        private int temp;
        private int i;
        BufferIterator(){
            temp = first;
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i != fillCount;
        }

        @Override
        public T next() {
            T x = rb[temp];
            temp ++;
            i ++;
            if(temp == capacity){
                temp = 0;
            }
            return x;
        }
    }
    @Override
    public void enqueue(T x) {
        if(isFull()){
            throw new RuntimeException("Ring buffer overflow");
        }
        else {
            rb[last] = x;
            last++;
            if (last == capacity) {
                last = 0;
            }
            fillCount++;
        }
    }

    public T dequeue() {
        T x;
        if(isEmpty()){
            throw new RuntimeException("Ring buffer underflow");
        }
        else {
            x = rb[first];
            rb[first] = null;
            fillCount --;
            first ++;
            if(first == capacity){
                first = 0;
            }
        }
        return x;
    }

    public T peek() {
        T x;
        if(isEmpty()){
            throw new RuntimeException("Ring buffer underflow");
        }
        else {
            x = rb[first];
        }
        return x;
    }

}
