// SmokeTest.java

package com.fges;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {

    @Test
    void should_always_pass() {
        assertThat(true).isTrue();
    }
}
