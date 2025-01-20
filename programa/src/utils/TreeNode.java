package utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private final String value;
    private final List<TreeNode> children;

    public TreeNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    public String getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void printTree(String prefix) {
        System.out.println(prefix + value);

        for (TreeNode child : children) {
            child.printTree(prefix + "  ");
        }
    }
}