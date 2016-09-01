package org.zalando.logbook;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

@Immutable
public final class MockRawHttpResponse implements MockHttpMessage, RawHttpResponse {

    private final String protocolVersion;
    private final Origin origin;
    private final int status;
    private final Map<String, List<String>> headers;
    private final String contentType;
    private final Charset charset;
    private final String body;

    @lombok.Builder(builderMethodName = "response", builderClassName = "Builder")
    public MockRawHttpResponse(
            @Nullable final String protocolVersion,
            @Nullable final Origin origin,
            final int status,
            @Nullable final Map<String, List<String>> headers,
            @Nullable final String contentType,
            @Nullable final Charset charset,
            @Nullable final String body) {
        this.protocolVersion = Optional.ofNullable(protocolVersion).orElse("HTTP/1.1");
        this.origin = Optional.ofNullable(origin).orElse(Origin.LOCAL);
        this.status = status == 0 ? 200 : status;
        this.headers = firstNonNullNorEmpty(headers, emptyMap());
        this.contentType = Optional.ofNullable(contentType).orElse("");
        this.charset = Optional.ofNullable(charset).orElse(StandardCharsets.UTF_8);
        this.body = Optional.ofNullable(body).orElse("");
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public HttpResponse withBody() throws IOException {
        return MockHttpResponse.response()
                .protocolVersion(protocolVersion)
                .origin(origin)
                .status(status)
                .headers(headers)
                .contentType(contentType)
                .charset(charset)
                .body(body)
                .build();
    }

    public static RawHttpResponse create() {
        return response().build();
    }

}
