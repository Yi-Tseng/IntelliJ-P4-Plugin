package org.p4.p4plugin.module;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.ui.ProjectJdksEditor;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ComboboxWithBrowseButton;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class P4SdkComboBox extends ComboboxWithBrowseButton {
    private static final Logger log = LoggerFactory.getLogger(P4SdkComboBox.class);

    public P4SdkComboBox() {
        getComboBox().setRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, Object value,
                                                 int index, boolean selected, boolean hasFocus) {
                if (value instanceof Sdk) {
                    append(((Sdk) value).getName());
                } else {
                    append("Select P4 SDK", SimpleTextAttributes.ERROR_ATTRIBUTES);
                }
            }
        });

        addActionListener(event -> {
            Sdk selectedSdk = getSelectedSdk();
            final Project project = ProjectManager.getInstance().getDefaultProject();
            ProjectJdksEditor editor = new ProjectJdksEditor(selectedSdk, project, P4SdkComboBox.this);
            editor.show();
            if (editor.isOK()) {
                selectedSdk = editor.getSelectedJdk();
                updateSdkList(selectedSdk, false);
            }
        });

        updateSdkList(null, true);
    }

    public void updateSdkList(Sdk sdkToSelect, boolean selectAnySdk) {

        final List<Sdk> sdkList = new ArrayList<>();
        for (Sdk sdk : ProjectJdkTable.getInstance().getAllJdks()) {
            if (sdk.getSdkType().getName().equals(P4SdkType.P4_SDK_ID)) {
                sdkList.add(sdk);
            }
        }
        if (selectAnySdk && sdkList.size() > 0) {
            sdkToSelect = sdkList.get(0);
        }
        sdkList.add(0, null); // For non selection
        getComboBox().setModel(new DefaultComboBoxModel(sdkList.toArray(new Sdk[sdkList.size()])));
        getComboBox().setSelectedItem(sdkToSelect);
    }

    public Sdk getSelectedSdk() {
        return (Sdk) getComboBox().getSelectedItem();
    }
}
