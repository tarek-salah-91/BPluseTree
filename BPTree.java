
import java.util.ArrayList;
import java.util.LinkedList;

public class BPTree<E extends Comparable<E>> {

    private int leafSize;
    private int internalSize;
    private Node<E> root;
    private int size;
    private NativeMethods<E> nm;
    private LinkedList<Node<E>> buffer;
    private int bufferboolSize;

    public BPTree(int leafSize, int internalSize, int bufferbool) {
        super();
        this.leafSize = leafSize;
        this.internalSize = internalSize;
        this.root = new Node<E>(leafSize, true);
        nm = new NativeMethods<E>();
        buffer = new LinkedList<Node<E>>();
        this.bufferboolSize = bufferbool;
    }

    // ======================================================================
    // ===========================INSERTION==================================
    @SuppressWarnings("unchecked")
    public void insertNode(E key, Object data) {
        LinkedList<Node<E>> stack = new LinkedList<Node<E>>();
        Node<E> n = root;
        while (!n.isLeaf) {
            stack.push(n);
            // ===================================================
            if (key.compareTo(n.getKeys().get(0)) < 0) {
                n = (Node<E>) n.getPointers().get(0);
            } else if (key.compareTo(n.getKeys().get(n.getKeys().size() - 1)) >= 0) {
                n = (Node<E>) n.getPointers().get(n.getPointers().size() - 1);
            } else {
                for (int i = 0; i < n.getKeys().size() - 1; i++) {
                    if (key.compareTo(n.getKeys().get(i)) >= 0 && key.compareTo(n.getKeys().get(i + 1)) < 0) {
                        n = (Node) n.getPointers().get(i + 1);
                        break;
                    }
                }
            }
        }
        // END OF WHILE
        for (int i = 0; i < n.getKeys().size(); i++) {
            if (key == n.getKeys().get(i)) {
                return;
            }
        }
        if (n.getKeys().size() < leafSize) {
            nm.sortedInsert(key, data, n);
        } else {
            Node<E> temp = new Node(leafSize, true);
            temp.setKeys(new ArrayList<E>(n.getKeys()));
            temp.setPointers(new ArrayList<Object>(n.getPointers()));
            nm.sortedInsert(key, data, temp);
            Node newNode = new Node(leafSize, true);
            int j = (int) Math.ceil(n.getPointers().size() / (double) 2);
            n.setKeys(new ArrayList<E>(temp.getKeys().subList(0, j)));
            n.setPointers(new ArrayList<Object>(temp.getPointers().subList(0, j)));
            if (n.getNext() != null) {
                n.getNext().setPrev(newNode);
            }
            newNode.setNext(n.getNext());
            n.setNext(newNode);
            newNode.setPrev(n);
            newNode.setKeys(new ArrayList<E>(temp.getKeys().subList(j, temp.getKeys().size())));
            newNode.setPointers(new ArrayList<Object>(temp.getPointers().subList(j, temp.getPointers().size())));

            key = temp.getKeys().get(j);
            boolean finished = false;
            do {
                if (stack.isEmpty()) {
                    root = new Node(internalSize, false);
                    ArrayList<Object> point = new ArrayList<Object>();
                    point.add(n);
                    point.add(newNode);
                    ArrayList<E> keys_ = new ArrayList<E>();
                    keys_.add(key);
                    root.setKeys(keys_);
                    root.setPointers(point);
                    finished = true;
                } else {
                    n = stack.pop();
                    if (n.getKeys().size() < internalSize) {
                        nm.sortedInsertInternal(key, newNode, n);
                        finished = true;
                    } else {
                        temp.setLeaf(false);
                        temp.setKeys(new ArrayList<E>(n.getKeys()));
                        temp.setPointers(new ArrayList<Object>(n.getPointers()));

                        nm.sortedInsertInternal(key, newNode, temp);
                        newNode = new Node(internalSize, false);
                        j = (int) Math.ceil(temp.getPointers().size() / (double) 2);

                        n.setKeys(new ArrayList<E>(temp.getKeys().subList(0, j - 1)));
                        n.setPointers(new ArrayList<Object>(temp.getPointers().subList(0, j)));
                        if (n.getNext() != null) {
                            n.getNext().setPrev(newNode);
                        }
                        newNode.setNext(n.getNext());
                        n.setNext(newNode);
                        newNode.setPrev(n);
                        newNode.setKeys(new ArrayList<E>(temp.getKeys().subList(j, temp.getKeys().size())));
                        newNode.setPointers(new ArrayList<Object>(temp.getPointers().subList(j, temp.getPointers().size())));

                        key = temp.getKeys().get(j - 1);
                    }
                }
            } while (!finished);
        }
    }
//======================================BULK INSERTION=================================================

