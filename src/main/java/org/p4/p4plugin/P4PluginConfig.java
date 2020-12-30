package org.p4.p4plugin;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class P4PluginConfig {
    private static final File USER_HOME = new File(System.getProperty("user.home"));
    public static final File CONFIG_FILE = new File(USER_HOME, ".p4plugin.cfg");
    public static final String P4_INCLUDE_PATH_KEY = "p4-include-path";

    public static Map<String, String> readConfig() {
        Map<String, String> config = Maps.newHashMap();
        try {
            BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
            br.lines().forEach(line -> {
                // Line format: KEY=VALUE
                String[] kv = line.split("=");
                String key = kv[0];

                if (kv.length < 2) {
                    config.put(key, "");
                    return;
                }

                String value = kv[1];
                if (kv.length > 2) {
                    StringBuilder valueBuilder = new StringBuilder(value);
                    for (int i = 2; i < kv.length; i++) {
                        valueBuilder.append("=");
                        valueBuilder.append(kv[i]);
                    }
                    value = valueBuilder.toString();
                }

                config.put(key, value);
            });
            br.close();
        } catch (FileNotFoundException e) {
            /* ignore */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void writeConfig(String key, String value) {
        try {
            PrintWriter pw = new PrintWriter(CONFIG_FILE.getAbsoluteFile(), "UTF-8");
            pw.printf("%s=%s", key, value);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
