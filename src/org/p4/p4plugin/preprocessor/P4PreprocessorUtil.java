package org.p4.p4plugin.preprocessor;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.p4.p4plugin.P4PluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class P4PreprocessorUtil {
    private static final Logger log = LoggerFactory.getLogger(P4PreprocessorUtil.class);
    private static final String TEMP_OUTPUT_DIR = "/tmp/p4tmp";

    public static VirtualFile getLatestPreprocessedFile(VirtualFile sourceFile) {
        String tempFilePath = String.format("%s/%s",
                                            TEMP_OUTPUT_DIR,
                                            sourceFile.getName());
        File tempOutputFile = new File(tempFilePath);
        if (!tempOutputFile.exists()) return null;
        return LocalFileSystem.getInstance().findFileByIoFile(tempOutputFile);
    }

    public static VirtualFile preprocessP4File(VirtualFile sourceFile) {
        String[] includePaths = P4PluginConfig.readConfig()
                .getOrDefault(P4PluginConfig.P4_INCLUDE_PATH_KEY, "").split(";");

        StringBuilder includePathArgs = new StringBuilder();
        for (String path : includePaths) {
            includePathArgs.append("-I ");
            includePathArgs.append(path);
        }

        String tempFilePath = String.format("%s/%s",
                                            TEMP_OUTPUT_DIR,
                                            sourceFile.getName());



        String cmd = String.format("cc -E -x c %s -o %s %s", includePathArgs.toString(),
                                   tempFilePath, sourceFile.getPath());

        try {
            log.info("executing {}", cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            File tempOutputFile = new File(tempFilePath);
            return LocalFileSystem.getInstance().findFileByIoFile(tempOutputFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
