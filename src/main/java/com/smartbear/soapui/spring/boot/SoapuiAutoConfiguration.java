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

import com.eviware.soapui.DefaultSoapUICore;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.SoapUICore;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.DefaultEnvironment;
import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.model.settings.Settings;
import com.eviware.soapui.support.scripting.groovy.SoapUIGroovyScriptEngine;
import com.eviware.soapui.support.scripting.js.JsScriptEngine;
import com.smartbear.soapui.template.SoapuiRequestTemplate;
import com.smartbear.soapui.template.SoapuiWsdlTemplate;
import com.smartbear.soapui.template.property.EnvironmentProperty;
import com.smartbear.soapui.template.setting.SoapuiProjectSettings;
import com.smartbear.soapui.template.setting.SoapuiSettingsImpl;

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
		
		SoapuiProjectSettings projectSettings = properties.getSettings().getProject();
		
		project.setAbortOnError(projectSettings.isAbortOnError());
		project.setActiveEnvironment(soapEnv);
		if(StringUtils.hasText(projectSettings.getScriptAfterLoad())) {
			project.setAfterLoadScript(projectSettings.getScriptAfterLoad());
		}
		if(StringUtils.hasText(projectSettings.getScriptAfterRun())) {
			project.setAfterRunScript(projectSettings.getScriptAfterRun());
		}
		if(StringUtils.hasText(projectSettings.getScriptBeforeRun())) {
			project.setBeforeRunScript(projectSettings.getScriptBeforeRun());
		}
		if(StringUtils.hasText(projectSettings.getScriptBeforeSave())) {
			project.setBeforeSaveScript(projectSettings.getScriptBeforeSave());
		}
		project.setCacheDefinitions(projectSettings.isCacheDefinitions());
		project.setDefaultScriptLanguage(projectSettings.getScriptLanguage().getName());
		project.setDescription(projectSettings.getDescription());
		project.setEncryptionStatus(projectSettings.getEncryptionStatus());
		if(StringUtils.hasText(projectSettings.getHermesConfigPath())) {
			project.setHermesConfig(projectSettings.getHermesConfigPath());
		}
		project.setName(projectSettings.getName());
		Properties pro = projectSettings.getProperties();
		Iterator<Object> ite = pro.keySet().iterator();
		while (ite.hasNext()) {
			String name = String.valueOf(ite.next());
			project.setPropertyValue(name, pro.getProperty(name));
		}
		if(StringUtils.hasText(projectSettings.getResourceRoot())) {
			project.setResourceRoot(projectSettings.getResourceRoot());
		}
		project.setRunType(projectSettings.getRunType());
		if(StringUtils.hasText(projectSettings.getPassword())) {
			project.setShadowPassword(projectSettings.getPassword());
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
	
	@Bean
	public Settings soapuiSettings(SoapuiProperties properties ) throws Exception {
		return new SoapuiSettingsImpl(properties.getSettings());
	}
	
	@Bean
	public SoapUICore soapuiCore(Settings soapuiSettings) throws Exception {
		SoapUICore soapUICore = new DefaultSoapUICore() {
			@Override
			public Settings getSettings() {
				return soapuiSettings;
			}
		};
		SoapUI.setSoapUICore(soapUICore);
		return soapUICore;
	}
	
	@PostConstruct
	public void init() {
		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
	}
	
}
