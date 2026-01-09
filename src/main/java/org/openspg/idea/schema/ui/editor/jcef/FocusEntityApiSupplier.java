package org.openspg.idea.schema.ui.editor.jcef;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FocusEntityApiSupplier extends SchemaResourceSupplier {

    private static final String API_FOCUS_ENTITY = "schema/focus";

    private final Function<List<SchemaEntity>, Boolean> processor;

    public FocusEntityApiSupplier(Function<List<SchemaEntity>, Boolean> processor) {
        this.processor = processor;
    }

    @Override
    public boolean isSupported(@NotNull CefRequest request) {
        return request.getURL().endsWith(API_FOCUS_ENTITY);
    }

    @Override
    public Resource getResource(@NotNull CefRequest request) {
        return new Resource() {
            @Override
            public @NotNull String getContentType() {
                return "application/json; charset=utf-8";
            }

            @NotNull
            @Override
            public byte[] getContent() {
                Map<String, Object> map = new HashMap<>();

                try {
                    String jsonString = request.getPostData().toString();
                    List<SchemaEntity> entities = JSONObject.parseObject(jsonString, new TypeReference<>() {
                    });

                    boolean result = processor.apply(entities);
                    map.put("code", result ? 0 : 1);
                    map.put("message", "success");

                } catch (Exception e) {
                    map.put("code", 500);
                    map.put("message", e.getMessage());
                }

                return JSONObject.toJSONString(map).getBytes(StandardCharsets.UTF_8);
            }
        };
    }

}
