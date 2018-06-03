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
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.parser.P4LangParserUtil;
import org.p4.p4plugin.psi.P4LangTypeRef;
import org.p4.p4plugin.psi.P4LangTypedefName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<VirtualFile> allIncludeFiles = getAllPossibleFiles(project);
        Set<String> includedFiles = getIncludedFiles(typeRef.getContainingFile());

        allIncludeFiles = allIncludeFiles.stream()
                .filter(Objects::nonNull)
                .filter(file -> includedFiles.contains(file.getName()))
                .collect(Collectors.toSet());
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

    private Set<String> getIncludedFiles(PsiFile sourceFile) {
        Set<String> result = Sets.newHashSet();
        Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(sourceFile, PsiComment.class);
        for (PsiComment comment : comments) {
            if (P4LangParserUtil.isInclude(comment)) {
                String includeFileName = P4LangParserUtil.getIncludeFile(comment);
                if (includeFileName != null && !includeFileName.isEmpty()) {
                    result.add(includeFileName);
                }
            }
        }
        return result;
    }

    private Set<VirtualFile> getAllPossibleFiles(Project project) {
        String includePathCfg = P4PluginConfig.readConfig()
                .getOrDefault(P4PluginConfig.P4_INCLUDE_PATH_KEY, "");

        Set<String> includePaths = Arrays.stream(includePathCfg.split(";"))
                .collect(Collectors.toSet());

        Set<VirtualFile> result = Sets.newHashSet();

        Set<VirtualFile> globalFiles = Sets.newHashSet();
        includePaths.forEach(path -> getGlobalFiles(path, globalFiles));
        result.addAll(globalFiles);

        Collection<VirtualFile> localFiles =
                FileBasedIndex.getInstance()
                        .getContainingFiles(ID.create("filetypes"), P4LangFileType.INSTANCE,
                                            GlobalSearchScope.allScope(project));
        result.addAll(localFiles);

        return result;
    }

    private void getGlobalFiles(String includePath, Set<VirtualFile> globalFiles) {
        File path = new File(includePath);
        Arrays.stream(Objects.requireNonNull(path.listFiles()))
                .forEach(file -> {
                    if (file.isDirectory()) {
                        getGlobalFiles(file.getAbsolutePath(), globalFiles);
                    } else {
                        VirtualFile vf = LocalFileSystem.getInstance().findFileByIoFile(file);
                        globalFiles.add(vf);
                    }
                });
    }
}
