package org.p4.p4plugin.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class P4ModuleSettings {
    public static final String PATH_SPLIT = ";";
    private static final String P4_INC_PATH = "p4IncludePaths";
    private static final String P4_COMPILER_PATH = "p4CompilerPath";
    private static final String P4_COMPILER_ARGS = "p4CompilerArgs";

    private String[] defaultP4IncludePaths;
    private String p4CompilerPath;
    private String p4CompilerArgs;

    public P4ModuleSettings(String defaultP4IncludePath, String p4CompilerPath, String p4CompilerArgs) {
        this.defaultP4IncludePaths = defaultP4IncludePath.split(PATH_SPLIT);
        this.p4CompilerPath = p4CompilerPath;
        this.p4CompilerArgs = p4CompilerArgs;
    }

    public String[] getDefaultP4IncludePaths() {
        return defaultP4IncludePaths;
    }

    public String getP4CompilerPath() {
        return p4CompilerPath;
    }

    public String getP4CompilerArgs() {
        return p4CompilerArgs;
    }

    public String getJsonData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(P4_INC_PATH, String.join(PATH_SPLIT, defaultP4IncludePaths));
        jsonObject.addProperty(P4_COMPILER_PATH, p4CompilerPath);
        jsonObject.addProperty(P4_COMPILER_ARGS, p4CompilerArgs);
        return jsonObject.toString();
    }

    public static P4ModuleSettings fromFile(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(inputStreamReader).getAsJsonObject();
            String incPaths = jsonObject.get(P4_INC_PATH).getAsString();
            String compilerPath = jsonObject.get(P4_COMPILER_PATH).getAsString();
            String compilerArgs = jsonObject.get(P4_COMPILER_ARGS).getAsString();

            return new P4ModuleSettings(incPaths, compilerPath, compilerArgs);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean commitToFile(File file) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(P4_INC_PATH, String.join(PATH_SPLIT, defaultP4IncludePaths));
            jsonObject.addProperty(P4_COMPILER_PATH, p4CompilerPath);
            jsonObject.addProperty(P4_COMPILER_ARGS, p4CompilerArgs);

            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
