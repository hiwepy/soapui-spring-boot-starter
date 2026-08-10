package com.smartbear.soapui.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link SoapuiAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class SoapuiAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SoapuiAutoConfiguration.class));

    @Test
    void propertiesAreBound() {
        this.contextRunner
                .withPropertyValues("soapui.timeout=5000")
                .run(context -> {
                    assertThat(context).hasSingleBean(SoapuiProperties.class);
                    SoapuiProperties props = context.getBean(SoapuiProperties.class);
                    assertThat(props.getTimeout()).isEqualTo(5000);
                });
    }

    @Test
    void autoConfigurationBeanHasGetters() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SoapuiAutoConfiguration.class);
            SoapuiAutoConfiguration config = context.getBean(SoapuiAutoConfiguration.class);
            assertThat(config.getProperties()).isNotNull();
            assertThat(config.getApplicationContext()).isNotNull();
        });
    }

}
