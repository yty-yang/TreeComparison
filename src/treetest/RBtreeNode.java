package treetest;

public class RBtreeNode {
    public static boolean Red = false;
    public static boolean Black = true;
    //节点颜色
    private boolean color;
    private int data;
    private RBtreeNode left;
    private RBtreeNode right;
    private RBtreeNode parent;

    public RBtreeNode(int data){
        this.data = data;
        color = Red;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public RBtreeNode getLeft() {
        return left;
    }

    public void setLeft(RBtreeNode left) {
        this.left = left;
    }

    public RBtreeNode getRight() {
        return right;
    }

    public void setRight(RBtreeNode right) {
        this.right = right;
    }

    public RBtreeNode getParent() {
        return parent;
    }

    public void setParent(RBtreeNode parent) {
        this.parent = parent;
    }
}
