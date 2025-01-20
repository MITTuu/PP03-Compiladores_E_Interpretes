package test;

import utils.TreeNode;

public class Main {
    public static void main(String[] args) {

        TreeNode root = new TreeNode("Raiz");

        TreeNode child1 = new TreeNode("Hijo 1");
        TreeNode child2 = new TreeNode("Hijo 2");

        root.addChild(child1);
        root.addChild(child2);

        TreeNode child1_1 = new TreeNode("Hijo 1.1");
        TreeNode child1_2 = new TreeNode("Hijo 1.2");

        child1.addChild(child1_1);
        child1.addChild(child1_2);

        root.printTree("");
    }
}
