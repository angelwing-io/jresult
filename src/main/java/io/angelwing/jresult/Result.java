package io.angelwing.jresult;

import java.util.Optional;
import java.util.function.*;

/**
 * A Result type that represents either success - {@link Ok}, or failure - {@link Err}.
 * <pre>{@code
 * public Result<String, AppError> sessionId(Request req) {
 *     Cookie[] cookies = req.getCookies();
 *     if (cookies == null || cookies.length == 0) {
 *         return Result.err(EMPTY_COOKIES);
 *     }
 *
 *     for (Cookie cookie : cookies) {
 *         if ("session_id".equals(cookie.getName())) {
 *             return Result.ok(cookie.getValue());
 *         }
 *     }
 *
 *     return Result.err(MISSING_SESSION);
 * }
 *
 * enum AppError {
 *     MISSING_SESSION,
 *     EMPTY_COOKIES;
 * }
 * }</pre>
 * <p>
 * Ok and Err are exposed to allow <strong>pattern matching and deconstruction</strong>.
 *
 * <pre>{@code
 * Request req = ...;
 * var res = sessionId(req); // Result<String, AppError>
 *
 * switch (res) {
 *   case Ok(String sid) -> validate(sid);
 *   case Err(AppError e) -> logger.error("AppError: {}", e);
 * }
 *
 * // or more granular
 * switch (res) {
 *     case Ok(String sid) -> validate(sid);
 *     case Err(AppError e) when e == MISSING_SESSION -> unauthorized();
 *     case Err(AppError e) when e == EMPTY_COOKIES -> redirect("/login");
 * }
 *
 * // react to success
 * if (res instanceof Ok(String sid)) {
 *    validate(sid);
 * }
 *
 * // react to error
 * if (res instanceof Err(AppError e)) {
 *     logger.error("AppError: {}", e);
 *     switch (e) {
 *         case MISSING_SESSION -> unauthorized();
 *         case EMPTY_COOKIES -> redirect("/login");
 *     }
 * }
 *
 * // error yields a default value
 * double total = 1234.56
 * int installments = 3;
 * var res = splitPayments(total, installments);
 * var first = switch (res) {
 *    case Ok(List<Double> installments) -> installments.getFirst();
 *    case Err(Exception e) -> total;
 * }
 * }</pre>
 *
 * @param <O> Type of success value.
 * @param <E> Type of error value.
 * @author dcadea
 * @since 1.0.0
 */
public sealed interface Result<O, E> permits Ok, Err {

    @SuppressWarnings("unchecked")
    static <O, E> Result<O, E> empty() {
        return (Ok<O, E>) Ok.EMPTY;
    }

    static <O, E> Result<O, E> ok(O value) {
        if (value == null) {
            return empty();
        }

        return new Ok<>(value);
    }

    static <O, E> Result<O, E> err(E error) {
        return new Err<>(error);
    }

    default boolean isEmpty() {
        return this == Ok.EMPTY;
    }

    default boolean isOk() {
        return this instanceof Ok;
    }

    default boolean isOkAnd(Predicate<? super O> p) {
        return switch (this) {
            case Ok(O value) -> p.test(value);
            case Err(E _) -> false;
        };
    }

    default boolean isErr() {
        return this instanceof Err;
    }

    default boolean isErrAnd(Predicate<? super E> p) {
        return switch (this) {
            case Ok(O _) -> false;
            case Err(E error) -> p.test(error);
        };
    }

    default Optional<O> ok() {
        return switch (this) {
            case Ok(O value) -> Optional.ofNullable(value);
            case Err(E _) -> Optional.empty();
        };
    }

    default Optional<E> err() {
        return switch (this) {
            case Ok(O _) -> Optional.empty();
            case Err(E error) -> Optional.of(error);
        };
    }

    default <NO> Result<NO, E> map(Function<? super O, ? extends NO> op) {
        return switch (this) {
            case Ok(O value) -> ok(op.apply(value));
            case Err(E error) -> err(error);
        };
    }

    default <NE> Result<O, NE> mapErr(Function<? super E, ? extends NE> op) {
        return switch (this) {
            case Ok(O value) -> ok(value);
            case Err(E error) -> err(op.apply(error));
        };
    }

    default O mapOr(UnaryOperator<O> op, O fallback) {
        return switch (this) {
            case Ok(O value) -> op.apply(value);
            case Err(E _) -> fallback;
        };
    }

    default O mapOrElse(UnaryOperator<O> op, Supplier<? extends O> fallbackOp) {
        return switch (this) {
            case Ok(O value) -> op.apply(value);
            case Err(E _) -> fallbackOp.get();
        };
    }

    default Result<O, E> inspect(Consumer<? super O> op) {
        if (this instanceof Ok(O value)) {
            op.accept(value);
        }

        return this;
    }

    default Result<O, E> inspectErr(Consumer<? super E> op) {
        if (this instanceof Err(E error)) {
            op.accept(error);
        }

        return this;
    }

    default O expect(String message) {
        return switch (this) {
            case Ok(O value) -> value;
            case Err(E _) -> throw new IllegalStateException(message);
        };
    }

    default E expectErr(String message) {
        return switch (this) {
            case Ok(O _) -> throw new IllegalStateException(message);
            case Err(E error) -> error;
        };
    }

    default O unwrap() {
        return switch (this) {
            case Ok(O value) -> value;
            case Err(E _) -> throw new IllegalStateException("called `Result.unwrap()` on an `Err` value");
        };
    }

    default E unwrapErr() {
        return switch (this) {
            case Ok(O _) -> throw new IllegalStateException("called `Result.unwrapErr()` on an `Ok` value");
            case Err(E error) -> error;
        };
    }

    default O unwrapOr(O fallback) {
        return switch (this) {
            case Ok(O value) -> value;
            case Err(E _) -> fallback;
        };
    }

    default O unwrapOrElse(Supplier<? extends O> fallbackOp) {
        return switch (this) {
            case Ok(O value) -> value;
            case Err(E _) -> fallbackOp.get();
        };
    }

    default <NO> Result<NO, E> and(Result<NO, E> res) {
        return switch (this) {
            case Ok(O _) -> res;
            case Err(E error) -> err(error);
        };
    }

    default <NO> Result<NO, E> andThen(Supplier<? extends Result<NO, E>> op) {
        return switch (this) {
            case Ok(O _) -> op.get();
            case Err(E error) -> err(error);
        };
    }

    default <NE> Result<O, NE> or(Result<O, NE> res) {
        return switch (this) {
            case Ok(O value) -> ok(value);
            case Err(E _) -> res;
        };
    }

    default <NE> Result<O, NE> orElse(Supplier<? extends Result<O, NE>> op) {
        return switch (this) {
            case Ok(O value) -> ok(value);
            case Err(E _) -> op.get();
        };
    }


}
