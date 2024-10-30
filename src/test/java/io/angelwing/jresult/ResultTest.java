package io.angelwing.jresult;

import org.junit.jupiter.api.Test;

import static io.angelwing.jresult.external.assertj.ResultAssertions.assertThat;

enum TestError {
    KABOOM,
    BOOM,
    CRASH
}

class ResultTest {

    @Test
    void shouldReturnEmptyWhenCreatingEmptyResult() {
        var result = Result.empty();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenNullValue() {
        var result = Result.ok(null);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOkWhenCreatingOkResult() {
        var result = Result.ok("value");
        assertThat(result).hasValue("value");
    }

    @Test
    void shouldReturnErrWhenCreatingErrResult() {
        var result = Result.err(TestError.KABOOM);
        assertThat(result).hasError(TestError.KABOOM);
    }

}