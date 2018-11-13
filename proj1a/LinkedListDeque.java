public class LinkedListDeque<T> {

    private TNode sentinel;
    private int size;

    public class TNode {
        public TNode prev;
        public T item;
        public TNode next;

        public TNode(TNode p, T i, TNode n){
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque(){
        sentinel = new TNode(null,null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(T i){
        sentinel = new TNode(null,null, null);
        //create first item
        sentinel.next = new TNode(sentinel,i,sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
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

    public void addFirst(T item){
        sentinel.next = new TNode(sentinel,item,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    public void addLast(T item){
        sentinel.prev = new TNode(sentinel.prev,item,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    public void printDeque(){
        TNode p = sentinel.next;
        while(p!=sentinel){
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    public T removeFirst(){
        if(!isEmpty()) {
            T oldFirst = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return oldFirst;
        }
        return null;
    }

    public T removeLast(){
        if(!isEmpty()) {
            T oldLast = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return oldLast;
        }
        return null;
    }

}
