package org.p4.p4plugin.action;

import com.google.common.base.CaseFormat;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.icon.P4LangIcon;
import java.util.Properties;

public class NewP4FileAction extends CreateFromTemplateAction<PsiFile> {

    public NewP4FileAction() {
        super("P4 File", null, P4LangIcon.ICON);
    }

    @Nullable
    @Override
    protected PsiFile createFile(String fileName, String templateName, PsiDirectory psiDirectory) {
        try {
            return createFile(fileName, psiDirectory, templateName).getContainingFile();
        }
        catch (Exception e) {
            throw new IncorrectOperationException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory,
                               CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("Create new P4 file");
        P4TemplateFileUtil.getApplicableTemplates(project).forEach(ft -> {
            String name = ft.getName();
            builder.addKind(name, P4LangIcon.ICON, name);
        });
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String newName, String templateName) {
        return "Creating file " + newName;
    }

    private static PsiElement createFile(String fileName, @NotNull PsiDirectory directory, final String templateName)
            throws Exception {
        final Project project = directory.getProject();
        final FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(project);
        final Properties props = new Properties(fileTemplateManager.getDefaultProperties());
        props.setProperty(FileTemplate.ATTRIBUTE_NAME, fileName);

        String allCaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fileName);
        props.setProperty("ALL_CASE_NAME", allCaseName);

        final FileTemplate template = fileTemplateManager.getInternalTemplate(templateName);
        return FileTemplateUtil.createFromTemplate(template, fileName, props, directory, NewP4FileAction.class.getClassLoader());
    }
}
