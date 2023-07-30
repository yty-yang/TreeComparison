package treetest;

import java.util.LinkedList;

/**
 * B树 BTree<br/>
 * <br/>
 * BTree是完全平衡多叉查找树，所以不能继承BST；<br/>
 * 并且BTree是混合多叉树，因此不宜继承MyAbstractTree或实现MyTree接口<br/>
 * 当然，为了解耦性在此单独定义类<br/>
 * <br/>
 * B树的定义(以m阶B树为例，通常m为大于3的偶数，因此B树的度数t(即m/2)为大于等于2的自然数)：
 * 	1.每个结点x有下面属性：
 * 		a x.n表示当前存储在结点x的关键字个数
 * 		b x.n个关键字本身以非降序存放(即关键字可重复，一般就是严格升序存放)
 * 		c x.leaf表示结点x是否叶结点(布尔类型)
 * 	2.每个内部结点x(有子结点的结点)还包含(x.n+1)个指向其子结点的指针p(i)(i为1...n+1)，叶结点的p(i)为null；
 * 	    (也就是说内部结点x必须有(x.n+1)个子结点)
 * 	3.关键字x.key(i)(i为1...n)对存储在各子结点中的关键字范围加以分割：
 * 		第i个子结点的关键字总是大于(等于)其父结点的第i-1个关键字而小于(等于)其父结点的第i个关键字
 * 	4.每个叶结点具有相同的深度，即树的高度h；
 * 	5.每个结点的关键字个数有上界和下界；
 * 		a 除根结点外每个结点(包括叶结点)至少m/2-1(向上取整)个关键字；如果树非空，则根结点至少1个关键字；
 * 		b 每个结点至多m-1个关键字(即至多m个子结点)
 */
public class BTree<K extends Comparable<K>> {
    private int degree = 2; // 度数，取以2开始的自然数
    private int order = 2 * degree; // 阶数，通常取偶数
    private int max = order - 1; // 关键字个数上界
    private int min = (int) Math.ceil(order / 2.0) - 1; // 关键字个数下界，因为阶数是偶数所以其实就是degree-1
    private BTreeNode<K> root = new BTreeNode<>(); // 根结点。树都是由1个根结点构成，所有其它结点都直接或间接被根结点指向
    private int size; // 树的大小(即关键字个数)

    public BTree() {

    }

