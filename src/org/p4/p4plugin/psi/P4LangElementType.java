package org.p4.p4plugin.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.P4Lang;

public class P4LangElementType extends IElementType {
    public P4LangElementType(@NotNull String name) {
        super(name, P4Lang.INSTANCE);
    }
}
