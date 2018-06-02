package org.p4.p4plugin;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.parser.P4LangParserUtil;
import org.p4.p4plugin.psi.P4LangTypeRef;
import org.p4.p4plugin.psi.P4LangTypedefName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class P4TypeRefAnnotator implements Annotator {
    private static final Logger log = LoggerFactory.getLogger(P4TypeRefAnnotator.class);

    @Override
    public void annotate(@NotNull PsiElement psiElement,
                         @NotNull AnnotationHolder holder) {
        if (psiElement instanceof P4LangTypeRef) {
            checkTypeRef((P4LangTypeRef) psiElement, holder);
        }
    }

    private void checkTypeRef(P4LangTypeRef typeRef, AnnotationHolder holder) {
        log.info("Containing file {} ", typeRef.getContainingFile().getVirtualFile().getPath());
        Set<String> definedTypes = P4LangParserUtil.getAllPossibleTypes(typeRef.getProject());
        PsiElement theType = typeRef.getFirstChild();
        if (theType instanceof P4LangTypedefName) {
            String typeRefName = theType.getText();
            if (!definedTypes.contains(typeRefName)) {
                holder.createErrorAnnotation(typeRef, "Can't find type " + typeRefName);
            }
        }
    }
}
