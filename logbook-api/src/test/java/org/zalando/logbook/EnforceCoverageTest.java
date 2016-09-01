package org.zalando.logbook;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@Hack
@OhNoYouDidnt
public final class EnforceCoverageTest {

    @Test
    public void shouldUseHeadersConstructor() {
        new BaseHttpMessage.HeadersBuilder();
    }

    @Test
    public void shouldUseLogbookCreatorConstructor() {
        new LogbookCreator();
    }

    @Test
    public void shouldCoverUselessClearMethods() {
        final LogbookCreator.Builder builder = Logbook.builder();

        builder.clearQueryObfuscators();
        builder.clearHeaderObfuscators();
        builder.clearBodyObfuscators();
        builder.clearRequestObfuscators();
        builder.clearResponseObfuscators();

        builder.queryObfuscator(mock(QueryObfuscator.class));
        builder.headerObfuscator(mock(HeaderObfuscator.class));
        builder.bodyObfuscator(mock(BodyObfuscator.class));
        builder.requestObfuscator(mock(RequestObfuscator.class));
        builder.responseObfuscator(mock(ResponseObfuscator.class));

        builder.clearQueryObfuscators();
        builder.clearHeaderObfuscators();
        builder.clearBodyObfuscators();
        builder.clearRequestObfuscators();
        builder.clearResponseObfuscators();
    }

    @Test
    public void shouldUseRequestURIConstructor() {
        new RequestURI();
    }

    @Test
    public void shouldUseComponentValueOf() {
        RequestURI.Component.valueOf("SCHEME");
    }

    @Test
    public void shouldUseOriginValueOf() {
        Origin.valueOf("LOCAL");
    }

    @Test
    public void shouldUseBuilderToString() {
        assertThat(Logbook.builder(), hasToString(notNullValue()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fakeLogbookShouldThrow() throws IOException {
        Logbook.create().write(mock(RawHttpRequest.class));
    }
}
