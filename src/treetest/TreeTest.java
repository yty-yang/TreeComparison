package treetest;

import java.util.*;

public class TreeTest {
    static AVLTree avl = new AVLTree();
    static RedBlackTree rbt = new RedBlackTree();
    static BTree bt3 = new BTree(3);
    static BTree bt4 = new BTree(4);
    static BTree bt10 = new BTree(10);
    static BPlusTree bpt3 = new BPlusTree(3);
    static BPlusTree bpt4 = new BPlusTree(4);
    static BPlusTree bpt10 = new BPlusTree(10);
    static List<Integer> testset;
    static long t_avl, t_rbt, t_bt3, t_bt4, t_bt10, t_bpt3, t_bpt4, t_bpt10; //time
    static List<Long> sortlist;
    static Map<Long, String> sortmap;
    static int testsize;
    static int repeat;
    static Map<String, Double> averagerank_insert;
    static Map<String, Double> averagerank_search;

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
        averagerank_insert = new HashMap<>(8);

        averagerank_insert.put("AVL", (double) 0);
        averagerank_insert.put("RBt", (double) 0);
        averagerank_insert.put("B(3)", (double) 0);
        averagerank_insert.put("B(4)", (double) 0);
        averagerank_insert.put("B(10)", (double) 0);
        averagerank_insert.put("B+(3)", (double) 0);
        averagerank_insert.put("B+(4)", (double) 0);
        averagerank_insert.put("B+(10)", (double) 0);

        averagerank_search = new HashMap<>(8);

        averagerank_search.put("AVL", (double) 0);
        averagerank_search.put("RBt", (double) 0);
        averagerank_search.put("B(3)", (double) 0);
        averagerank_search.put("B(4)", (double) 0);
        averagerank_search.put("B(10)", (double) 0);
        averagerank_search.put("B+(3)", (double) 0);
        averagerank_search.put("B+(4)", (double) 0);
        averagerank_search.put("B+(10)", (double) 0);
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
            System.out.println("insert " + testsize + " nodes to AVL tree:            " + t_avl + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            rbt.insert(iterator);
        }
        etime = System.nanoTime();
        t_rbt = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to RBtree:              " + t_rbt + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt3.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt3 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to 2-3 tree:            " + t_bt3 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt4.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to 2-3-4 tree:          " + t_bt4 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bt10.insert(iterator);
        }
        etime = System.nanoTime();
        t_bt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B-tree of order 10:  " + t_bt10 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt3.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt3 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 3:  " + t_bpt3 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt4.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 4:  " + t_bpt4 + " ns");
        }

        stime = System.nanoTime();
        for (int iterator : testset) {
            bpt10.insert(iterator, 0);
        }
        etime = System.nanoTime();
        t_bpt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("insert " + testsize + " nodes to B+ tree of order 10: " + t_bpt10 + " ns");
        }
    }

    private static void insertsort(int i) {
        if (i == repeat - 1) {
            System.out.println("fast <------------insert------------> slow");
        }
        sortinit();
        rank(i, averagerank_insert);
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
        bt3.search(searchnum);
        etime = System.nanoTime();
        t_bt3 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in 2-3 tree:            " + t_bt3 + " ns");
        }

        stime = System.nanoTime();
        bt4.search(searchnum);
        etime = System.nanoTime();
        t_bt4 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in 2-3-4 tree:          " + t_bt4 + " ns");
        }

        stime = System.nanoTime();
        bt10.search(searchnum);
        etime = System.nanoTime();
        t_bt10 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B-tree of order 10:  " + t_bt10 + " ns");
        }

        stime = System.nanoTime();
        bpt3.search(searchnum);
        etime = System.nanoTime();
        t_bpt3 = etime - stime;
        if (i == repeat - 1) {
            System.out.println("search in B+tree of order 3:   " + t_bpt3 + " ns");
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
            System.out.println("search in B+tree of order 10:  " + t_bpt10 + " ns");
        }
    }

    private static void searchsort(int i) {
        if (i == repeat - 1) {
            System.out.println("fast <------------search------------> slow");
        }
        sortinit();
        rank(i, averagerank_search);
    }

    private static void sortinit() {
        sortlist = new ArrayList<>(8);

        sortlist.add(t_avl);
        sortlist.add(t_rbt);
        sortlist.add(t_bt3);
        sortlist.add(t_bt4);
        sortlist.add(t_bt10);
        sortlist.add(t_bpt3);
        sortlist.add(t_bpt4);
        sortlist.add(t_bpt10);

        sortmap = new HashMap<>(8);

        sortmap.put(t_avl, "AVL");
        sortmap.put(t_rbt, "RBt");
        sortmap.put(t_bt3, "B(3)");
        sortmap.put(t_bt4, "B(4)");
        sortmap.put(t_bt10, "B(10)");
        sortmap.put(t_bpt3, "B+(3)");
        sortmap.put(t_bpt4, "B+(4)");
        sortmap.put(t_bpt10, "B+(10)");

        Collections.sort(sortlist);
    }

    private static void rank(int i, Map<String, Double> averagerank) {
        String name;
        int rank = 0;
        for (long iterator : sortlist) {
            name = sortmap.get(iterator);
            rank++;
            double currentrank = averagerank.get(name);
            currentrank *= i;
            currentrank += rank;
            currentrank /= i + 1;
            averagerank.replace(name, currentrank);

            if (i == repeat - 1) {
                System.out.print(name + ' ');
            }
        }
        if (i == repeat - 1) {
            System.out.println();
        }
    }

    public static void main(String[] Args) {
        repeat = 100;

        for (testsize = 10; testsize <= 1000000; testsize *= 10) {
            initrank();

            if (testsize == 100000) {
                repeat = 20;
            }
            if (testsize == 1000000) {
                repeat = 10;
            }

            for (int i = 0; i < repeat; i++) {
                testset = randomnum(testsize);
                insert(i);
                insertsort(i);
                search(i);
                searchsort(i);
            }

            System.out.println();
            System.out.println("averagerank_insert:");
            System.out.println(averagerank_insert.toString());
            System.out.println("averagerank_search:");
            System.out.println(averagerank_search.toString());
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }
}
