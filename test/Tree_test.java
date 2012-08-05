/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Source.Node;
import Source.BPTree;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author DELL
 */
public class Tree_test {

    // TODO add test methods here.
    BPTree<Integer> tr = new BPTree<Integer>(3, 3, 3);

    @Test
    public void testTree() {
        String s = "";
        System.out.println("Testing for tree");
        tr.insertNode(3, "3");
        tr.insertNode(5, "5");
        tr.insertNode(15, "15");
        tr.insertNode(27, "27");
        tr.insertNode(30, "30");
        tr.insertNode(45, "45");
        tr.insertNode(22, "22");
        tr.insertNode(17, "17");
        tr.insertNode(19, "19");
        tr.insertNode(20, "20");
        tr.insertNode(28, "28");
        tr.insertNode(29, "29");
        tr.insertNode(40, "40");
        tr.insertNode(47, "47");
        tr.insertNode(23, "23");
        tr.insertNode(24, "24");
        tr.insertNode(100, "100");
        tr.insertNode(77, "77");
        tr.insertNode(60, "60");
        tr.insertNode(36, "36");
        tr.insertNode(37, "37");
        s = tr.print();
        assertEquals(s, "22 30 \n15 19 \n24 28 \n37 45 77 \n3 5 \n15 17 \n19 20 \n22 23 \n24 27 \n28 29 \n30 36 \n37 40 \n45 47 60 \n77 100 \n");
        Node<Integer> n = tr.search(10);
        assertEquals(n, null);
        n = tr.search(105);
        assertEquals(n, null);
        n = tr.search(1);
        assertEquals(n, null);
        n = tr.search(2);
        assertEquals(n, null);
        n = tr.search(3);
        int found = n.getKeys().get(0);
        assertEquals(found, 3);
        n = tr.search(100);
        found = n.getKeys().get(1);
        assertEquals(found, 100);
        n = tr.search(47);
        found = n.getKeys().get(1);
        assertEquals(found, 47);
        n = tr.search(40);
        found = n.getKeys().get(1);
        assertEquals(found, 40);
        tr.delete(37);
        s = tr.print();
        assertEquals(s, "22 30 \n15 19 \n24 28 \n40 47 77 \n3 5 \n15 17 \n19 20 \n22 23 \n24 27 \n28 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        tr.delete(20);
        s = tr.print();
        assertEquals(s, "22 30 \n15 \n24 28 \n40 47 77 \n3 5 \n15 17 19 \n22 23 \n24 27 \n28 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        tr.delete(22);
        s = tr.print();
        assertEquals(s, "22 30 \n15 \n28 \n40 47 77 \n3 5 \n15 17 19 \n23 24 27 \n28 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        n = tr.search(22);
        assertEquals(n, null);
        tr.delete(28);
        s = tr.print();
        assertEquals(s, "22 30 \n15 \n27 \n40 47 77 \n3 5 \n15 17 19 \n23 24 \n27 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        tr.delete(15);
        s = tr.print();
        assertEquals(s, "22 30 \n17 \n27 \n40 47 77 \n3 5 \n17 19 \n23 24 \n27 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        tr.delete(19);
        s = tr.print();
        assertEquals(s, "30 \n22 27 \n40 47 77 \n3 5 17 \n23 24 \n27 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        tr.delete(27);
        s = tr.print();
        assertEquals(s, "30 \n22 \n40 47 77 \n3 5 17 \n23 24 29 \n30 36 \n40 45 \n47 60 \n77 100 \n");
        n = tr.search(27);
        assertEquals(n, null);
        tr.delete(47);
         s = tr.print();
        assertEquals(s, "30 \n22 \n40 60 \n3 5 17 \n23 24 29 \n30 36 \n40 45 \n60 77 100 \n");
        tr.delete(40);
         s = tr.print();
        assertEquals(s, "30 \n22 \n45 77 \n3 5 17 \n23 24 29 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(23);
        s = tr.print();
        assertEquals(s, "30 \n22 \n45 77 \n3 5 17 \n24 29 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(17);
        s = tr.print();
        assertEquals(s, "30 \n22 \n45 77 \n3 5 \n24 29 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(5);
         s = tr.print();
        assertEquals(s, "45 \n30 \n77 \n3 24 29 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(3);
         s = tr.print();
        assertEquals(s, "45 \n30 \n77 \n24 29 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(24);
         s = tr.print();
        assertEquals(s, "45 77 \n29 30 36 \n45 60 \n77 100 \n");
        tr.delete(29);
         s = tr.print();
        assertEquals(s, "45 77 \n30 36 \n45 60 \n77 100 \n");
        tr.delete(30);
        s = tr.print();
        assertEquals(s, "77 \n36 45 60 \n77 100 \n");
        tr.delete(36);
         s = tr.print();
        assertEquals(s, "77 \n45 60 \n77 100 \n");
        tr.delete(45);
        s = tr.print();
        assertEquals(s, "60 77 100 \n");
        tr.delete(60);
        s = tr.print();
        assertEquals(s, "77 100 \n");
        tr.delete(77);
        s = tr.print();
        assertEquals(s, "100 \n");
        tr.delete(100);
        s = tr.print();
        assertEquals(s, "\n");

    }
}
