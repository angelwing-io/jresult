package io.angelwing.jresult;

import static java.util.Objects.requireNonNull;

/**
 * Err represents the error branch of the {@link Result} type.
 * It contains the error value and never a success value.
 * <p>
 * It is encouraged to use the static factory method {@link Result#err(Object)} to create an instance of Err.
 *
 * @param <O> Type of success value.
 * @param <E> Type of error value.
 * @author dcadea
 * @see Result#err(Object)
 * @since 1.0.0
 */
public record Err<O, E>(E error) implements Result<O, E> {

    public Err {
        requireNonNull(error, "Error cannot be null");
    }

}
