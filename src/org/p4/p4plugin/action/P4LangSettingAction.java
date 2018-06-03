package org.p4.p4plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class P4LangSettingAction extends AnAction {

    public P4LangSettingAction() {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        P4LangSettingDialog dialog = new P4LangSettingDialog(project);
        dialog.show();
    }
}
