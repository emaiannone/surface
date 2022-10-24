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

        public ClassInspectorResults getClassResults() {
            return classResults;
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

        public String getRootFullyQualifiedName() {
            return classResults.getClassFullyQualifiedName();
        }

        public int getNumberChildren() {
            return children.size();
        }

        public int getNumberCriticalClasses() {
            return (classResults.isCritical() ? 1 : 0) +
                    children.stream().mapToInt(InheritanceTreeNode::getNumberCriticalClasses).sum();
        }

        public int getNumberCriticalSuperclasses() {
            return (getNumberChildren() == 0 ? 0 :
                    ((classResults.isCritical() ? 1 : 0) +
                    children.stream().mapToInt(InheritanceTreeNode::getNumberCriticalSuperclasses).sum()));
        }

        public int getNumberClassifiedMethods() {
            return classResults.getNumberClassifiedMethods() +
                    children.stream().mapToInt(InheritanceTreeNode::getNumberClassifiedMethods).sum();
        }

        public int getNumberInheritableClassifiedMethods() {
            return (getNumberChildren() == 0 ? 0 :
                    (classResults.getNumberInheritableClassifiedMethods() +
                    children.stream().mapToInt(InheritanceTreeNode::getNumberInheritableClassifiedMethods).sum()));
        }

        public int getNumberClassifiedAttributes() {
            return classResults.getNumberClassifiedAttributes() +
                    children.stream().mapToInt(InheritanceTreeNode::getNumberClassifiedAttributes).sum();
        }

        public int getNumberInheritableClassifiedAttributes() {
            return (getNumberChildren() == 0 ? 0 :
                    (classResults.getNumberInheritableClassifiedAttributes() +
                            children.stream().mapToInt(InheritanceTreeNode::getNumberInheritableClassifiedAttributes).sum()));
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
