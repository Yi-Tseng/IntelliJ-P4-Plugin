package org.p4.p4plugin.parser;

import com.google.common.collect.Sets;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.p4.p4plugin.psi.P4LangDeclaration;
import org.p4.p4plugin.psi.P4LangDerivedTypeDeclaration;
import org.p4.p4plugin.psi.P4LangExternDeclaration;
import org.p4.p4plugin.psi.P4LangFile;
import org.p4.p4plugin.psi.P4LangFunctionPrototype;
import org.p4.p4plugin.psi.P4LangName_;
import org.p4.p4plugin.psi.P4LangNonTypeName;
import org.p4.p4plugin.psi.P4LangPackageTypeDeclaration;
import org.p4.p4plugin.psi.P4LangProgram;
import org.p4.p4plugin.psi.P4LangTypeDeclaration;
import org.p4.p4plugin.psi.P4LangTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P4LangParserUtil extends GeneratedParserUtilBase {
    private static final Logger log = LoggerFactory.getLogger(P4LangParserUtil.class);
    private static final Pattern GLOBAL_INC_FILE = Pattern.compile("<([^>]+)>");
    private static final Pattern LOCAL_INC_FILE = Pattern.compile("\"([^>]+)\"");

    public static boolean isInclude(PsiComment comment) {
        return comment.getTokenType().equals(P4LangTypes.PRE_PROCESS) &&
                comment.getText().contains("include");
    }

    public static String getGlobalIncludeFile(PsiComment preProcess) {
        String text = preProcess.getText();
        Matcher matcher = GLOBAL_INC_FILE.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getLocalIncludeFile(PsiComment preProcess) {
        String text = preProcess.getText();
        Matcher matcher = LOCAL_INC_FILE.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Set<String> getAllPossibleTypes(Set<VirtualFile> allPossibleFiles, Project project) {

        Set<String> result = Sets.newHashSet();
        for (VirtualFile file : allPossibleFiles) {
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
                /* Externs */
                if (declaration.getFirstChild() instanceof P4LangExternDeclaration) {

                    P4LangExternDeclaration externDeclaration = (P4LangExternDeclaration) declaration.getFirstChild();

                    P4LangName_ nameChild = PsiTreeUtil.getChildOfType(externDeclaration, P4LangName_.class);
                    if (nameChild != null) {
                        result.add(nameChild.getText());
                        continue;
                    }

                    P4LangNonTypeName nonTypeName = PsiTreeUtil.getChildOfType(externDeclaration, P4LangNonTypeName.class);
                    if (nonTypeName != null) {
                        result.add(nonTypeName.getText());
                        continue;
                    }
                    P4LangFunctionPrototype functionPrototype =
                            PsiTreeUtil.getChildOfType(externDeclaration, P4LangFunctionPrototype.class);
                    if (functionPrototype != null) {
                        nameChild = functionPrototype.getName_();
                        result.add(nameChild.getText());
                        continue;
                    }
                }

                /* TypeDef */
                if (declaration.getFirstChild() instanceof P4LangTypeDeclaration) {
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

                /* Packages */
                if (declaration.getFirstChild() instanceof P4LangPackageTypeDeclaration) {
                    log.info("Package {}");
                    P4LangPackageTypeDeclaration packageTypeDeclaration =
                            (P4LangPackageTypeDeclaration) declaration.getFirstChild();
                    P4LangName_ nameChild = packageTypeDeclaration.getName_();
                    result.add(nameChild.getText());
                }
            }
        }
        return result;
    }
}
