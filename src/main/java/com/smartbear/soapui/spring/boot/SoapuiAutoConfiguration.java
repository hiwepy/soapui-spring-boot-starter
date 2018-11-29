package com.smartbear.soapui.spring.boot;

import java.util.Iterator;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.DefaultEnvironment;
import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.settings.HttpSettings;
import com.eviware.soapui.support.scripting.groovy.SoapUIGroovyScriptEngine;
import com.eviware.soapui.support.scripting.js.JsScriptEngine;
import com.smartbear.soapui.template.SoapuiRequestTemplate;
import com.smartbear.soapui.template.SoapuiWsdlTemplate;
import com.smartbear.soapui.template.property.EnvironmentProperty;

@Configuration
@ConditionalOnClass(WsdlProject.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(SoapuiProperties.class)
public class SoapuiAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SoapuiAutoConfiguration.class);
	
	@Bean
	public JsScriptEngine jsScriptEngine() throws Exception {
		
		// 创建Javascript脚本解析引擎 
		JsScriptEngine engine = new JsScriptEngine(getClass().getClassLoader());
		
		
		return engine;
	}
	
	@Bean
	public SoapUIGroovyScriptEngine groovyScriptEngine() throws Exception {
		
		// 创建Groovy脚本解析引擎 
		SoapUIGroovyScriptEngine engine = new SoapUIGroovyScriptEngine(getClass().getClassLoader());
		
		
		return engine;
	}
	
	@Bean
	public Environment soapEnv(SoapuiProperties properties) throws Exception {
		Environment env = DefaultEnvironment.getInstance();
		EnvironmentProperty envProperties = properties.getEnv();
		env.setName(envProperties.getName());
		Properties pro = envProperties.getSettings();
		Iterator<Object> ite = pro.keySet().iterator();
		while (ite.hasNext()) {
			String name = String.valueOf(ite.next());
			env.addNewProperty(name, pro.getProperty(name));
		}
		return env;
	}
	
	@Bean
	public WsdlProject wsdlProject(SoapuiProperties properties, Environment soapEnv) throws Exception {
		
		// create new project
		WsdlProject project = new WsdlProject();
		
		project.setAbortOnError(properties.isAbortOnError());
		project.setActiveEnvironment(soapEnv);
		if(StringUtils.hasText(properties.getScriptAfterLoad())) {
			project.setAfterLoadScript(properties.getScriptAfterLoad());
		}
		if(StringUtils.hasText(properties.getScriptAfterRun())) {
			project.setAfterRunScript(properties.getScriptAfterRun());
		}
		if(StringUtils.hasText(properties.getScriptBeforeRun())) {
			project.setBeforeRunScript(properties.getScriptBeforeRun());
		}
		if(StringUtils.hasText(properties.getScriptBeforeSave())) {
			project.setBeforeSaveScript(properties.getScriptBeforeSave());
		}
		project.setCacheDefinitions(properties.isCacheDefinitions());
		project.setDefaultScriptLanguage(properties.getScriptLanguage().getName());
		project.setDescription(properties.getDescription());
		project.setEncryptionStatus(properties.getEncryptionStatus());
		if(StringUtils.hasText(properties.getHermesConfigPath())) {
			project.setHermesConfig(properties.getHermesConfigPath());
		}
		project.setName(properties.getName());
		Properties pro = properties.getProperties();
		Iterator<Object> ite = pro.keySet().iterator();
		while (ite.hasNext()) {
			String name = String.valueOf(ite.next());
			project.setPropertyValue(name, pro.getProperty(name));
		}
		if(StringUtils.hasText(properties.getResourceRoot())) {
			project.setResourceRoot(properties.getResourceRoot());
		}
		project.setRunType(properties.getRunType());
		if(StringUtils.hasText(properties.getPassword())) {
			project.setShadowPassword(properties.getPassword());
		}
		project.setTimeout(properties.getTimeout());
		
		return project;
	}
	
	@Bean
	public SoapuiWsdlTemplate soapuiWsdlTemplate(WsdlProject wsdlProject ,SoapuiProperties properties) throws Exception {
		return new SoapuiWsdlTemplate(wsdlProject, properties);
	}
	
	@Bean
	public SoapuiRequestTemplate soapuiRequestTemplate(SoapuiWsdlTemplate soapuiWsdlTemplate ) throws Exception {
		return new SoapuiRequestTemplate(soapuiWsdlTemplate);
	}
	
	@PostConstruct
	public void init() {
		SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
	}
	
}
