package com.smartbear.soapui.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SoapuiProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class SoapuiPropertiesTest {

    @Test
    void prefix() {
        assertThat(SoapuiProperties.PREFIX).isEqualTo("soapui");
    }

    @Test
    void canCreateInstance() {
        SoapuiProperties props = new SoapuiProperties();
        assertThat(props).isNotNull();
    }

    @Test
    void settingsNotNull() {
        SoapuiProperties props = new SoapuiProperties();
        assertThat(props.getSettings()).isNotNull();
    }

    @Test
    void timeoutDefaultValue() {
        SoapuiProperties props = new SoapuiProperties();
        assertThat(props.getTimeout()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void envNotNull() {
        SoapuiProperties props = new SoapuiProperties();
        assertThat(props.getEnv()).isNotNull();
    }

}
