package com.smartbear.soapui.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import com.eviware.soapui.SoapUI;

/**
 * Auto-configuration for SoapUI integration.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnClass(SoapUI.class)
@EnableConfigurationProperties(SoapuiProperties.class)
public class SoapuiAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SoapuiAutoConfiguration.class);

	private final ApplicationContext applicationContext;

	private final SoapuiProperties properties;

	public SoapuiAutoConfiguration(ApplicationContext applicationContext, SoapuiProperties properties) {
		this.applicationContext = applicationContext;
		this.properties = properties;
		logger.info("SoapuiAutoConfiguration initialized with prefix '{}'", SoapuiProperties.PREFIX);
	}

    /**
     * <p>Returns the application context.</p>
     * @return the get application context
     */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

    /**
     * <p>Returns the properties.</p>
     * @return the get properties
     */
	public SoapuiProperties getProperties() {
		return properties;
	}

}
