import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static guru.nidi.graphviz.model.Factory.node;

public class Tree {

    private String node;
    private List<Tree> children;
    private int mark = 0;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tree tree = (Tree) o;
        return mark == tree.mark &&
            Objects.equals(node, tree.node) &&
            Objects.equals(children, tree.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, children, mark);
    }

    private int setMarks(int currentMarkValue) {
        this.mark = currentMarkValue++;
        for (Tree child : children) {
            currentMarkValue = child.setMarks(currentMarkValue);
        }
        return currentMarkValue;
    }

    public MutableGraph getSources(MutableGraph g) {
        Map<Tree, Node> mapVertexToId = new HashMap<>();
        setMarks(1);
        g = init(mapVertexToId, g);
        g = dfs(mapVertexToId, g);
        return g;
    }

    private MutableGraph init(Map<Tree, Node> storage, MutableGraph g) {
        if (storage.containsKey(this)) {
            return g;
        }
        Node mNode = node(node + "_" + storage.size()).with(Label.of(node));
        g.add(mNode);
        storage.put(this, mNode);
        for (Tree child : children) {
            g = child.init(storage, g);
        }
        return g;
    }


    private MutableGraph dfs(Map<Tree, Node> storage, MutableGraph g) {
        Node currentVertex = storage.get(this);
        for (Tree child : children) {
            currentVertex = currentVertex.link(storage.get(child));
        }
        g = g.add(currentVertex);
        for (Tree child : children) {
            g = child.dfs(storage, g);
        }
        return g;
    }
}
