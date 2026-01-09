package org.openspg.idea.schema.ui.editor.jcef;

import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FetchSchemaApiSupplier extends SchemaResourceSupplier {

    private static final String API_SCHEMA_FETCH = "schema/fetch";

    private final Supplier<byte[]> supplier;

    public FetchSchemaApiSupplier(Supplier<byte[]> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean isSupported(@NotNull CefRequest request) {
        return request.getURL().endsWith(API_SCHEMA_FETCH);
    }

    @Override
    public Resource getResource(@NotNull CefRequest request) {
        return new Resource() {
            @Override
            public @NotNull String getContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getContent() {
                return supplier.get();
            }
        };
    }

}
