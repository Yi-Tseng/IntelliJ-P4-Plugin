package org.p4.p4plugin.module.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;

public class P4Facet extends Facet<P4FacetConfig> {
    public P4Facet(@NotNull FacetType facetType,
                   @NotNull Module module,
                   @NotNull String name,
                   @NotNull P4FacetConfig configuration,
                   Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }
}
