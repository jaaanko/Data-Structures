/**
    An array deque (double-ended queue) implementation that treats the array as circular.
    (e.g. adding an item to the front when the current front index is 0, will add an item
    to the back/last index of the array).
    The initial front and back positions were arbitrarily chosen.
*/
public class ArrayDeque<T> implements Deque<Item>{
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }
	@Override
    public void addFirst(T item){
        if(size == items.length){
            resize(size * 2);
        }
        items[nextFirst] = item;
        size++;
        nextFirst = minusOne(nextFirst);
    }
	@Override
    public void addLast(T item){
        if(size == items.length){
            resize(size * 2);
        }
        items[nextLast] = item;
        size++;
        nextLast = plusOne(nextLast);
    }
	@Override
    public void printDeque(){
        int x = plusOne(nextFirst);
        for(int i = 0;i<size;i++){
            System.out.print(items[x] + " ");
            x = plusOne(x);
        }
    }
	@Override
    public T removeLast(){
        if(items.length >= 16 && (size * 1.0)/items.length < 0.25){
            resize(items.length / 2);
        }
        T oldLast = items[minusOne(nextLast)];
        items[minusOne(nextLast)] = null;
        size--;
        nextLast = minusOne(nextLast);
        return oldLast;
    }
	@Override
    public T removeFirst(){
        if(items.length >= 16 && (size * 1.0)/items.length < 0.25){
            resize(items.length / 2);
        }
        T oldFirst = items[plusOne(nextFirst)];
        items[plusOne(nextFirst)] = null;
        size--;
        nextFirst = plusOne(nextFirst);
        return oldFirst;
    }
	@Override
    public T get(int index){
        if(!isEmpty() && index < size && index >= 0){
            return items[getIndex(index,nextFirst)];
        }
        return null;
    }
	@Override
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
	@Override
    public int size(){
        return size;
    }

    private int minusOne(int index){
        if(index == 0){
            return items.length - 1;
        }
        return index - 1;
    }

    private int plusOne(int index){
        if(index == items.length - 1){
            return 0;
        }
        return index + 1;
    }

    private int getIndex(int index, int nextFirst){
        int x = plusOne(nextFirst) + index;
        if(x >= items.length){
            x = x - items.length;
        }
        return x;
    }

    /**
        The resize() function:
        1. Doubles the size of the array when the array gets full.
        2. Halves the size of the array when less than 25% of the total space is used, and the
           array length is 16 or more.
		   
        Example for scenario #1:
                              L  F
        Before resize(): | 6, 7, 0, 1, 2, 3, 4, 5 |
                          F                    L
        After resize(): | 0, 1, 2, 3, 4, 5, 6, 7, , , , , , , |
    */
    private void resize(int capacity){
        T a[] = (T[]) new Object[capacity];
        int start = plusOne(nextFirst);
        System.arraycopy(items,start,a,0,size - start);
        if(start != 0){
            System.arraycopy(items,0,a,size-start,start);
        }
        nextLast = size;
        items = a;
        nextFirst = items.length - 1;
    }

}
