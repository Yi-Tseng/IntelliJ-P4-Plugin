package org.p4.p4plugin.module;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.ModuleElementsEditor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;


public class P4ModuleConfigEditor extends ModuleElementsEditor {
    private static final Logger log = LoggerFactory.getLogger(P4ModuleConfigEditor.class);

    ModifiableRootModel module;
    Project project;
    String configPath;
    private JPanel mainPanel;
    private JTextField includePaths;
    private JTextField compilerPath;
    private JTextField compilerArgs;

    protected P4ModuleConfigEditor(@NotNull ModuleConfigurationState moduleConfigurationState) {
        super(moduleConfigurationState);
        project = moduleConfigurationState.getProject();
        module = moduleConfigurationState.getRootModel();
        configPath = project.getBasePath() + "/.idea/p4config.json";
        File p4ConfigFile = new File(configPath);

        if (p4ConfigFile.exists()) {
            P4ModuleSettings moduleSettings = P4ModuleSettings.fromFile(p4ConfigFile);
            includePaths.setText(String.join(P4ModuleSettings.PATH_SPLIT, moduleSettings.getDefaultP4IncludePaths()));
            compilerPath.setText(moduleSettings.getP4CompilerPath());
            compilerArgs.setText(moduleSettings.getP4CompilerArgs());
        } else {
            try {
                p4ConfigFile.createNewFile();
            } catch (IOException e) {
                log.error("Can not create p4 config file, {}", e.getMessage());
            }
        }
    }

    @Nls
    @Override
    public String getDisplayName() {
        return module.getModule().getName() + " settings";
    }

    @Override
    protected JComponent createComponentImpl() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        // save config to high level config file
        try {
            File configFile = new File(configPath);
            if (!configFile.exists()) {
                configFile.createNewFile();
            }

            P4ModuleSettings settings = new P4ModuleSettings(includePaths.getText(), compilerPath.getText(), compilerArgs.getText());
            log.info(settings.getJsonData());
            settings.commitToFile(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            log.error("Can not find idea directory from this project.");
        }

    }

}