    @SuppressWarnings("unchecked")
    public void insertBulk(ArrayList<E> keys, ArrayList<Object> records) {
        E key;
        boolean firstInsert = true;
        int first = 0;
        int second = 0;
        for (int i = 0; i < Math.ceil(keys.size() / (double) leafSize); i++) {
            LinkedList<Node<E>> stack = new LinkedList<Node<E>>();
            Node<E> n = root;
            first = second;
            second = second + leafSize;
            if (second > keys.size()) {
                second = keys.size();
            }
            ArrayList<E> newKeys = new ArrayList<E>(keys.subList(first, second));
            ArrayList<Object> newRecords = new ArrayList<Object>(records.subList(first, second));
            while (!n.isLeaf) {
                stack.push(n);
                n = (Node<E>) n.getPointers().get(n.getPointers().size() - 1);
            }
            if (firstInsert) {
                root.setKeys(newKeys);
                root.setPointers(newRecords);
                firstInsert = false;
            } else {
                Node<E> temp = new Node(leafSize, true);
                temp.setKeys(new ArrayList<E>(n.getKeys()));
                temp.setPointers(new ArrayList<Object>(n.getPointers()));
                temp.getKeys().addAll(newKeys);
                temp.getPointers().addAll(newRecords);
                Node newNode = new Node(leafSize, true);
                int j = (int) Math.ceil(temp.getPointers().size() / (double) 2);
                n.setKeys(new ArrayList<E>(temp.getKeys().subList(0, j)));
                n.setPointers(new ArrayList<Object>(temp.getPointers().subList(0, j)));
                if (n.getNext() != null) {
                    n.getNext().setPrev(newNode);
                }
                newNode.setNext(n.getNext());
                n.setNext(newNode);
                newNode.setPrev(n);
                newNode.setKeys(new ArrayList<E>(temp.getKeys().subList(j, temp.getKeys().size())));
                newNode.setPointers(new ArrayList<Object>(temp.getPointers().subList(j, temp.getPointers().size())));
                key = temp.getKeys().get(j);
                boolean finished = false;
                do {
                    if (stack.isEmpty()) {
                        root = new Node(internalSize, false);
                        ArrayList<Object> point = new ArrayList<Object>();
                        point.add(n);
                        point.add(newNode);
                        ArrayList<E> keys_ = new ArrayList<E>();
                        keys_.add(key);
                        root.setKeys(keys_);
                        root.setPointers(point);
                        finished = true;
                    } else {
                        n = stack.pop();
                        if (n.getKeys().size() < internalSize) {
                            nm.sortedInsertInternal(key, newNode, n);
                            finished = true;
                        } else {
                            temp.setLeaf(false);
                            temp.setKeys(new ArrayList<E>(n.getKeys()));
                            temp.setPointers(new ArrayList<Object>(n.getPointers()));
                            nm.sortedInsertInternal(key, newNode, temp);
                            newNode = new Node(internalSize, false);
                            j = (int) Math.ceil(temp.getPointers().size() / (double) 2);
                            n.setKeys(new ArrayList<E>(temp.getKeys().subList(0, j - 1)));
                            n.setPointers(new ArrayList<Object>(temp.getPointers().subList(0, j)));
                            if (n.getNext() != null) {
                                n.getNext().setPrev(newNode);
                            }
                            newNode.setNext(n.getNext());
                            n.setNext(newNode);
                            newNode.setPrev(n);
                            newNode.setKeys(new ArrayList<E>(temp.getKeys().subList(j, temp.getKeys().size())));
                            newNode.setPointers(new ArrayList<Object>(temp.getPointers().subList(j, temp.getPointers().size())));
                            key = temp.getKeys().get(j - 1);
                        }
                    }
                } while (!finished);
            }
        }
    }

