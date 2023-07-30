package treetest;

import java.util.*;

public class TreeTest {
    static AVLTree avl = new AVLTree();
    static RedBlackTree rbt = new RedBlackTree();
    static BTree bt4 = new BTree(4);
    static BTree bt10 = new BTree(10);
    static BTree bt100 = new BTree(100);
    static BPlusTree bpt4 = new BPlusTree(4);
    static BPlusTree bpt10 = new BPlusTree(10);
    static BPlusTree bpt100 = new BPlusTree(100);
    static List<Integer> testset;
    static long t_avl, t_rbt, t_bt4, t_bt10, t_bt100, t_bpt4, t_bpt10, t_bpt100; //time
    static List<Long> sortlist;
    static Map<Long, String> sortmap;
    static int testsize;
    static int repeat;
    static Map<String, Double> averagetime_insert;
    static Map<String, Double> averagetime_search;
    static List<String> name;

    private static List<Integer> randomnum(int n) {
        Random random = new Random();
        List<Integer> list = new ArrayList<>(n);
        int rnum;
        for (int i = 0; i < n; i++) {
            rnum = random.nextInt();
            list.add(rnum);
        }

        return list;
    }

    private static void initrank() {
        averagetime_insert = new HashMap<>(8);
        averagetime_search = new HashMap<>(8);

        for (String i: name) {
            averagetime_insert.put(i, (double) 0);

            averagetime_search.put(i, (double) 0);
        }
    }

