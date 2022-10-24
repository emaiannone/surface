package org.surface.surface.core.engine.inspection;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InheritanceInspector extends Inspector {
    private final ProjectInspectorResults projectResults;

    public InheritanceInspector(ProjectInspectorResults projectResults) {
        this.projectResults = projectResults;
    }

    @Override
    public InheritanceInspectorResults inspect() {
        Set<ClassInspectorResults> classResults = projectResults.getClassResults();
        Map<String, InheritanceTreeNode> nodeMap = new LinkedHashMap<>();
        classResults.forEach(cr -> nodeMap.put(cr.getClassFullyQualifiedName(), new InheritanceTreeNode(cr)));
        for (ClassInspectorResults classResult : classResults) {
            // Can be null if the name cannot be resolved (superclass is not in the classpath)
            ResolvedReferenceTypeDeclaration superclass = classResult.getSuperclass();
            if (superclass != null) {
                InheritanceTreeNode superclassTreeNode = nodeMap.get(superclass.getQualifiedName());
                // If the superclass was not inspected, we ignore it, as it is outside our scope
                if (superclassTreeNode != null) {
                    InheritanceTreeNode childTreeNode = nodeMap.get(classResult.getClassFullyQualifiedName());
                    superclassTreeNode.addChild(childTreeNode);
                }
            }
        }
        Set<InheritanceTreeNode> allChildren = nodeMap.values()
                .stream()
                .map(InheritanceTreeNode::getChildren)
                .flatMap(Set::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        // Roots detection phase
        Set<InheritanceTreeNode> roots = nodeMap.values()
                .stream()
                .filter(e -> e.getNumberChildren() > 0)
                .filter(e -> !allChildren.contains(e))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        InheritanceInspectorResults inheritanceInspectorResults = new InheritanceInspectorResults();
        inheritanceInspectorResults.addInheritanceTreeRoots(roots);
        return inheritanceInspectorResults;
    }
}
