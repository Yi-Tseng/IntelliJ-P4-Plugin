package org.p4.p4plugin.module;

import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P4SdkType extends SdkType {
    Logger log = LoggerFactory.getLogger(getClass());
    private static final P4SdkType INSTANCE = new P4SdkType();
    public static final String P4_SDK_ID = "P4_SDK";

    public P4SdkType() {
        super(P4_SDK_ID);
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        return "/usr/local/share/p4c/";
    }

    @Override
    public boolean isValidSdkHome(String s) {
        return true;
    }

    @Override
    public String suggestSdkName(String s, String s1) {
        return "P4";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel,
                                                                       @NotNull SdkModificator sdkModificator) {
        log.info("createAdditionalDataConfigurable {} {}", sdkModel, sdkModificator);
        return null;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "P4 SDK";
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData,
                                   @NotNull Element element) {
        if (sdkAdditionalData instanceof P4SdkData) {
            XmlSerializer.serializeInto(sdkAdditionalData, element);
        }
    }

    @Nullable
    @Override
    public SdkAdditionalData loadAdditionalData(Element additional) {
        return XmlSerializer.deserialize(additional, P4SdkData.class);
    }

    @Override
    public boolean setupSdkPaths(@NotNull Sdk sdk, @NotNull SdkModel sdkModel) {
        SdkModificator modificator = sdk.getSdkModificator();
        modificator.setVersionString(getVersionString(sdk));
        modificator.commitChanges(); // save
        return true;
    }

    @Nullable
    @Override
    public String getVersionString(@NotNull Sdk sdk) {
        // TODO: get version from p4c
        return "0.0.1";
    }

    public static P4SdkType getInstance() {
        return INSTANCE;
    }
}
