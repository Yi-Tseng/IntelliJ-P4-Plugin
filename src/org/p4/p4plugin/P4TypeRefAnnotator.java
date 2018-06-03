package org.p4.p4plugin;

import com.google.common.collect.Sets;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.parser.P4LangParserUtil;
import org.p4.p4plugin.psi.P4LangTypeRef;
import org.p4.p4plugin.psi.P4LangTypedefName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
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
        Project project = typeRef.getProject();
        Set<VirtualFile> allIncludeFiles = Sets.newHashSet();

        String localPath = typeRef.getContainingFile().getVirtualFile().getParent().getPath();
        String[] globalPaths = P4PluginConfig.readConfig()
                .getOrDefault(P4PluginConfig.P4_INCLUDE_PATH_KEY, "").split(";");

        Set<String> localIncludeFiles = Sets.newHashSet();
        Set<String> globalIncludeFiles = Sets.newHashSet();
        getIncludedFiles(typeRef.getContainingFile(), localIncludeFiles, globalIncludeFiles);

        localIncludeFiles.forEach(localFile -> {
            File includeFile = new File(localPath + "/" + localFile);
            if (includeFile.exists()) {
                allIncludeFiles.add(LocalFileSystem.getInstance().findFileByIoFile(includeFile));
            } else {
                log.warn("File {} not exist", includeFile.getAbsolutePath());
            }
        });

        globalIncludeFiles.forEach(globalFile -> {
            Arrays.stream(globalPaths).forEach(globalPath -> {
                File includeFile = new File(globalPath + "/" + globalFile);
                if (includeFile.exists()) {
                    allIncludeFiles.add(LocalFileSystem.getInstance().findFileByIoFile(includeFile));
                } else {
                    log.warn("File {} not exist", includeFile.getAbsolutePath());
                }
            });
        });

        allIncludeFiles.add(typeRef.getContainingFile().getVirtualFile());

        Set<String> definedTypes = P4LangParserUtil.getAllPossibleTypes(allIncludeFiles, project);
        PsiElement theType = typeRef.getFirstChild();
        if (theType instanceof P4LangTypedefName) {
            String typeRefName = theType.getText();
            if (!definedTypes.contains(typeRefName)) {
                holder.createErrorAnnotation(typeRef, "Can not resolve " + typeRefName);
            }
        }
    }

    private void getIncludedFiles(PsiFile sourceFile, Set<String> localIncludeFiles, Set<String> globalIncludeFiles) {
        Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(sourceFile, PsiComment.class);
        for (PsiComment comment : comments) {
            if (P4LangParserUtil.isInclude(comment)) {
                String includeFileName = P4LangParserUtil.getLocalIncludeFile(comment);
                if (includeFileName != null && !includeFileName.isEmpty()) {
                    localIncludeFiles.add(includeFileName);
                }
                includeFileName = P4LangParserUtil.getGlobalIncludeFile(comment);
                if (includeFileName != null && !includeFileName.isEmpty()) {
                    globalIncludeFiles.add(includeFileName);
                }
            }
        }
    }
}
