package dbus;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OptionalTest {

    @Rule
    public ExpectedException ee = ExpectedException.none();

    private final Optional<String> empty = Optional.empty();
    private final Optional<String> nonEmpty = Optional.of("test");

    @Test
    public void ofNullable_should_return_empty_optional_when_provided_value_is_null() {
        assertThat(Optional.ofNullable(null)).isEqualTo(empty);
    }

    @Test
    public void ofNullable_should_return_non_empty_optional_when_provided_value_is_non_null() {
        assertThat(Optional.ofNullable(5)).isEqualTo(Optional.of(5));
    }

    @Test
    public void get_should_throw_on_empty_optional() {
        // expect
        ee.expect(NoSuchElementException.class);
        ee.expectMessage("No value present");

        // when
        empty.get();
    }

    @Test
    public void get_should_return_provided_value_on_non_empty_optional() {
        assertThat(nonEmpty.get()).isEqualTo("test");
    }

    @Test
    public void isPresent_should_return_false_on_empty_optional() {
        assertThat(empty.isPresent()).isFalse();
    }

    @Test
    public void isPresent_should_return_true_on_empty_optional() {
        assertThat(nonEmpty.isPresent()).isTrue();
    }

    @Test
    public void isEmpty_should_return_true_on_empty_optional() {
        assertThat(empty.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_should_return_false_on_empty_optional() {
        assertThat(nonEmpty.isEmpty()).isFalse();
    }

    /**
     * This way of testing is not a proper one.
     * I'm only using a trick to be able to test this method
     * <p>
     * We'll see on lesson about Mocks how to test this properly
     */
    @Test
    public void ifPresent_should_do_nothing_on_empty_optional() {
        // given
        Integer[] ints = new Integer[]{0};

        // when
        empty.ifPresent(s -> ints[0]++);

        // then
        assertThat(ints[0]).isEqualTo(0);
    }

    /**
     * This way of testing is not a proper one.
     * I'm only using a trick to be able to test this method
     * <p>
     * We'll see on lesson about Mocks how to test this properly
     */
    @Test
    public void ifPresent_should_execute_consumer_on_non_empty_optional() {
        // given
        Integer[] ints = new Integer[]{0};

        // when
        nonEmpty.ifPresent(s -> ints[0]++);

        // then
        assertThat(ints[0]).isEqualTo(1);
    }

    /**
     * This way of testing is not a proper one.
     * I'm only using a trick to be able to test this method
     * <p>
     * We'll see on lesson about Mocks how to test this properly
     */
    @Test
    public void ifPresentOrElse_should_execute_else_runnable_on_empty_optional() {
        // given
        Integer[] ints = new Integer[]{0};

        // when
        empty.ifPresentOrElse(s -> ints[0]++, () -> ints[0]--);

        // then
        assertThat(ints[0]).isEqualTo(-1);
    }

    /**
     * This way of testing is not a proper one.
     * I'm only using a trick to be able to test this method
     * <p>
     * We'll see on lesson about Mocks how to test this properly
     */
    @Test
    public void ifPresentOrElse_should_execute_consumer_on_non_empty_optional() {
        // given
        Integer[] ints = new Integer[]{0};

        // when
        nonEmpty.ifPresentOrElse(s -> ints[0]++, () -> {
            ints[0]++;
            ints[0]++;
        });

        // then
        assertThat(ints[0]).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void filter_should_throw_NullPointerException_on_empty_when_provided_predicate_is_null() {
        empty.filter(null);
    }

    @Test(expected = NullPointerException.class)
    public void filter_should_throw_NullPointerException_on_non_empty_when_provided_predicate_is_null() {
        nonEmpty.filter(null);
    }

    @Test
    public void filter_should_return_empty_optional_on_empty_optional() {
        assertThat(empty.filter(s -> true)).isEqualTo(empty);
    }

    @Test
    public void filter_should_return_empty_optional_when_predicate_return_false() {
        assertThat(nonEmpty.filter(s -> false)).isEqualTo(empty);
    }

    @Test
    public void filter_should_return_same_optional_when_predicate_return_true() {
        assertThat(nonEmpty.filter(s -> s.startsWith("t"))).isEqualTo(nonEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void map_should_throw_NullPointerException_on_empty_when_provided_mapper_is_null() {
        empty.map(null);
    }

    @Test(expected = NullPointerException.class)
    public void map_should_throw_NullPointerException_on_non_empty_when_provided_mapper_is_null() {
        nonEmpty.map(null);
    }

    @Test
    public void map_should_return_empty_optional_on_empty_optional() {
        assertThat(empty.map(identity())).isEqualTo(empty);
    }

    @Test
    public void map_should_return_mappedy_optional_on_non_empty_optional() {
        assertThat(nonEmpty.map(s -> 4)).isEqualTo(Optional.of(4));
    }

    @Test(expected = NullPointerException.class)
    public void flatMap_should_throw_NullPointerException_on_empty_when_provided_mapper_is_null() {
        empty.flatMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void flatMap_should_throw_NullPointerException_on_non_empty_when_provided_mapper_is_null() {
        nonEmpty.flatMap(null);
    }

    @Test
    public void flatMap_should_return_empty_optional_on_empty() {
        assertThat(empty.flatMap(s -> null)).isEqualTo(empty);
    }

    @Test(expected = NullPointerException.class)
    public void flatMap_should_throw_NullPointerException_on_non_empty_when_provided_mapper_returns_null() {
        nonEmpty.flatMap(s -> null);
    }

    @Test
    public void flatMap_should_map_on_non_empty_optional() {
        assertThat(nonEmpty.flatMap(s -> Optional.of(7))).isEqualTo(Optional.of(7));
    }

    @Test(expected = NullPointerException.class)
    public void or_should_throw_NullPointerException_on_empty_when_provided_supplier_is_null() {
        empty.or(null);
    }

    @Test(expected = NullPointerException.class)
    public void or_should_throw_NullPointerException_on_non_empty_when_provided_supplier_is_null() {
        nonEmpty.or(null);
    }

    @Test
    public void or_should_provide_supplier_s_value_on_empty_optional() {
        assertThat(empty.or(() -> Optional.of("alternative value")))
                .isEqualTo(Optional.of("alternative value"));
    }

    @Test
    public void or_should_provide_optional_s_value_on_non_empty_optional() {
        assertThat(nonEmpty.or(() -> Optional.of("alternative value")))
                .isEqualTo(nonEmpty);
    }

    @Test
    public void stream_should_return_empty_stream_on_empty_optional() {
        assertStreamEquals(empty.stream(), Stream.empty());
    }

    @Test
    public void stream_should_return_non_empty_stream_on_non_empty_optional() {
        assertStreamEquals(nonEmpty.stream(), Stream.of("test"));
    }


    private static void assertStreamEquals(Stream<?> s1, Stream<?> s2) {
        Iterator<?> iter1 = s1.iterator(), iter2 = s2.iterator();
        while (iter1.hasNext() && iter2.hasNext())
            assertThat(iter1.next()).isEqualTo(iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    public void orElse_should_provide_provided_value_on_empty() {
        assertThat(empty.orElse("alternate value"))
                .isEqualTo("alternate value");
    }

    @Test
    public void orElse_should_provide_optional_value_on_non_empty() {
        assertThat(nonEmpty.orElse("alternate value"))
                .isEqualTo("test");
    }

    @Test(expected = NullPointerException.class)
    public void orElseGet_should_throw_NullPointerException_on_empty_optional_when_supplier_is_null() {
        empty.orElseGet(null);
    }

    @Test
    public void orElseGet_should_provide_supplier_s_value_on_empty_optional() {
        assertThat(empty.orElseGet(() -> "alternate value"))
                .isEqualTo("alternate value");
    }

    @Test
    public void orElseGet_should_provide_optional_value_on_non_empty() {
        assertThat(nonEmpty.orElseGet(() -> "alternate value"))
                .isEqualTo("test");
    }

    @Test
    public void orElseThrow_should_throw_NoSuchElementException_on_empty_optional() {
        ee.expect(NoSuchElementException.class);
        ee.expectMessage("No value present");

        empty.orElseThrow();
    }

    @Test
    public void orElseThrow_should_return_optional_value_on_non_empty_optional() {
        assertThat(nonEmpty.orElseThrow()).isEqualTo("test");
        assertThat(nonEmpty.orElseThrow(() -> null)).isEqualTo("test");
    }

    @Test(expected = MyException.class)
    public void orElseThrow_should_throw_supplier_s_exception_on_empty_optional() throws MyException {
        empty.orElseThrow(MyException::new);
    }

    @Test(expected = NullPointerException.class)
    public void orElseThrow_should_throw_NullPointerException_on_empty_optional_when_supplier_is_null() {
        empty.orElseThrow(null);
    }

    private static class MyException extends Exception {
    }

    @Test
    public void toString_should_return_optional_empty_on_empty_optional() {
        assertThat(empty.toString()).isEqualTo("Optional.empty");
    }

    @Test
    public void toString_should_return_optional_with_value_on_non_empty_optional() {
        assertThat(nonEmpty.toString()).isEqualTo("Optional[test]");
    }

    @Test
    public void empty_optionals_should_have_same_hashcode() {
        assertThat(empty.hashCode()).isEqualTo(Optional.empty().hashCode());
    }

    @Test
    public void non_empty_equal_optionals_should_have_same_hashcode() {
        assertThat(nonEmpty.hashCode()).isEqualTo(Optional.of("test").hashCode());
    }
}