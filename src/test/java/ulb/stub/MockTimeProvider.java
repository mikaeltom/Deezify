package ulb.stub;

import ulb.utils.TimeProvider;

/**
 * A mock implementation of the TimeProvider interface for use in unit tests.
 * Allows simulation and control of time progression.
 */
public class MockTimeProvider implements TimeProvider {

    private long currentTimeMillis = 0;

    // Method to get the simulated time in milliseconds
    @Override
    public double getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    // Method to increment the simulated time by the specified number of milliseconds.
    public void incrementTime(long increment) {
        currentTimeMillis += increment;
    }
}
