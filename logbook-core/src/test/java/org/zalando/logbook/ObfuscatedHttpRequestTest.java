package org.zalando.logbook;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.zalando.logbook.MockHttpRequest.request;

public final class ObfuscatedHttpRequestTest {

    private final HttpRequest unit = new ObfuscatedHttpRequest(request()
            .query("password=1234&limit=1")
            .headers(MockHeaders.of(
                    "Authorization", "Bearer 9b7606a6-6838-11e5-8ed4-10ddb1ee7671",
                    "Accept", "text/plain"))
            .body("My secret is s3cr3t")
            .build(),
            Obfuscators.obfuscate("password", "unknown"),
            Obfuscators.authorization(),
            (contentType, body) -> body.replace("s3cr3t", "f4k3"));

    @Test
    public void shouldNotFailOnInvalidUri() {
        final String query = "file=.|.%2F.|.%2Fetc%2Fpasswd";

        final ObfuscatedHttpRequest invalidRequest = new ObfuscatedHttpRequest(
                MockHttpRequest.request()
                        .path("/login")
                        .query(query)
                        .build(),
                Obfuscators.obfuscate("file", "unknown"),
                HeaderObfuscator.none(),
                BodyObfuscator.none());

        assertThat(invalidRequest.getRequestUri(), endsWith("/login?file=unknown"));
        assertThat(invalidRequest.getPath(), is("/login"));
        assertThat(invalidRequest.getQuery(), is("file=unknown"));
    }

    @Test
    public void shouldObfuscateAuthorizationHeader() {
        assertThat(unit.getHeaders(), hasEntry(equalTo("Authorization"), contains("XXX")));
    }

    @Test
    public void shouldNotObfuscateAcceptHeader() {
        assertThat(unit.getHeaders(), hasEntry(equalTo("Accept"), contains("text/plain")));
    }

    @Test
    public void shouldNotObfuscateEmptyQueryString() {
        final ObfuscatedHttpRequest request = new ObfuscatedHttpRequest(MockHttpRequest.create(),
                $ -> "*",
                HeaderObfuscator.none(),
                BodyObfuscator.none());

        assertThat(request.getRequestUri(), is("http://localhost/"));
        assertThat(request.getQuery(), is(emptyString()));
    }

    @Test
    public void shouldObfuscatePasswordParameter() {
        assertThat(unit.getRequestUri(), is("http://localhost/?password=unknown&limit=1"));
        assertThat(unit.getQuery(), is("password=unknown&limit=1"));
    }

    @Test
    public void shouldObfuscateBody() throws IOException {
        assertThat(unit.getBodyAsString(), is("My secret is f4k3"));
    }

    @Test
    public void shouldObfuscateBodyContent() throws IOException {
        assertThat(new String(unit.getBody(), unit.getCharset()), is("My secret is f4k3"));
    }

}
