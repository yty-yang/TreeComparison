package treetest;

public class AVLNode {
    public int key; // 结点的键，用于标识结点在AVL树中的位置
    public Object value; // 结点的值，用于存储数据
    public int height; // 结点的高度
    public AVLNode left; // 结点的左指针
    public AVLNode right; // 结点的右指针

    public AVLNode(int key, Object value) {
        this.key = key;
        this.value = value;
        height = 1;
    }
}
