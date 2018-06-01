package org.p4.p4plugin.module.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.module.P4SdkType;

public class P4FacetConfig implements FacetConfiguration, PersistentStateComponent<P4ModuleSettings> {
    private P4ModuleSettings p4ModuleSettings = new P4ModuleSettings();

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
        return new FacetEditorTab[]{new P4FacetEditorTab(p4ModuleSettings)};
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {

    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {

    }

    @Nullable
    @Override
    public P4ModuleSettings getState() {
        return p4ModuleSettings;
    }

    @Override
    public void loadState(P4ModuleSettings p4ModuleSettings) {
        XmlSerializerUtil.copyBean(p4ModuleSettings, this.p4ModuleSettings);
    }

    @Override
    public void noStateLoaded() {

    }

    public void setSdk(Sdk sdk) {
        if (sdk != null && sdk.getSdkType() instanceof P4SdkType) {
            p4ModuleSettings.p4SdkName = sdk.getName();
        }

    }
}
