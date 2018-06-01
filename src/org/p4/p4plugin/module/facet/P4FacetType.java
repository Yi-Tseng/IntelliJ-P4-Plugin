package org.p4.p4plugin.module.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.module.P4ModuleType;

public class P4FacetType extends FacetType<P4Facet, P4FacetConfig> {

    private static final FacetTypeId<P4Facet> P4_FACET_TYPE_ID = new FacetTypeId<>("P4");

    public P4FacetType() {
        super(P4_FACET_TYPE_ID, P4ModuleSettings.FACET_ID, P4ModuleSettings.FACET_NAME);
    }
    @Override
    public P4FacetConfig createDefaultConfiguration() {
        return new P4FacetConfig();
    }

    @Override
    public P4Facet createFacet(@NotNull Module module, String name,
                               @NotNull P4FacetConfig config,
                               @Nullable Facet underlyingFacet) {
        return new P4Facet(this, module, name, config, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return moduleType instanceof P4ModuleType;
    }
}
