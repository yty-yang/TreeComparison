package treetest;

public class AVLTree {
    private AVLNode root;
    private final StringBuilder visualizeBuilder; // 二叉树图形化解析的builder

    public AVLTree() {
        visualizeBuilder = new StringBuilder();
    }

    /**
     * 平衡调整
     */
    private AVLNode reBalance(AVLNode avlNode) {
        // 获取avlNode的平衡因子
        int altitudeDiff = getAltitudeDiff(avlNode);

        if (Math.abs(altitudeDiff) > 1) {
            if (altitudeDiff < 0) { // LL旋转或者RL旋转
                // avlNode的右子树的平衡因子只可能为-1|0|1
                if (getAltitudeDiff(avlNode.right) == 1) {
                    // avlNode的右子树右旋
                    avlNode.right = rightRevolve(avlNode.right);
                }
                // avlNode左旋
                return leftRevolve(avlNode);
            } else { // RR旋转或者LR旋转
                // avlNode的左子树的平衡因子只可能为-1|0|1
                if (getAltitudeDiff(avlNode.left) == -1) {
                    // avlNode的左子树左旋
                    avlNode.left = leftRevolve(avlNode.left);
                }
                // avlNode右旋
                return rightRevolve(avlNode);
            }
        }
        return avlNode;
    }

    /**
     * 左旋
     */
    private AVLNode leftRevolve(AVLNode avlNode) {
        // 先保存avlNode的右子树
        AVLNode right = avlNode.right;

        // avlNode的右指针指向right的左子树
        avlNode.right = right.left;

        // right的左指针指向avlNode
        right.left = avlNode;

        // 先更新avlNode的高度
        resetHeight(avlNode);

        // 再更新right的高度
        resetHeight(right);

        // 返回right作为调整后的root
        return right;
    }

    /**
     * 右旋
     */
    private AVLNode rightRevolve(AVLNode avlNode) {
        // 保存avlNode的左子树
        AVLNode left = avlNode.left;

        // avlNode的左指针指向left的右子树
        avlNode.left = left.right;

        // left的右指针指向avlNode
        left.right = avlNode;

        // 先更新avlNode的高度
        resetHeight(avlNode);

        // 再更新left的高度
        resetHeight(left);

        // 返回left作为调整后的root
        return left;
    }

    /**
     * 获取结点高度差（平衡因子）
     */
    private int getAltitudeDiff(AVLNode avlNode) {
        return (avlNode.left != null ? avlNode.left.height : 0) -
                (avlNode.right != null ? avlNode.right.height : 0);
    }

    /**
     * 插入结点
     */
    public void insert(AVLNode newNode) {
        root = insert(newNode, root);
    }

    private AVLNode insert(AVLNode newNode, AVLNode root) {
        if (root == null) {
            // 找到了正确的位置，直接返回新结点
            return newNode;
        }
        if (root.key > newNode.key) {
            // 新结点的key比当前结点的小，因此向当前结点的左子树递归
            root.left = insert(newNode, root.left);
        } else if (root.key < newNode.key) {
            // 新结点的key比当前结点的大，因此向当前结点的右子树递归
            root.right = insert(newNode, root.right);
        } else {
            // 两个结点的key相同，因此直接更新value并返回
            root.value = newNode.value;
            return root;
        }
        resetHeight(root); // 在调整之前，更新当前结点的高度
        return reBalance(root); // 调整并返回
    }

    /**
     * 删除结点
     */
    public boolean delete(int key) {
        if (root == null) {
            return false;
        }

        // delete方法返回null的情况：待删除的是唯一一个结点
        AVLNode delete = delete(key, root);

        // 待删除的结点存在
        if (delete != null || root.left == null && root.right == null && key == root.key) {
            root = delete;
            return true;
        }

        // 待删除的结点不存在
        return false;
    }

