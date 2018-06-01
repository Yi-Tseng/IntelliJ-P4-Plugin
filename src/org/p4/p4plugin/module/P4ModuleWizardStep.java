package org.p4.p4plugin.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

public class P4ModuleWizardStep extends ModuleWizardStep {

    private P4ModuleBuilder moduleBuilder;
    private P4SdkPanel p4SdkPanel;

    public P4ModuleWizardStep(P4ModuleBuilder moduleBuilder) {
        this.moduleBuilder = moduleBuilder;
        this.p4SdkPanel = new P4SdkPanel();
    }

    @Override
    public JComponent getComponent() {
        return p4SdkPanel;
    }

    @Override
    public void updateDataModel() {
        moduleBuilder.setP4Sdk(p4SdkPanel.getSdk());
    }

    @Override
    public boolean validate() throws ConfigurationException {
        return p4SdkPanel.getSdk() != null;
    }
}
