package org.p4.p4plugin;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P4LangAnnotator implements Annotator {
    private static final Logger log = LoggerFactory.getLogger(P4LangAnnotator.class);

    @Override
    public void annotate(@NotNull PsiElement psiElement,
                         @NotNull AnnotationHolder holder) {
    }
}