    private static void insert(int i) {
        long stime, etime;

        stime = System.nanoTime();
        AVLNode AVLnode;
        for (int iterator : testset) {
            AVLnode = new AVLNode(iterator, 0);
            avl.insert(AVLnode);
        }
        etime = System.nanoTime();
        t_avl = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to AVL tree:             " + t_avl + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            rbt.insert(iterator);
        }
        etime = System.nanoTime();
        t_rbt = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to RBtree:               " + t_rbt + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt4.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B-tree of order 4:    " + t_bt4 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt10.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B-tree of order 10:   " + t_bt10 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt100.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt100 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B-tree of order 100:  " + t_bt100 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt4.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 4:   " + t_bpt4 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt10.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 10:  " + t_bpt10 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt100.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt100 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 100: " + t_bpt100 + " ns");
        }
    }

    private static void insertsort(int i) {
        if (i == repeat - 1) {
            System.out.println("fast <------------insert------------> slow");
        }
        sortinit();
        averagetime(i);
    }

    private static void search(int i) {
        Random random = new Random();
        int searchnum = random.nextInt(testsize);
        long stime, etime;

        stime = System.nanoTime();
        avl.get(searchnum);
        etime = System.nanoTime();
        t_avl = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in AVL tree:            " + t_avl + " ns");
        }

        stime = System.nanoTime();
        rbt.query(searchnum);
        etime = System.nanoTime();
        t_rbt = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in RBtree:              " + t_rbt + " ns");
        }

        stime = System.nanoTime();
        bt4.search(searchnum);
        etime = System.nanoTime();
        t_bt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B-tree of order 4:   " + t_bt4 + " ns");
        }

        stime = System.nanoTime();
        bt10.search(searchnum);
        etime = System.nanoTime();
        t_bt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B-tree of order 10:  " + t_bt10 + " ns");
        }

        stime = System.nanoTime();
        bt100.search(searchnum);
        etime = System.nanoTime();
        t_bt100 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B-tree of order 100: " + t_bt100 + " ns");
        }

        stime = System.nanoTime();
        bpt4.search(searchnum);
        etime = System.nanoTime();
        t_bpt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B+tree of order 4:   " + t_bpt4 + " ns");
        }

        stime = System.nanoTime();
        bpt10.search(searchnum);
        etime = System.nanoTime();
        t_bpt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B+tree of order 10:   " + t_bpt10 + " ns");
        }

        stime = System.nanoTime();
        bpt100.search(searchnum);
        etime = System.nanoTime();
        t_bpt100 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B+tree of order 100: " + t_bpt100 + " ns");
        }
    }

    private static void searchsort(int i) {
        if (i == repeat - 1) {
            System.out.println("fast <------------search------------> slow");
        }
        sortinit();
        averagetime(i);
    }

    private static void sortinit() {
        sortlist = new ArrayList<>(8);

        sortlist.add(t_avl);
        sortlist.add(t_rbt);
        sortlist.add(t_bt4);
        sortlist.add(t_bt10);
        sortlist.add(t_bt100);
        sortlist.add(t_bpt4);
        sortlist.add(t_bpt10);
        sortlist.add(t_bpt100);

        sortmap = new HashMap<>(8);

        sortmap.put(t_avl, "AVL");
        sortmap.put(t_rbt, "RBt");
        sortmap.put(t_bt4, "B(4)");
        sortmap.put(t_bt10, "B(10)");
        sortmap.put(t_bt100, "B(100)");
        sortmap.put(t_bpt4, "B+(4)");
        sortmap.put(t_bpt10, "B+(10)");
        sortmap.put(t_bpt100, "B+(100)");

        Collections.sort(sortlist);
    }

    private static void averagetime(int i) {
        String name;

        for (long iterator : sortlist) {
            name = sortmap.get(iterator);

            if (i == repeat - 1) {
                System.out.print(name + ' ');
            }
        }
        if (i == repeat - 1) {
            System.out.println();
        }
    }

    private static void totaltime_insert() {
        Double total;

        total = averagetime_insert.get("AVL") + t_avl;
        averagetime_insert.replace("AVL", total);

        total = averagetime_insert.get("RBt") + t_rbt;
        averagetime_insert.replace("RBt", total);

        total = averagetime_insert.get("B(4)") + t_bt4;
        averagetime_insert.replace("B(4)", total);

        total = averagetime_insert.get("B(10)") + t_bt10;
        averagetime_insert.replace("B(10)", total);

        total = averagetime_insert.get("B(100)") + t_bt100;
        averagetime_insert.replace("B(100)", total);

        total = averagetime_insert.get("B+(4)") + t_bpt4;
        averagetime_insert.replace("B+(4)", total);

        total = averagetime_insert.get("B+(10)") + t_bpt10;
        averagetime_insert.replace("B+(10)", total);

        total = averagetime_insert.get("B+(100)") + t_bpt100;
        averagetime_insert.replace("B+(100)", total);
    }

    private static void totaltime_search() {
        Double total;

        total = averagetime_search.get("AVL") + t_avl;
        averagetime_search.replace("AVL", total);

        total = averagetime_search.get("RBt") + t_rbt;
        averagetime_search.replace("RBt", total);

        total = averagetime_search.get("B(4)") + t_bt4;
        averagetime_search.replace("B(4)", total);

        total = averagetime_search.get("B(10)") + t_bt10;
        averagetime_search.replace("B(10)", total);

        total = averagetime_search.get("B(100)") + t_bt100;
        averagetime_search.replace("B(100)", total);

        total = averagetime_search.get("B+(4)") + t_bpt4;
        averagetime_search.replace("B+(4)", total);

        total = averagetime_search.get("B+(10)") + t_bpt10;
        averagetime_search.replace("B+(10)", total);

        total = averagetime_search.get("B+(100)") + t_bpt100;
        averagetime_search.replace("B+(100)", total);
    }

    private static void timetoaverage() {
        for (String i: name) {
            averagetime_insert.replace(i, averagetime_insert.get(i) / repeat);

            averagetime_search.replace(i, averagetime_insert.get(i) / repeat);
        }
    }

    private static void test() {
        name = new ArrayList<>(8);
        name.add("AVL");
        name.add("RBt");
        name.add("B(4)");
        name.add("B(10)");
        name.add("B(100)");
        name.add("B+(4)");
        name.add("B+(10)");
        name.add("B+(100)");

        repeat = 100;

        for (testsize = 10; testsize <= 1000000; testsize *= 10) {
            initrank();

            if (testsize == 100000) {
                repeat = 20;
            }
            if (testsize == 1000000) {
                repeat = 2;
            }

            for (int i = 0; i < repeat; i++) {
                testset = randomnum(testsize);
                insert(i);
                totaltime_insert();
                insertsort(i);
                search(i);
                totaltime_search();
                searchsort(i);
            }

            timetoaverage();
            System.out.println();
            System.out.println("averagetime_insert:");
            System.out.println(averagetime_insert.toString());
            System.out.println("averagetime_search:");
            System.out.println(averagetime_search.toString());
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }

    public static void main(String[] Args) {
        test();
    }
}
