package org.p4.p4plugin;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class P4Plugin extends AbstractProjectComponent {

    private static final String DEFAULT_P4_INCLUDE_PATH = "/usr/local/share/p4c/p4include";

    public P4Plugin(Project project) {
        super(project);
    }

    @Override
    public void initComponent() {
        if (!P4PluginConfig.CONFIG_FILE.exists()) {
            // initialize the config file
            P4PluginConfig.writeConfig(P4PluginConfig.P4_INCLUDE_PATH_KEY, DEFAULT_P4_INCLUDE_PATH);
        }
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
