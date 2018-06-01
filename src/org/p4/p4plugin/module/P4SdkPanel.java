package org.p4.p4plugin.module;

import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class P4SdkPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel sdkPanel;
    private P4SdkComboBox sdkComboBox;
    private Sdk sdk;

    public P4SdkPanel() {
        super(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    public Sdk getSdk() {
        return sdkComboBox.getSelectedSdk();
    }

}
