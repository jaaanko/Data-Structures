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
        items[nextFirst] = item;
        size++;
        nextFirst = minusOne(nextFirst);
    }

    public void addLast(T item){
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
        T oldLast = items[minusOne(nextLast)];
        items[minusOne(nextLast)] = null;
        size--;
        nextLast = minusOne(nextLast);
        return oldLast;
    }

    public T removeFirst(){
        T oldFirst = items[plusOne(nextFirst)];
        items[plusOne(nextFirst)] = null;
        size--;
        nextFirst = plusOne(nextFirst);
        return oldFirst;
    }

    public T get(int index){
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

    public int minusOne(int index){
        if(index == 0){
            return items.length - 1;
        }
        return index - 1;
    }

    public int plusOne(int index){
        if(index == items.length - 1){
            return 0;
        }
        return index + 1;
    }

}
