package org.p4.p4plugin.parser;

import com.intellij.lang.parser.GeneratedParserUtilBase;

import java.util.HashSet;
import java.util.Set;

public class P4LangParserUtil extends GeneratedParserUtilBase {
    private static Set<String> customTypes = new HashSet<>();

    public static void clearCustomTypes() {
        customTypes.clear();
    }

    public static void addCustomType(String customType) {
        customTypes.add(customType);
    }

    public static boolean containsCustonType(String customType) {
        return customTypes.contains(customType);
    }
}