    private AVLNode delete(int key, AVLNode avlNode) {
        if (avlNode == null) { // 待删除的结点不存在
            return null;
        }
        if (avlNode.key > key) {
            // 对avlNode的左子树递归调用方法
            avlNode.left = delete(key, avlNode.left);
        } else if (avlNode.key < key) {
            // 对avlNode的右子树递归调用方法
            avlNode.right = delete(key, avlNode.right);
        } else {
            if (avlNode.left == null && avlNode.right == null) { // 待删除的结点没有左右子树
                // 返回空表示删除当前结点（avlNode）
                return null;
            } else if (avlNode.right == null) { // 待删除的结点只有左子树
                // 保存avlNode的左子树
                AVLNode left = avlNode.left;

                // 清空avlNode的左指针
                avlNode.left = null;

                // 用avlNode的左子树接替avlNode
                return left;
            } else if (avlNode.left == null) { // 待删除的结点只有右子树
                // 保存avlNode的右子树
                AVLNode right = avlNode.right;

                // 清空avlNode的右指针
                avlNode.right = null;

                // 用avlNode的右子树接替avlNode
                return right;
            } else { // 待删除的结点有左右子树
                // 获取左子树的最右结点
                AVLNode mostRightNodeOfLeftTree = getMostRightNode(avlNode.left);

                // 删除待删除结点的左子树的最右结点，由于此时最右结点没有右子树，因此只涉及两种返回情况：
                // 1、只有左子树
                // 2、没有左右子树
                // 由于可能会发生旋转，因此newRoot不一定等于avlNode
                AVLNode newRoot = delete(mostRightNodeOfLeftTree.key, avlNode);

                // 将avlNode的数据更新为左子树的最右结点的数据，此时avlNode的高度已在上一步更新
                avlNode.key = mostRightNodeOfLeftTree.key;
                avlNode.value = mostRightNodeOfLeftTree.value;

                // 返回调整之后的以原待删除结点为根结点的AVL树的根结点
                return newRoot;
            }
        }
        resetHeight(avlNode); // 在调整之前，更新当前结点的高度
        return reBalance(avlNode); // 调整并返回
    }

    /**
     * 重新设置给定结点的高度
     */
    private void resetHeight(AVLNode avlNode) {
        avlNode.height = Math.max(
                avlNode.left == null ? 0 : avlNode.left.height,
                avlNode.right == null ? 0 : avlNode.right.height
        ) + 1;
    }

    /**
     * 获取给定结点的最右子结点
     */
    private AVLNode getMostRightNode(AVLNode avlNode) {
        return avlNode.right == null ? avlNode : getMostRightNode(avlNode.right);
    }

    /**
     * 检查AVL树是否平衡
     */
    public boolean checkBalance() {
        if (root != null) {
            if (Math.abs(getAltitudeDiff(root)) > 1) {
                return false;
            }
            return checkBalance(root.left) && checkBalance(root.right);
        }
        return true;
    }

    private boolean checkBalance(AVLNode root) {
        if (root != null) {
            if (Math.abs(getAltitudeDiff(root)) > 1) {
                return false;
            }
            return checkBalance(root.left) && checkBalance(root.right);
        }
        return true;
    }

    /**
     * 设置用于二叉树图形化解析的builder
     * 图形化解析网址：http://mshang.ca/syntree
     */
    public String getVisualizeString() {
        visualizeBuilder.setLength(0);
        setVisualizeBuilder(root, visualizeBuilder);
        return visualizeBuilder.toString();
    }

    private void setVisualizeBuilder(AVLNode avlNode, StringBuilder builder) {
        if (avlNode == null) {
            builder.append("[null]");
            return;
        }
        builder.append("[").append(avlNode.key).append("-").append(avlNode.height);
        setVisualizeBuilder(avlNode.left, builder);
        setVisualizeBuilder(avlNode.right, builder);
        builder.append("]");
    }

    /**
     * 获取二叉树的结点总数
     */
    public int countNodes() {
        return countNodes(root);
    }

    private int countNodes(AVLNode avlNode) {
        if (avlNode == null) {
            return 0;
        }
        return 1 + countNodes(avlNode.left) + countNodes(avlNode.right);
    }

    /**
     * 查找结点值
     */
    public Object get(int key) {
        return get(key, root);
    }

    private Object get(int key, AVLNode root) {
        if (root == null) {
            return null;
        }
        if (root.key < key) {
            return get(key, root.right);
        }
        if (root.key > key) {
            return get(key, root.left);
        }
        return root.value;
    }
}


