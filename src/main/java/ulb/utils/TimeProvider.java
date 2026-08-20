package ulb.utils;

/**
 * A functional interface that provides the current time in milliseconds.
 * This interface is used to abstract the time source, allowing for easier
 * testing and mocking.
 */
@FunctionalInterface
public interface TimeProvider {
    double getCurrentTimeMillis();
}
