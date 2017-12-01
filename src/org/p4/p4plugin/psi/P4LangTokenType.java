package org.p4.p4plugin.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.P4Lang;

public class P4LangTokenType extends IElementType {
    public P4LangTokenType(@NotNull String debugName) {
        super(debugName, P4Lang.INSTANCE);
    }
}
