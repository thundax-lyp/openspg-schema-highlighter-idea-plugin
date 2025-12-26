package org.openspg.idea.schema.ui.editor.model;

import java.io.Serializable;
import java.util.Map;

public class SchemaNamespaceModel implements Serializable {

    private Map<String, Object> jsonValue;

    public Map<String, Object> getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(Map<String, Object> jsonValue) {
        this.jsonValue = jsonValue;
    }
}
