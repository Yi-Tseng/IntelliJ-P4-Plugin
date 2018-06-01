package org.p4.p4plugin.module;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

public class P4SdkData implements SdkAdditionalData, PersistentStateComponent<P4SdkData> {

    private String homePath;
    private String version;

    @Nullable
    @Override
    public P4SdkData getState() {
        return this;
    }

    @Override
    public void loadState(P4SdkData p4SdkData) {
        XmlSerializerUtil.copyBean(p4SdkData, this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getHomePath() {
        return homePath;
    }

    public String getVersion() {
        return version;
    }
}