    public BTree(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public BTreeNode<K> getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    /**
     * 查找
     * @param key
     * @return
     */
    public Result<K> search(K key) {
        return search(root, key);
    }

    private Result<K> search(BTreeNode<K> root, K key) {
        if (root == null || key == null) {
            return null;
        }
        int i = 0;
        while (i < root.n && key.compareTo(root.getKey(i)) > 0) { // 注意：数组下标从0开始
            i++;
        }
        if (i < root.n && key.compareTo(root.getKey(i)) == 0) { // 在结点中找到关键字
            return new Result<>(root, i);
        } else if (root.leaf) { // 遍历完毕未找到关键字
            return null;
        } else { // 在子结点中查找关键字
            // 注意：BTree的实际应用中，此处是需要先将子结点关键字从磁盘读取到主存
            return search(root.getNode(i), key);
        }
    }

    /**
     * 插入
     * @param key
     * @return
     */
    public boolean insert(K key) {
        boolean result = insert(root, key);
        if (result) {
            size++;
        }
        return result;
    }

    private boolean insert(BTreeNode<K> root, K key) {
        if (root == null || key == null) {
            return false;
        }
        if (root.n == max) { // 根结点为满而需要分裂的特殊情形
            BTreeNode<K> newNode = new BTreeNode<>(); // 新的根结点，高度增加
            this.root = newNode;
            newNode.addNode(root);
            newNode.leaf = false; // 因为子结点列表nodeList有元素，所以不是叶结点
            split(newNode, 0);
            return insertWithoutFull(newNode, key);
        } else {
            return insertWithoutFull(root, key);
        }
    }

    /**
     * 本方法是指向以非满结点参数root为根的树插入关键字key<br/>
     * 并不是指参数root为根的树其所有结点都非满
     * @param root
     * @param key
     * @return
     */
    private boolean insertWithoutFull(BTreeNode<K> root, K key) {
        if (root == null || key == null) {
            return false;
        }
        int i = root.n - 1; // 关键字下标
        if (root.leaf) { // 注意：存在子结点为满的结点必须先分裂，所以必然是插入到分解后的叶结点
            while (i >= 0 && key.compareTo(root.getKey(i)) < 0) {
                i--;
            }
            if (i < root.n - 1) {
                root.addKey(i + 1, key);
            } else {
                root.addKey(key);
            }
            root.n++;
            // 注意：BTree的实际应用中，此处需要将root的关键字从主存写入到磁盘
            return true;
        } else {
            while (i >= 0 && key.compareTo(root.getKey(i)) < 0) {
                i--;
            }
            i++; // 因为子结点个数比关键字个数多1
            // 注意：BTree的实际应用中，此处需要将root的下标为i的子结点的关键字从磁盘读取到主存
            if (root.getNode(i).n == max) { // root结点需要分裂，root结点的该子结点是满的，并不是指root是满的
                split(root, i);
                if (key.compareTo(root.getKey(i)) > 0) { // 此步不要遗漏
                    i++;
                }
            }
            return insertWithoutFull(root.getNode(i), key);
        }
    }

    /**
     * 分裂<br/>
     * <br/>
     * 分裂其实有不同算法，自顶而下和自底而上；<br/>
     * 本方法采用自顶而下算法，即每次插入新关键字时，沿着树向下查找所属位置，<br/>
     *      依次分裂沿途每个满结点(包括叶结点)，然后插入该关键字；<br/>
     * 自底而上算法稍微复杂点，它是在找到新关键字所属位置(叶结点)后分裂该叶结点(满的)并插入关键字，<br/>
     *      因为每次分裂后其父结点关键字数量就会加1，因此继续分裂父结点直至首个非满结点为止；<br/>
     * <br/>
     * 分裂是BTree长高的唯一途径(对根结点分裂是增加BTree高度的唯一途径)
     * @param target 需要分裂的结点
     * @param i 分裂位置，即需要分裂的结点的子结点下标，即该子结点是满的
     */
    private void split(BTreeNode<K> target, int i) { // 注意：总是 i <= target.n
        BTreeNode<K> leftNode = target.getNode(i);
        BTreeNode<K> rightNode = new BTreeNode<>();
        rightNode.leaf = leftNode.leaf;
        rightNode.n = min; // 分裂后的两个子结点的关键字个数其实就是BTree允许的最小关键字个数

        // 处理分裂后的新增子结点以及原子结点
        for (int j = 0; j < min; j++) { // 通常 min = degree - 1
            // 注意：这里不能使用add(i, e)方法添加元素
            rightNode.addKey(leftNode.getKey(j + min + 1));
        }
        if (!leftNode.leaf) {
            for (int j = 0; j < min + 1; j++) {
                rightNode.addNode(leftNode.getNode(j + min + 1));
            }
        }

        // 处理需要分裂的结点
        if (i == target.n) { // target的下标target.n的子结点(即最右边子结点)是满的
            // 关键字及子结点不需要移位
            target.addKey(leftNode.getKey(min));
            target.addNode(rightNode);
        } else { // target的下标i(0,1,2,...,target.n-1)的子结点是满的
            // 关键字及子结点需要移位
            target.addKey(i, leftNode.getKey(min));
            target.addNode(i + 1, rightNode);
        }
        target.n++;

        // 处理分裂后的原子结点
        for (int j = 0; j < min + 1; j++) { // 提升到父结点的关键字也会被删除
            leftNode.removeLastKey();
        }
        if (!leftNode.leaf) {
            for (int j = 0; j < min + 1; j++) {
                leftNode.removeLastNode();
            }
        }
        leftNode.n = min;
        // 注意：BTree的实际应用中，此处需要将target、leftNode、rightNode的关键字从主存写入到磁盘
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public boolean delete(K key) {
        Result<K> result = search(key);
        if (result == null) {
            return false;
        }
        delete(root, key);
        size--;
        return true;
    }

    /**
     * 在参数root为根结点代表的树中删除关键字key(key在树中)
     * @param root
     * @param key
     * @return
     */
    private void delete(BTreeNode<K> root, K key) {
        int i = 0;
        while (i < root.n && key.compareTo(root.getKey(i)) > 0) { // 注意：数组下标从0开始
            i++;
        }
        if (i < root.n && key.compareTo(root.getKey(i)) == 0) { // key在当前结点中(即在当前结点的关键字列表中)
            if (root.leaf) { // 情形1
                root.keyList.remove(i);
                root.n--;
            } else { // 情形2
                BTreeNode<K> leftNode = root.getNode(i);
                BTreeNode<K> rightNode = root.getNode(i + 1);
                if (leftNode.n > min) { // 情形2a
                    K newKey = leftNode.keyList.getLast();
                    root.keyList.remove(i);
                    root.addKey(newKey);
                    delete(leftNode, newKey);
                } else if (rightNode.n > min) { // 情形2b
                    // 情形2b与情形2a是对称情形
                    K newKey = rightNode.keyList.getFirst();
                    root.keyList.remove(i);
                    root.addKey(newKey);
                    delete(rightNode, newKey);
                } else { // 情形2c
                    leftNode.addKey(key);
                    leftNode.n++;
                    for (int j = 0; j < min; j++) {
                        leftNode.addKey(rightNode.getKey(j));
                        leftNode.n++;
                    }
                    if (!leftNode.leaf) {
                        for (int j = 0; j < min + 1; j++) {
                            leftNode.addNode(rightNode.getNode(j));
                        }
                    }
                    root.keyList.remove(i);
                    root.nodeList.remove(i + 1);
                    root.n--;
                    if (root.n == 0) { // 说明入参root是根结点
                        // 树的高度缩减1
                        this.root = leftNode;
                    }
                    delete(leftNode, key);
                }
            }
        }  else { // key在当前结点下标为i的子树中
            BTreeNode<K> childNode = root.getNode(i);
            if (childNode.n == min) { // 情形3
                // 不论key是在childNode中还是在childNode的子树中，总是保证本次迭代后childNode至少min+1个关键字
                if (i > 0 && root.getNode(i - 1).n > min) { // 情形3a
                    // childNode的左邻兄弟结点有可借关键字
                    BTreeNode<K> leftNode = root.getNode(i - 1);
                    childNode.keyList.addFirst(root.getKey(i - 1));
                    childNode.n++;
                    root.keyList.set(i - 1, leftNode.keyList.removeLast());
                    leftNode.n--;
                    if (!childNode.leaf) { // 左邻兄弟结点的最后子结点也要借过去
                        childNode.nodeList.addFirst(leftNode.nodeList.removeLast());
                    }
                    delete(childNode, key);
                } else if (i < root.n && root.getNode(i + 1).n > min) { // 情形3b
                    // childNode的右邻兄弟结点有可借关键字
                    // 情形3b情形3a是对称情形
                    BTreeNode<K> rightNode = root.getNode(i + 1);
                    childNode.keyList.addLast(root.getKey(i));
                    childNode.n++;
                    root.keyList.set(i, rightNode.keyList.removeFirst());
                    rightNode.n--;
                    if (!childNode.leaf) {// 右邻兄弟结点的第一个子结点也要借过去
                        childNode.nodeList.addLast(rightNode.nodeList.removeFirst());
                    }
                    delete(childNode, key);
                } else { // 情形3c
                    // childNode的相邻兄弟结点无可借关键字(注意：肯定有相邻兄弟结点)
                    // 当前结点要么是根结点，要么关键字个数必然大于min
                    // 当前结点不可能是关键字个数为min的非根结点的内部结点，因为前面已经保证了关键字个数至少min+1
                    if (i > 0) { // childNode的左邻兄弟结点无可借关键字
                        BTreeNode<K> leftNode = root.getNode(i - 1);
                        childNode.keyList.addFirst(root.keyList.remove(i - 1));
                        root.n--;
                        childNode.n++;
                        for (int j = min - 1; j >= 0; j--) {
                            childNode.keyList.addFirst(leftNode.keyList.get(j));
                            childNode.n++;
                        }
                        if (!childNode.leaf) {
                            for (int j = min; j >= 0; j--) {
                                childNode.nodeList.addFirst(leftNode.nodeList.get(j));
                            }
                        }
                        root.nodeList.remove(i - 1);
                    } else if (i < root.n) {  // childNode的右邻兄弟结点无可借关键字
                        BTreeNode<K> rightNode = root.getNode(i + 1);
                        childNode.keyList.addLast(root.keyList.remove(i));
                        root.n--;
                        childNode.n++;
                        for (int j = 0; j <= min - 1; j++) {
                            childNode.keyList.addLast(rightNode.keyList.get(j));
                            childNode.n++;
                        }
                        if (!childNode.leaf) {
                            for (int j = 0; j <= min; j++) {
                                childNode.nodeList.addLast(rightNode.nodeList.get(j));
                            }
                        }
                        root.nodeList.remove(i + 1);
                    }
                    if (root.n == 0) { // 说明入参root是根结点
                        // 树的高度缩减1
                        this.root = childNode;
                    }
                    delete(childNode, key);
                }
            } else {
                delete(childNode, key);
            }
        }
    }

    public void widthOrder() {
        widthOrder(root);
    }

    /**
     * 广度遍历
     * @param root
     */
    private void widthOrder(BTreeNode<K> root) {
        throw new UnsupportedOperationException("要想支持广度遍历需要增加结点属性");
    }

    public void preOrder() {
        System.out.println("===前序遍历：===");
        System.out.print("\t");
        preOrder(root);
        System.out.println("\n=====完毕=====");
    }

    /**
     * 前序遍历<br/>
     * <br/>
     * 深度遍历与前序遍历相同，亦即深度遍历
     * @param root
     */
    private void preOrder(BTreeNode<K> root) {
        for (int i = 0; i < root.n; i++) {
            System.out.print(root.getKey(i) + " ");
        }
        if (!root.leaf) {
            for (int i = 0; i <= root.n; i++) {
                preOrder(root.getNode(i));
            }
        }
    }

    public void inOrder() {
        System.out.println("===中序遍历：===");
        System.out.print("\t");
        inOrder(root);
        System.out.println("\n=====完毕=====");
    }

    /**
     * 中序遍历<br/>
     * <br/>
     * 中序遍历可以排序
     * @param root
     */
    private void inOrder(BTreeNode<K> root) {
        boolean isNotLeaf = !root.leaf;
        if (isNotLeaf) {
            inOrder(root.getNode(0));
        }
        for (int i = 0; i < root.n; i++) {
            System.out.print(root.getKey(i) + " ");
            if (isNotLeaf) {
                inOrder(root.getNode(i + 1));
            }
        }
    }

    public void postOrder() {
        System.out.println("===后序遍历：===");
        System.out.print("\t");
        postOrder(root);
        System.out.println("\n=====完毕=====");
    }

    /**
     * 后序遍历
     * @param root
     */
    private void postOrder(BTreeNode<K> root) {
        boolean isNotLeaf = !root.leaf;
        if (isNotLeaf) {
            postOrder(root.getNode(0));
        }
        for (int i = 0; i < root.n; i++) {
            if (isNotLeaf) {
                postOrder(root.getNode(i + 1));
            }
            System.out.print(root.getKey(i) + " ");
        }
    }

    /**
     * 表示关键字位置的类<br/>
     * <br/>
     * 类是public权限但构造方法私有化并且仅提供公有getter方法，是为让其它类仅仅只可以获取该类的属性
     * @param <U>
     */
    public static class Result<U extends Comparable<U>> {
        private BTreeNode<U> node;
        private int index;

        private Result(BTreeNode<U> node, int index) {
            this.node = node;
            this.index = index;
        }

        public BTreeNode<U> getNode() {
            return node;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * BTree的结点类<br/>
     * <br/>
     * 类是public权限但构造方法私有化并且仅提供公有getter方法，是为让其它类仅仅只可以获取该类的属性
     * @param <V>
     */
    public static class BTreeNode<V extends Comparable<V>> {
        private int n; // 关键字个数
        private boolean leaf = true; // 是否叶结点
        private LinkedList<V> keyList = new LinkedList<>(); // 关键字列表
        private LinkedList<BTreeNode<V>> nodeList = new LinkedList<>(); // 子结点列表

        private BTreeNode() {
        }

        public boolean isLeaf() {
            return leaf;
        }

        public LinkedList<V> getKeyList() {
            return keyList;
        }

        public LinkedList<BTreeNode<V>> getNodeList() {
            return nodeList;
        }

        public void addKey(V key) {
            keyList.add(key);
        }

        public void addKey(int index, V key) {
            keyList.add(index, key);
        }

        public void addNode(BTreeNode<V> node) {
            nodeList.add(node);
        }

        public void addNode(int index, BTreeNode<V> node) {
            nodeList.add(index, node);
        }

        public V getKey(int index) {
            return keyList.get(index);
        }

        public BTreeNode<V> getNode(int index) {
            return nodeList.get(index);
        }

        public void removeLastKey() {
            keyList.removeLast();
        }

        public void removeLastNode() {
            nodeList.removeLast();
        }
    }
}
