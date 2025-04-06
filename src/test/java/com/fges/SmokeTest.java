package com.fges;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A basic smoke test to ensure the test framework is set up correctly.
 * This test acts as a sanity check and should always pass.
 */
class SmokeTest {

    /**
     * Verifies that the test environment is functional.
     * This test is expected to always pass.
     */
    @Test
    void should_always_pass() {
        assertThat(true).isTrue();
    }
}
