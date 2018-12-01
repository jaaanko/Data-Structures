public class ArrayDeque <T>{
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

    public void addFirst(T item){
        if(size == items.length){
            resize(size * 2);
        }
        items[nextFirst] = item;
        size++;
        nextFirst = minusOne(nextFirst);
    }

    public void addLast(T item){
        if(size == items.length){
            resize(size * 2);
        }
        items[nextLast] = item;
        size++;
        nextLast = plusOne(nextLast);
    }

    public void printDeque(){
        int x = plusOne(nextFirst);
        for(int i = 0;i<size;i++){
            System.out.print(items[x] + " ");
            x = plusOne(x);
        }
    }

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

    public T get(int index){
        if(!isEmpty() && index < size && index >= 0){
            return items[getIndex(index,nextFirst)];
        }
        return null;
    }

    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }

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
