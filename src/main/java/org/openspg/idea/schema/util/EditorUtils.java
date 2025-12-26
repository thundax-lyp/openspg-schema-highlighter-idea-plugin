package org.openspg.idea.schema.util;

import com.alibaba.fastjson.JSONObject;
import com.intellij.ide.scratch.ScratchUtil;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditorUtils {

    private static final Logger logger = Logger.getInstance(EditorUtils.class);

    public static boolean hasSchemaType(@NotNull VirtualFile file) {
        return FileTypeRegistry.getInstance().isFileOfType(file, SchemaFileType.INSTANCE);
    }

    public static boolean isSchemaScratchFile(Project project, VirtualFile file) {
        if (!ScratchUtil.isScratch(file)) {
            return false;
        }

        Language language = LanguageUtil.getLanguageForPsi(project, file);
        return language == SchemaFileType.INSTANCE.getLanguage();
    }

    public static void traceFileAsJson(Project project, VirtualFile file) {
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) {
            logger.error("Unparsed file: " + file.getName());

        } else {
            String jsonString = fileToJsonString(psiFile);
            logger.warn(jsonString);
        }
    }

    public static @NotNull String fileToJsonString(PsiFile psiFile) {
        Map<String, Object> map = PsiUtils.toJson(psiFile);
        map = normalizeJsonMap(map);
        return JSONObject.toJSONString(map);
    }

    public static Map<String, Object> normalizeJsonMap(Map<?, ?> data) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof String && !((String) value).isEmpty()) {
                result.put(key, value);

            } else if (value instanceof List && !((List<?>) value).isEmpty()) {
                result.put(key, ((List<?>) value).stream().map(item -> {
                    if (item instanceof Map) {
                        return normalizeJsonMap((Map<?, ?>) item);
                    }
                    return item;
                }).collect(Collectors.toList()));

            } else if (value instanceof Map && !((Map<?, ?>) value).isEmpty()) {
                result.put(key, normalizeJsonMap((Map<?, ?>) value));
            }
        }
        return result;
    }

}
