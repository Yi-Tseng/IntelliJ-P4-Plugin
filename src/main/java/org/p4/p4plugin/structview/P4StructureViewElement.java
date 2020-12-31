package org.p4.p4plugin.structview;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.psi.P4LangFile;
import org.p4.p4plugin.parsing.P4Parser;
import org.p4.p4plugin.psi.P4LangTokenType;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class P4StructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private final NavigatablePsiElement element;
    private final ItemPresentation presentation;

    public P4StructureViewElement(NavigatablePsiElement element) {
        this.element = element;
        this.presentation = new P4StructViewElementPresentation(element);
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        return this.presentation;
    }

    @Override
    public @NotNull TreeElement[] getChildren() {
        List<TreeElement> treeElements = Lists.newArrayList();
        if (element instanceof P4LangFile) {
            PsiElement start = null;
            for (PsiElement child : element.getChildren()) {
                if (isRule(child, P4Parser.RULE_start)) {
                    start = child;
                    break;
                }
            }
            if (start == null) {
                return new TreeElement[0];
            }
            PsiElement prog = start.getFirstChild();
            if (prog == null) {
                return new TreeElement[0];
            }
            PsiElement input = prog.getFirstChild();
            if (input == null) {
                return new TreeElement[0];
            }
            PsiElement[] inputs = input.getChildren();
            List<PsiElement> decls = collectRulesFromList(inputs, P4Parser.RULE_declaration);
            decls.stream()
                    .map(PsiElement::getFirstChild)
                    .filter(e -> e instanceof ASTWrapperPsiElement)
                    .map(e -> new P4StructureViewElement((ASTWrapperPsiElement) e))
                    .forEach(treeElements::add);

        } else if (isRule(element, P4Parser.RULE_controlDeclaration)) {
            // Find all table, action, counter, register, ... from control block.
            // controlDeclaration
            //       : controlTypeDeclaration optConstructorParameters
            //         '{' controlLocalDeclarations APPLY controlBody '}'
            PsiElement controlLocalDeclarations = findChildRule(element, P4Parser.RULE_controlLocalDeclarations);
            if (controlLocalDeclarations != null) {
                List<PsiElement> decls = collectRulesFromList(controlLocalDeclarations.getChildren(),
                        P4Parser.RULE_controlLocalDeclaration);
                decls.stream()
                        .map(PsiElement::getFirstChild)
                        .filter(e -> e instanceof ASTWrapperPsiElement)
                        .map(e -> new P4StructureViewElement((ASTWrapperPsiElement) e))
                        .forEach(treeElements::add);
            }

            PsiElement controlBody = findChildRule(element, P4Parser.RULE_controlBody);
            if (controlBody instanceof ASTWrapperPsiElement) {
                treeElements.add(new P4StructureViewElement((ASTWrapperPsiElement) controlBody));
            }
        }
        return treeElements.toArray(TreeElement[]::new);
    }

    private List<PsiElement> collectRulesFromList(PsiElement[] children, int rule) {
        List<PsiElement> result = Lists.newArrayList();
        Queue<PsiElement> decls = new ArrayDeque<>(Arrays.asList(children));
        while (!decls.isEmpty()) {
            PsiElement c = decls.poll();
            if (isRule(c, rule)) {
                result.add(c);
            } else {
                decls.addAll(Arrays.asList(c.getChildren()));
            }
        }
        return result;
    }

    public static boolean isRule(PsiElement e, int rule) {
        if (e == null) {
            return false;
        }
        return e.getNode().getElementType() == P4LangTokenType.getRuleElementType(rule);
    }

    public static PsiElement findChildRule(PsiElement e, int rule) {
        if (e == null) {
            return null;
        }
        for (PsiElement c : e.getChildren()) {
            if (isRule(c, rule)) {
                return c;
            }
        }
        return null;
    }


    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        String key = presentation.getPresentableText();
        return key == null ? "" : key;
    }
}
