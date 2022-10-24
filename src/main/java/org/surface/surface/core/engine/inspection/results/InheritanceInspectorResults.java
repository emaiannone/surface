package org.surface.surface.core.engine.inspection.results;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InheritanceInspectorResults implements InspectorResults {
    private final Set<InheritanceTreeNode> inheritanceTreeRoots;

    public InheritanceInspectorResults() {
        this.inheritanceTreeRoots = new LinkedHashSet<>();
    }

    public void addInheritanceTreeRoot(InheritanceTreeNode root) {
        inheritanceTreeRoots.add(root);
    }

    public void addInheritanceTreeRoots(Set<InheritanceTreeNode> roots) {
        inheritanceTreeRoots.addAll(roots);
    }

    public void removeInheritanceTreeRoot(InheritanceTreeNode root) {
        inheritanceTreeRoots.remove(root);
    }

    public Set<InheritanceTreeNode> getInheritanceTreeRoots() {
        return Collections.unmodifiableSet(inheritanceTreeRoots);
    }

    public static class InheritanceTreeNode {
        private final ClassInspectorResults classResults;
        private final Set<InheritanceTreeNode> children;

        public InheritanceTreeNode(ClassInspectorResults classResults) {
            this.classResults = classResults;
            this.children = new LinkedHashSet<>();
        }

        public void addChild(InheritanceTreeNode child) {
            children.add(child);
        }

        public void removeChild(InheritanceTreeNode child) {
            children.remove(child);
        }

        public Set<InheritanceTreeNode> getChildren() {
            return Collections.unmodifiableSet(children);
        }

        public int getNumberChildren() {
            return children.size();
        }

        @Override
        public String toString() {
            List<String> collect = children.stream().map(InheritanceTreeNode::toString).collect(Collectors.toList());
            return "InheritanceTreeNode{" +
                    "className=" + classResults.getClassFullyQualifiedName() +
                    "\n\tchildren=" + (collect.size() > 0 ? "[" + String.join(",\n\t", collect) + "\n\t]" : "None") +
                    '}';
        }
    }
}
