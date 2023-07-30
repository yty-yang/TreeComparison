package treetest;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 红黑树
 */
public class RedBlackTree {
    //根节点
    private RBtreeNode root;


    /**
     * 插入节点
     * @param data
     * @return
     */
    public RBtreeNode insert(int data){
        RBtreeNode insert = new RBtreeNode(data);
        if(root == null){
            root = insert;
            setBlack(insert);
        }else{
            RBtreeNode parent = null;
            RBtreeNode node = root;
            while(node != null){
                parent = node;
                if(node.getData() >= data){
                    //左子树
                    node = node.getLeft();
                }else{
                    //右子树
                    node = node.getRight();
                }
            }

            //跳出循环则说明找到插入位置
            if(parent.getData() >= data){
                parent.setLeft(insert);
            }else{
                parent.setRight(insert);
            }
            insert.setParent(parent);

            //旋转和调整节点颜色保持红黑树平衡
            insertFix(insert);
        }
        return insert;
    }

    /**
     * 旋转和调整节点颜色保持红黑树平衡
     * @param node 插入节点
     */
    private void insertFix(RBtreeNode node) {
        while(node.getParent() != null && isRed(node.getParent())){
            RBtreeNode parent = node.getParent();
            RBtreeNode grandFather = parent.getParent();
            if(grandFather.getLeft() == parent){
                //F为G左儿子的情况
                RBtreeNode uncle = grandFather.getRight();
                if(uncle != null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if(parent.getRight() == node){
                    //插入节点为父节点的右子树

                    //左旋
                    leftRotate(parent);

                    //将旋转后的parent看作插入节点
                    RBtreeNode tmp = node;
                    node = parent;
                    parent = tmp;
                }
                setBlack(parent);
                setRed(grandFather);
                rightRotate(grandFather);
                break;
            }else{
                //F为G的右儿子的情况，对称操作
                RBtreeNode uncle = grandFather.getLeft();
                if(uncle != null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if(parent.getLeft() == node){
                    //插入位置为父节点的左子树

                    //右旋
                    rightRotate(parent);

                    RBtreeNode tmp = node;
                    node = parent;
                    parent = tmp;
                }
                setBlack(parent);
                setRed(grandFather);
                leftRotate(grandFather);
                break;
            }
        }
        setBlack(root);
    }

    /**
     * 删除节点
     * @param data
     * @return
     */
    public RBtreeNode delete(int data){
        RBtreeNode node = query(data);
        if(node == null){
            return null;
        }
        deleteNode(node);
        return node;
    }

    /**
     * 查询节点
     * @param data
     * @return
     */
    public RBtreeNode query(int data){
        if(root == null){
            return null;
        }
        RBtreeNode node = root;
        while(node != null){
            if(node.getData() == data){
                return node;
            }else if(node.getData() >= data){
                node = node.getLeft();
            }else {
                node = node.getRight();
            }
        }
        return null;
    }

    private void deleteNode(RBtreeNode node) {
        if (node == null){
            return;
        }
        //替换节点
        RBtreeNode replaceNode = null;
        if(node.getLeft() != null && node.getRight() != null){
            //存在左右子树
            RBtreeNode tmp = node.getRight();
            while(tmp != null){
                replaceNode = tmp;
                tmp = tmp.getLeft();
            }
            //将替换节点的值放到原本需要删除的节点
            node.setData(replaceNode.getData());
            //删除替换节点
            node = replaceNode;
        }

        if(node.getLeft() != null){
            replaceNode = node.getLeft();
        }else{
            replaceNode = node.getRight();
        }
        RBtreeNode parent = node.getParent();
        if(parent == null){
            root = replaceNode;
            if(replaceNode != null){
                replaceNode.setParent(null);
            }
        }else{
            if(parent.getLeft() == node){
                parent.setLeft(replaceNode);
            }else{
                parent.setRight(replaceNode);
            }
            if(replaceNode != null){
                replaceNode.setParent(parent);
            }
        }
        if(isBlack(node)){
            //replaceNode为了保持平衡，多了一个黑色，需修复
            removeFix(parent, replaceNode);
        }
    }

    /**
     * 修复
     * @param parent
     * @param node 多了一个黑色
     */
    private void removeFix(RBtreeNode parent, RBtreeNode node) {
        while(isBlack(node) && node != root){
            if(parent.getLeft() == node){
                //S是P的左儿子
                RBtreeNode brother = parent.getRight();
                if(isRed(brother)){
                    setBlack(brother);
                    setRed(parent);
                    leftRotate(parent);
                    brother = parent.getRight();
                }
                if(brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))){
                    setRed(brother);
                    node = parent;
                    parent = node.getParent();
                    continue;
                }
                if(isRed(brother.getLeft()) && isBlack(brother.getRight())){
                    setRed(brother);
                    setBlack(brother.getLeft());
                    rightRotate(brother);
                    brother = parent.getRight();
                }
                brother.setColor(parent.isColor());
                setBlack(parent);
                setBlack(brother.getRight());
                leftRotate(parent);
                node = root;
            }else{
                //S是P的右儿子
                RBtreeNode brother = parent.getLeft();
                if(isRed(brother)){
                    setBlack(brother);
                    setRed(parent);
                    rightRotate(parent);
                    brother = parent.getLeft();
                }
                if(brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))){
                    setRed(brother);
                    node = parent;
                    parent = node.getParent();
                    continue;
                }
                if(isRed(brother.getRight()) && isBlack(brother.getLeft())){
                    setBlack(brother.getRight());
                    setRed(brother);
                    leftRotate(brother);
                    brother = parent.getLeft();
                }
                brother.setColor(parent.isColor());
                setBlack(parent);
                setBlack(brother.getLeft());
                rightRotate(parent);
                node = root;
            }
        }
        if(node != null){
            setBlack(node);
        }
    }

    /**
     * 左旋
     * @param node
     */
    private void leftRotate(RBtreeNode node) {
        RBtreeNode right = node.getRight();
        RBtreeNode parent = node.getParent();

        node.setRight(right.getLeft());
        if(right.getLeft() != null){
            right.getLeft().setParent(node);
        }
        node.setParent(right);

        right.setLeft(node);
        if(parent == null){
            root = right;
            right.setParent(null);
        }else{
            right.setParent(parent);
            if(parent.getLeft() != null && parent.getLeft() == node){
                parent.setLeft(right);
            }else{
                parent.setRight(right);
            }
        }
    }

    /**
     * 右旋
     * @param node
     */
    private void rightRotate(RBtreeNode node) {
        RBtreeNode left = node.getLeft();
        RBtreeNode parent = node.getParent();

        node.setLeft(left.getRight());
        if(left.getRight() != null){
            left.getRight().setParent(node);
        }
        node.setParent(left);

        left.setRight(node);
        if(parent == null){
            root = left;
            left.setParent(null);
        }else{
            left.setParent(parent);
            if(parent.getLeft() != null && parent.getLeft() == node){
                parent.setLeft(left);
            }else{
                parent.setRight(left);
            }
        }
    }

    /**
     * 设置颜色为黑
     * @param node
     */
    public static void setBlack(RBtreeNode node) {
        if(node == null){
            return;
        }else{
            node.setColor(RBtreeNode.Black);
        }
    }

    /**
     * 设置颜色为红
     * @param node
     */
    public static void setRed(RBtreeNode node) {
        if(node == null){
            return;
        }else{
            node.setColor(RBtreeNode.Red);
        }
    }

    /**
     * 是否是黑色节点
     * @param node
     * @return
     */
    public static  boolean isBlack(RBtreeNode node){
        if(node == null){
            return true;
        }else{
            return node.isColor() == RBtreeNode.Black;
        }
    }

    /**
     * 是否是红色节点
     * @param node
     * @return
     */
    public static  boolean isRed(RBtreeNode node){
        if(node == null){
            return false;
        }else{
            return node.isColor() == RBtreeNode.Red;
        }
    }

    /**
     * 层级遍历
     * @param root
     */
    public static void levelTraversal(RBtreeNode root){
        if(root == null){
            return;
        }
        Queue<RBtreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()){
            RBtreeNode poll = queue.poll();
            String color = "Black";
            if(isRed(poll)){
                color = "Red";
            }
            System.out.print(poll.getData()+"(" + color + ") ");
            if(poll.getLeft() != null){
                queue.offer(poll.getLeft());
            }
            if(poll.getRight() != null){
                queue.offer(poll.getRight());
            }
        }
    }

    /**
     * 前序遍历
     * @param node
     */
    public static void recursivelyPreTraversal(RBtreeNode node){
        if(node == null){
            return;
        }
        String color = "Black";
        if(isRed(node)){
            color = "Red";
        }
        System.out.print(node.getData()+"(" + color + ") ");
        recursivelyPreTraversal(node.getLeft());
        recursivelyPreTraversal(node.getRight());
    }

    /**
     * 中序遍历
     * @param node
     */
    public static void recursivelyInTraversal(RBtreeNode node){
        if(node == null){
            return;
        }
        recursivelyInTraversal(node.getLeft());
        String color = "Black";
        if(isRed(node)){
            color = "Red";
        }
        System.out.print(node.getData()+"(" + color + ") ");
        recursivelyInTraversal(node.getRight());
    }

    /**
     * 后序遍历
     * @param node
     */
    public static void recursivelyPostTraversal(RBtreeNode node){
        if(node == null){
            return;
        }
        recursivelyPostTraversal(node.getLeft());
        recursivelyPostTraversal(node.getRight());
        String color = "Black";
        if(isRed(node)){
            color = "Red";
        }
        System.out.print(node.getData()+"(" + color + ") ");
    }
}

