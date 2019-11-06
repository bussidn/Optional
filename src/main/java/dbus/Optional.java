package dbus;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * see java doc : https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 * @param <T>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Optional<T> {

    private Optional() {}

    private static  <U> Optional<U> narrow(Optional<? extends U> opt) {
        //noinspection unchecked
        return (Optional<U>) opt;
    }

    public static <T> Optional<T> empty() {
        return Empty.getInstance();
    }

    public static <T> Optional<T> of(T value) {
        return new Some<>(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    abstract public T get();

    abstract public boolean isPresent();

    abstract public boolean isEmpty();

    abstract public void ifPresent(Consumer<? super T> action);

    public abstract void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction);

    public abstract Optional<T> filter(Predicate<? super T> predicate);

    public abstract <U> Optional<U> map(Function<? super T, ? extends U> mapper);

    public abstract <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper);

    public abstract Optional<T> or(Supplier<? extends Optional<? extends T>> supplier);

    public abstract Stream<T> stream();

    public abstract T orElse(T other);

    public abstract T orElseGet(Supplier<? extends T> supplier);

    public abstract T orElseThrow();

    public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    public abstract String toString();

    private static class Empty<T> extends Optional<T> {
        private static final Empty<?> EMPTY = new Empty();

        private Empty() {
        }

        static <T> Optional<T> getInstance() {
            //noinspection unchecked
            return (Optional<T>) EMPTY;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("No value present");
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void ifPresent(Consumer<? super T> action) {
            // no value to apply the action to
        }

        @Override
        public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
            emptyAction.run();
        }

        @Override
        public Optional<T> filter(Predicate<? super T> predicate) {
            requireNonNull(predicate);
            return this;
        }

        @Override
        public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
            return getInstance();
        }

        @Override
        public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper) {
            requireNonNull(mapper);
            return getInstance();
        }

        @Override
        public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier) {
            requireNonNull(supplier);
            return this;
        }

        @Override
        public Stream<T> stream() {
            return Stream.empty();
        }

        @Override
        public T orElse(T other) {
            return other;
        }

        @Override
        public T orElseGet(Supplier<? extends T> supplier) {
            return supplier.get();
        }

        @Override
        public T orElseThrow() {
            throw new NoSuchElementException("No value present");
        }

        @Override
        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        @Override
        public String toString() {
            return "Optional.empty";
        }
    }

    private static class Some<T> extends Optional<T> {

        private T value;

        public Some(T value) {
            this.value = requireNonNull(value);
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void ifPresent(Consumer<? super T> action) {
            if (this.value != null) {
                action.accept(this.value);
            }

        }

        @Override
        public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
            action.accept(this.value);
        }

        @Override
        public Optional<T> filter(Predicate<? super T> predicate) {
            return requireNonNull(predicate).test(this.value) ? this : empty();
        }

        @Override
        public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
            return ofNullable(requireNonNull(mapper).apply(this.value));
        }

        @Override
        public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper) {
            requireNonNull(mapper);
            return Optional.narrow(mapper.apply(this.value));
        }

        @Override
        public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier) {
            requireNonNull(supplier);
            return requireNonNull(narrow(supplier.get()));
        }

        @Override
        public Stream<T> stream() {
            return Stream.of(this.value);
        }

        @Override
        public T orElse(T other) {
            return value;
        }

        @Override
        public T orElseGet(Supplier<? extends T> supplier) {
            return value;
        }

        @Override
        public T orElseThrow() {
            return value;
        }

        @SuppressWarnings("RedundantThrows")
        @Override
        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Optional[%s]", this.value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Some<?> some = (Some<?>) o;
            return value.equals(some.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), value);
        }
    }
}
