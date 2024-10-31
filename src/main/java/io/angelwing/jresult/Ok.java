package io.angelwing.jresult;

/**
 * Ok represents the success branch of the {@link Result} type.
 * It contains the success value and never an error value.
 * <p>
 * It is encouraged to use the static factory method {@link Result#ok(Object)} to create an instance of Ok.
 *
 * @param <O> Type of success value.
 * @param <E> Type of error value.
 * @author dcadea
 * @see Result#ok(Object)
 * @since 1.0.0
 */
public record Ok<O, E>(O value) implements Result<O, E> {
    static Ok<?, ?> EMPTY = new Ok<>(null);
}
