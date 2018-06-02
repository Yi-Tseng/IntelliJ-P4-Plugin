package org.p4.p4plugin.parser;

import com.google.common.collect.Sets;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import org.p4.p4plugin.P4LangFileType;
import org.p4.p4plugin.psi.P4LangControlTypeDeclaration;
import org.p4.p4plugin.psi.P4LangDeclaration;
import org.p4.p4plugin.psi.P4LangDerivedTypeDeclaration;
import org.p4.p4plugin.psi.P4LangFile;
import org.p4.p4plugin.psi.P4LangName_;
import org.p4.p4plugin.psi.P4LangPackageTypeDeclaration;
import org.p4.p4plugin.psi.P4LangParserTypeDeclaration;
import org.p4.p4plugin.psi.P4LangProgram;
import org.p4.p4plugin.psi.P4LangTypeDeclaration;
import org.p4.p4plugin.psi.P4LangTypedefDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P4LangParserUtil extends GeneratedParserUtilBase {
    private static final Logger log = LoggerFactory.getLogger(P4LangParserUtil.class);

    public static Set<VirtualFile> getIncludeFiles(Project project, VirtualFile sourceFile) {
        return null;
    }

    public static Set<String> getAllPossibleTypes(Project project) {

        Collection<VirtualFile> files =
                FileBasedIndex.getInstance().getContainingFiles(ID.create("filetypes"), P4LangFileType.INSTANCE,
                                                                GlobalSearchScope.allScope(project));

        Set<String> result = Sets.newHashSet();

        for (VirtualFile file : files) {
            P4LangFile p4File = (P4LangFile) PsiManager.getInstance(project).findFile(file);
            if (p4File == null) {
                continue;
            }
            P4LangProgram p4Program = PsiTreeUtil.getChildOfType(p4File, P4LangProgram.class);

            if (p4Program == null) {
                continue;
            }

            P4LangDeclaration[] declarations = PsiTreeUtil.getChildrenOfType(p4Program, P4LangDeclaration.class);
            if (declarations == null || declarations.length == 0) {
                continue;
            }

            for (P4LangDeclaration declaration : declarations) {
                if (!(declaration.getFirstChild() instanceof P4LangTypeDeclaration)) {
                    continue;
                }
                P4LangTypeDeclaration typeDeclaration = (P4LangTypeDeclaration) declaration.getFirstChild();

                PsiElement child = typeDeclaration.getFirstChild();
                if (child == null) {
                    continue;
                }
                P4LangName_ nameChild = null;
                if (child instanceof P4LangDerivedTypeDeclaration) {
                    PsiElement derivedChild = child.getFirstChild();
                    if (derivedChild != null) {
                        nameChild = PsiTreeUtil.getChildOfType(derivedChild, P4LangName_.class);
                    }
                } else {
                    nameChild = PsiTreeUtil.getChildOfType(child, P4LangName_.class);
                }
                if (nameChild != null) {
                    result.add(nameChild.getText());
                }
            }
        }
        return result;
    }
}
