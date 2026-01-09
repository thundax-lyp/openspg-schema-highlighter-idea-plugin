package org.openspg.idea.schema.ui.editor.jcef;

import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;

public abstract class SchemaResourceSupplier {

    public abstract boolean isSupported(@NotNull CefRequest request);

    public abstract Resource getResource(@NotNull CefRequest request);

    public interface Resource {
        @NotNull String getContentType();

        byte[] getContent();
    }
}
