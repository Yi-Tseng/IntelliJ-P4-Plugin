package org.p4.p4plugin.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.P4PluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Map;

public class P4LangSettingDialog extends DialogWrapper {
    private static final Logger log = LoggerFactory.getLogger(P4LangSettingDialog.class);



    private JPanel mainPanel;
    private JTextField includePathField;

    public P4LangSettingDialog(@Nullable Project project) {
        super(project);
        setOKButtonText("OK");
        setCancelButtonText("Cancel");
        setTitle("P4 Language Settings");
        loadConfig();
        init();
    }

    @Override
    protected void doOKAction() {
        storeConfig();
        super.doOKAction();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    private void storeConfig() {
        String newIncludePath = includePathField.getText();
        P4PluginConfig.writeConfig(P4PluginConfig.P4_INCLUDE_PATH_KEY, newIncludePath);
    }

    private void loadConfig() {
        includePathField.setText("");
        Map<String, String> config = P4PluginConfig.readConfig();
        String includePath = config.getOrDefault(P4PluginConfig.P4_INCLUDE_PATH_KEY, "");
        includePathField.setText(includePath);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        String newIncludePath = includePathField.getText();
        if (newIncludePath.isEmpty()) {
            return null;
        }

        String[] paths = newIncludePath.split(";");
        for (String path : paths) {
            if (path.isEmpty()) {
                continue;
            }
            File pathDir = new File(path);
            if (!pathDir.exists() || !pathDir.isDirectory()) {
                return new ValidationInfo("Path " + path + " is invalidate");
            }
        }
        return null;
    }
}
