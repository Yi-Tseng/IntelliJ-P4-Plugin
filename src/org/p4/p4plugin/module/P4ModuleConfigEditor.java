package org.p4.p4plugin.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.ModuleElementsEditor;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class P4ModuleConfigEditor extends ModuleElementsEditor {
    private static final Logger log = LoggerFactory.getLogger(P4ModuleConfigEditor.class);

    ModifiableRootModel module;
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;

    protected P4ModuleConfigEditor(@NotNull ModuleConfigurationState moduleConfigurationState) {
        super(moduleConfigurationState);
        module = getModel();
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
        module.addContentEntry("Test");
        module.commit();
    }

}
