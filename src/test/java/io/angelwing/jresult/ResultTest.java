package io.angelwing.jresult;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.angelwing.jresult.external.assertj.ResultAssertions.assertThat;

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
        var result = Result.err(new Kaboom("something went wrong"));
        assertThat(result).hasError(new Kaboom("something went wrong"));
    }

    @Test
    void shouldReturnTrueWhenResultIsEmpty() {
        var result = Result.empty();
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnTrueWhenResultIsEmptyFromNullValue() {
        var result = Result.ok(null);
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsEmptyWhenResultHasValue() {
        var result = Result.ok(5);
        Assertions.assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void shouldReturnFalseOnIsEmptyWhenResultIsErr() {
        var result = Result.err(new Boom(10));
        Assertions.assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void shouldReturnTrueOnIsOkWhenResultHasValue() {
        var result = Result.ok(5);
        Assertions.assertThat(result.isOk()).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsOkWhenResultIsErr() {
        var result = Result.err(new Boom(10));
        Assertions.assertThat(result.isOk()).isFalse();
    }

    @Test
    void shouldReturnTrueOnIsOkAndWhenResultHasValueGreaterThanFive() {
        var result = Result.ok(6);
        Assertions.assertThat(result.isOkAnd(v -> v > 5)).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsOkAndWhenResultHasValueLessThanFive() {
        var result = Result.ok(6);
        Assertions.assertThat(result.isOkAnd(v -> v < 5)).isFalse();
    }

    @Test
    void shouldReturnFalseOnIsOkAndWhenResultIsErr() {
        Result<String, TestError> result = Result.err(new Crash(-3.14f));
        Assertions.assertThat(result.isOkAnd(Objects::isNull)).isFalse();
    }

    @Test
    void shouldReturnFalseOnIsErrWhenResultHasValue() {
        var result = Result.ok(5);
        Assertions.assertThat(result.isErr()).isFalse();
    }

    @Test
    void shouldReturnTrueOnIsErrWhenResultIsErr() {
        var result = Result.err(new Boom(10));
        Assertions.assertThat(result.isErr()).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsErrAndWhenResultIsOk() {
        Result<Integer, Boom> result = Result.ok(6);
        Assertions.assertThat(result.isErrAnd(e -> e.radius() == 0)).isFalse();
    }

    @Test
    void shouldReturnTrueOnIsErrAndWhenResultHasBoomErrEqualsZero() {
        var result = Result.err(new Boom(0));
        Assertions.assertThat(result.isErrAnd(e -> e.radius() == 0)).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsErrAndWhenResultCrashErrDoesNotEqualPi() {
        var result = Result.err(new Crash(3.14f));
        Assertions.assertThat(result.isErrAnd(e -> e.damage() == 3.15f)).isFalse();
    }

    @Test
    void shouldReturnOptionalEmptyOnOkWhenResultIsEmpty() {
        var result = Result.empty();
        Assertions.assertThat(result.ok()).isEmpty();
    }

    @Test
    void shouldReturnOptionalEmptyOnOkWhenResultIsEmptyFromNullValue() {
        var result = Result.ok(null);
        Assertions.assertThat(result.ok()).isEmpty();
    }

    @Test
    void shouldReturnOptionalContainingValueOnOkWhenResultIsOk() {
        var result = Result.ok(5);
        Assertions.assertThat(result.ok()).contains(5);
    }

    @Test
    void shouldReturnOptionalEmptyOnOkWhenResultIsErr() {
        var result = Result.err("error");
        Assertions.assertThat(result.ok()).isEmpty();
    }

    @Test
    void shouldReturnOptionalEmptyOnErrWhenResultIsEmpty() {
        var result = Result.empty();
        Assertions.assertThat(result.err()).isEmpty();
    }

    @Test
    void shouldReturnOptionalEmptyOnErrWhenResultIsEmptyFromNullValue() {
        var result = Result.ok(null);
        Assertions.assertThat(result.err()).isEmpty();
    }

    @Test
    void shouldReturnOptionalEmptyOnErrWhenResultIsOk() {
        var result = Result.ok(5);
        Assertions.assertThat(result.err()).isEmpty();
    }

    @Test
    void shouldReturnOptionalContainingErrorValueOnErrWhenResultIsErr() {
        var result = Result.err("error");
        Assertions.assertThat(result.err()).hasValue("error");
    }

    @Test
    void shouldReturnMappedOkResultWhenResultIsOk() {
        var result = Result.ok(5);
        var actual = result.map("result: %d"::formatted);
        assertThat(actual).hasValue("result: 5");
    }

    @Test
    void shouldNotApplyMappingWhenResultIsErr() {
        Result<Integer, String> result = Result.err("error");
        var actual = result.map(v -> v * 2);
        assertThat(actual).hasError("error");
    }

    @Test
    void shouldNotApplyErrorMappingWhenResultIsOk() {
        Result<Integer, String> result = Result.ok(5);
        var actual = result.mapErr(Kaboom::new);
        assertThat(actual).hasValue(5);
    }

    @Test
    void shouldReturnMappedErrResultWhenResultIsErr() {
        var result = Result.err("error");
        var actual = result.mapErr(String::length);
        assertThat(actual).hasError(5);
    }
}

sealed interface TestError permits Kaboom, Boom, Crash {
}

record Kaboom(String reason) implements TestError {
}

record Boom(int radius) implements TestError {
}

record Crash(float damage) implements TestError {
}