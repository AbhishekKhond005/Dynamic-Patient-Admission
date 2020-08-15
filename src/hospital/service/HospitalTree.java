package hospital.service;

import hospital.model.*;

import java.util.*;

public class HospitalTree {

    private TreeNode root;

    public static class TreeNode {
        private String name;
        private TreeNode parent;
        private List<TreeNode> children;
        private Object data;

        public TreeNode(String name, Object data) {
            this.name = name;
            this.data = data;
            this.children = new ArrayList<>();
        }

        public String getName() { return name; }
        public Object getData() { return data; }
        public TreeNode getParent() { return parent; }
        public List<TreeNode> getChildren() { return children; }

        public void addChild(TreeNode child) {
            children.add(child);
            child.parent = this;
        }

        public boolean isLeaf() { return children.isEmpty(); }
        public boolean isRoot() { return parent == null; }

        public int getHeight() {
            if (isLeaf()) return 0;
            int max = 0;
            for (TreeNode c : children) {
                max = Math.max(max, c.getHeight() + 1);
            }
            return max;
        }

        public int getSize() {
            int count = 1;
            for (TreeNode c : children) count += c.getSize();
            return count;
        }
    }

    public HospitalTree(List<Department> departments) {
        root = new TreeNode("Hospital", null);
        for (Department dept : departments) {
            TreeNode deptNode = new TreeNode(dept.getName(), dept);
            root.addChild(deptNode);
            for (Room room : dept.getRooms()) {
                TreeNode roomNode = new TreeNode(room.getRoomName(), room);
                deptNode.addChild(roomNode);
                for (Bed bed : room.getBeds()) {
                    TreeNode bedNode = new TreeNode(bed.getBedId(), bed);
                    roomNode.addChild(bedNode);
                }
            }
        }
    }

    public TreeNode getRoot() { return root; }

    public void dfsTraverse(TreeNode node, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) indent.append("  ");
        System.out.println(indent + node.getName());
        for (TreeNode child : node.getChildren()) {
            dfsTraverse(child, depth + 1);
        }
    }

    public void bfsTraverse() {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            int depth = getDepth(node);
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < depth; i++) indent.append("  ");
            System.out.println(indent + node.getName());
            queue.addAll(node.getChildren());
        }
    }

    public int getDepth(TreeNode node) {
        int depth = 0;
        TreeNode current = node;
        while (!current.isRoot()) {
            current = current.getParent();
            depth++;
        }
        return depth;
    }

    public TreeNode findNode(String name) {
        return findNodeRecursive(root, name);
    }

    private TreeNode findNodeRecursive(TreeNode node, String name) {
        if (node.getName().equals(name)) return node;
        for (TreeNode child : node.getChildren()) {
            TreeNode found = findNodeRecursive(child, name);
            if (found != null) return found;
        }
        return null;
    }

    public List<Bed> getAllBedsUnder(TreeNode node) {
        List<Bed> beds = new ArrayList<>();
        collectBeds(node, beds);
        return beds;
    }

    private void collectBeds(TreeNode node, List<Bed> beds) {
        if (node.getData() instanceof Bed) {
            beds.add((Bed) node.getData());
        }
        for (TreeNode child : node.getChildren()) {
            collectBeds(child, beds);
        }
    }

    public List<Bed> getAvailableBedsUnder(TreeNode node) {
        List<Bed> all = getAllBedsUnder(node);
        List<Bed> available = new ArrayList<>();
        for (Bed b : all) {
            if (b.isAvailable()) available.add(b);
        }
        return available;
    }

    public Map<String, Integer> countBedsByDepartment() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (TreeNode deptNode : root.getChildren()) {
            int count = 0;
            for (TreeNode roomNode : deptNode.getChildren()) {
                count += roomNode.getChildren().size();
            }
            counts.put(deptNode.getName(), count);
        }
        return counts;
    }

    public Map<String, Integer> countAvailableByDepartment() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (TreeNode deptNode : root.getChildren()) {
            int count = 0;
            for (TreeNode roomNode : deptNode.getChildren()) {
                for (TreeNode bedNode : roomNode.getChildren()) {
                    Bed bed = (Bed) bedNode.getData();
                    if (bed.isAvailable()) count++;
                }
            }
            counts.put(deptNode.getName(), count);
        }
        return counts;
    }
}
