package org.p4.p4plugin;

import com.intellij.lang.Language;

public class P4Lang extends Language {
    public static P4Lang INSTANCE = new P4Lang();

    protected P4Lang() {
        super("P4Lang");
    }
}