    // ======================================================================
    // =============================SEARCHING================================
    @SuppressWarnings("unchecked")
    public Node<E> search(E key) {
        for (int i = 0; i < buffer.size(); i++) {
            ArrayList<E> find = buffer.get(i).getKeys();
            if (find.contains(key)) {
                return buffer.get(i);
            }
        }
        Node<E> n = root;
        while (!n.isLeaf) {
            if (key.compareTo(n.getKeys().get(0)) < 0) {
                n = (Node<E>) n.getPointers().get(0);
            } else if (key.compareTo(n.getKeys().get(n.getKeys().size() - 1)) >= 0) {
                n = (Node<E>) n.getPointers().get(n.getPointers().size() - 1);
            } else {
                for (int i = 0; i < n.getKeys().size() - 1; i++) {
                    if (key.compareTo(n.getKeys().get(i)) >= 0 && key.compareTo(n.getKeys().get(i + 1)) < 0) {
                        n = (Node) n.getPointers().get(i + 1);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < n.getKeys().size(); i++) {
            if (key.compareTo(n.getKeys().get(i)) == 0) {
                if (buffer.size() == bufferboolSize) {
                    buffer.removeFirst();
                    buffer.add(n);
                } else {
                    buffer.add(n);
                }
                return n;
            }
        }
        return null;
    }

    // ======================================================================
    // ==============================DELETION================================
    @SuppressWarnings("unchecked")
    public void delete(E key) {
        LinkedList<Node<E>> stack = new LinkedList<Node<E>>();
        Node<E> n = root;
        while (!n.isLeaf) {
            stack.push(n);
            // ===================================================
            if (key.compareTo(n.getKeys().get(0)) < 0) {
                n = (Node<E>) n.getPointers().get(0);
            } else if (key.compareTo(n.getKeys().get(n.getKeys().size() - 1)) >= 0) {
                n = (Node) n.getPointers().get(n.getPointers().size() - 1);
            } else {
                for (int i = 0; i < n.getKeys().size(); i++) {
                    if (key.compareTo(n.getKeys().get(i)) >= 0 && key.compareTo(n.getKeys().get(i + 1)) < 0) {
                        n = (Node) n.getPointers().get(i + 1);
                        break;
                    }
                }
            }
        }
        // END OF WHILE
        boolean flag = false;
        for (int i = 0; i < n.getKeys().size(); i++) {
            if (key == n.getKeys().get(i)) {
                flag = true;
                break;
            }
        }
        //searching to determine if the element is found in leaf node or not
        if (flag) {
            if (n.getKeys().size() - 1 >= Math.ceil(leafSize / 2.0)) {
                nm.deleteNode(n, key);
            } else {
                Node<E> parent = stack.peek();
                int deter = nm.sameParent(n, stack.peek(), leafSize);
                if (deter == 1) {
                    nm.deleteNode(n, key);
                    E element = n.getNext().getKeys().remove(0);
                    n.getKeys().add(element);
                    for (int i = 0; i < parent.getKeys().size(); i++) {
                        if (element.compareTo(parent.getKeys().get(i)) == 0) {
                            parent.getKeys().set(i, n.getNext().getKeys().get(0));
                            break;
                        }
                    }
                    return;
                } else if (deter == 2) {
                    nm.deleteNode(n, key);
                    E element = n.getPrev().getKeys().remove(n.getPrev().getKeys().size() - 1);
                    n.getKeys().add(0, element);
                    for (int i = 0; i < parent.getKeys().size(); i++) {
                        if (element.compareTo(parent.getKeys().get(i)) == 0) {
                            parent.getKeys().set(i, n.getPrev().getKeys().get(n.getPrev().getKeys().size() - 1));
                            break;
                        }
                    }
                    return;
                } else {
                    nm.deleteNode(n, key);
                    int tempKey = 0;
                    int tempPointer = 0;
                    if (n.getNext() != null) {
                        Node<E> next = n.getNext();
                        next.getKeys().addAll(0, n.getKeys());
                        next.getPointers().addAll(0, n.getPointers());
                        for (int i = 0; i < parent.getKeys().size(); i++) {
                            if (next.getKeys().get(n.getKeys().size()).compareTo(parent.getKeys().get(i)) == 0) {
                                tempKey = i;
                                tempPointer = i;
                                break;
                            }
                        }
                        if (tempKey > 0 && parent.getKeys().get(tempKey - 1) == key) {
                            parent.getKeys().set(tempKey - 1, next.getKeys().get(0));
                        }
                    } else {
                        Node<E> prev = n.getPrev();
                        prev.getKeys().addAll(n.getKeys());
                        prev.getPointers().addAll(n.getPointers());
                        for (int i = 0; i < parent.getKeys().size(); i++) {
                            if (n.getKeys().get(0).compareTo(parent.getKeys().get(i)) == 0) {
                                tempKey = i;
                                tempPointer = i + 1;
                                break;
                            }
                        }
                    }
                    boolean finished = false;
                    do {
                        if (stack.isEmpty()) {
                            root.getKeys().remove(tempKey);
                            root.getPointers().remove(tempPointer);
                            finished = true;
                        } else {
                            n = stack.pop();
                            //try borrowing from the cebeling
                            if (n.getKeys().size() - 1 >= 1) {
                                n.getKeys().remove(tempKey);
                                n.getPointers().remove(tempPointer);
                                finished = true;
                            } else {
                                if (n == root) {
                                    n.getKeys().remove(tempKey);
                                    n.getPointers().remove(tempPointer);
                                    if (n.getPointers().size() == 1) {
                                        root = (Node<E>) n.getPointers().get(0);
                                    }
                                    finished = true;
                                } else {
                                    n.getKeys().remove(tempKey);
                                    n.getPointers().remove(tempPointer);
                                    deter = nm.nexOrprev(n, stack.peek(), internalSize);
                                    parent = stack.peek();
                                    if (deter == 1) {
                                        Node<E> element = (Node<E>) n.getNext().getPointers().remove(0);
                                        n.getPointers().add(element);
                                        E tempk = n.getNext().getKeys().get(0);
                                        for (int i = 0; i < parent.getPointers().size(); i++) {
                                            if (n == parent.getPointers().get(i)) {
                                                parent.getKeys().set(i, tempk);
                                                break;
                                            }
                                        }
                                        finished = true;
                                    } else if (deter == 2) {
                                        Node<E> element = (Node<E>) n.getPrev().getPointers().remove(n.getPrev().getPointers().size() - 1);
                                        n.getPointers().add(0, element);
                                        E tempk = n.getPrev().getKeys().get(n.getKeys().size() - 1);
                                        for (int i = 0; i < parent.getPointers().size(); i++) {
                                            if (n == parent.getPointers().get(i)) {
                                                parent.getKeys().set(i - 1, tempk);
                                                break;
                                            }
                                        }
                                        finished = true;
                                    } else {
                                        if (n.getNext() != null) {
                                            for (int i = 0; i < parent.getPointers().size(); i++) {
                                                if (n == parent.getPointers().get(i)) {
                                                    tempKey = i;
                                                    tempPointer = i;
                                                    break;
                                                }
                                            }
                                            Node<E> next = n.getNext();
                                            //next.getKeys().add(0, parent.getKeys().get(tempKey));
                                            next.getKeys().addAll(0, n.getKeys());
                                            next.getPointers().addAll(0, n.getPointers());

                                        } else {
                                            for (int i = 0; i < parent.getPointers().size(); i++) {
                                                if (n == parent.getPointers().get(i)) {
                                                    tempKey = i;
                                                    tempPointer = i + 1;
                                                    break;
                                                }
                                            }
                                            Node<E> prev = n.getPrev();
                                            //prev.getKeys().add(parent.getKeys().get(tempKey));
                                            prev.getKeys().addAll(n.getKeys());
                                            prev.getPointers().addAll(n.getPointers());
                                        }
                                    }
                                }
                            }
                        }
                    } while (!finished);

                }
            }
        } else {
            return;
        }
    }

// ======================================================================
// ===========================GETTERS AND SETTERS========================
    public int getLeafSize() {
        return leafSize;
    }

    public void setLeafSize(int leafSize) {
        this.leafSize = leafSize;
    }

    public int getInternalSize() {
        return internalSize;
    }

    public void setInternalSize(int internalSize) {
        this.internalSize = internalSize;
    }

    public Node<E> getRoot() {
        return root;
    }

    public void setRoot(Node<E> root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;


    }

    @SuppressWarnings("unchecked")
    public String print() {
        String s = "";
        LinkedList<Node<E>> view = new LinkedList<Node<E>>();
        view.add(root);
        while (!view.isEmpty()) {
            Node<E> e = view.pop();
            for (int i = 0; i
                    < e.getKeys().size(); i++) {
                s += (e.getKeys().get(i) + " ");
            }
            for (int i = 0; i < e.getPointers().size(); i++) {
                try {
                    view.add((Node<E>) e.getPointers().get(i));
                } catch (Exception e1) {
                }
            }
            s += "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        BPTree<Integer> t = new BPTree<Integer>(100, 3, 3);
        ArrayList<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < 1000; i++) {
            keys.add(i);
        }
        ArrayList<Object> pointers = new ArrayList<Object>();
        for (int i = 0; i < 1000; i++) {
            pointers.add(i + "");
        }
        t.insertBulk(keys, pointers);
        t.print();
        System.out.println("===========================================");
        //System.out.println(t.search(10).getKeys());
//        BPTree<Integer> tr = new BPTree<Integer>(2, 2);
//        int temp = 0;
//        for (int i = 0; i < 100; i++) {
//            tr.insertNode((int) (Math.random() * 100), i + "");
//            tr.print();
//            System.out.println("==========================================");
//        }
//        tr.delete(2);
//        tr.print();
//        t.insertNode(12, "15");
//        t.insertNode(1, "5");
//        t.insertNode(11, "7");
//        t.insertNode(0, "16");
//        t.insertNode(7, "8");
//        t.insertNode(14, "18");
//        t.insertNode(6, "19");
//        t.insertNode(13, "2");
//        t.insertNode(10, "9");
//        t.insertNode(3, "10");
//        t.insertNode(12, "11");
//        t.insertNode(0, "100");
//        t.insertNode(7, "36");
//        t.delete(2);
        // t.insertNode(67, "67");
        // t.insertNode(77, "77");
        // t.insertNode(23, "23");
        // t.insertNode(35, "35");
        // t.insertNode(57, "57");
        // t.insertNode(63, "63");
        // t.insertNode(84, "84");
        // t.insertNode(23, "23");
//        t.print();

    }
}
