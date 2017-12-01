package org.p4.p4plugin;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class P4Plugin extends AbstractProjectComponent {

    public P4Plugin(Project project) {
        super(project);
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "P4Plugin";
    }

    @Override
    public void projectOpened() {
        // called when project is opened
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}
