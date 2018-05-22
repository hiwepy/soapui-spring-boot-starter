/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.smartbear.soapui.spring.boot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Predicate;

public class ScanUtils {

    private static final String  SPLIT_STR = ",";//通过","切割包
    
    private static Properties pro = null;
    
    private static Reflections reflections = null;
    
    static{
        InputStream in = ScanUtils.class.getClassLoader()
        		.getResourceAsStream("reflection.properties");
        pro = new Properties();
        try {
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化
     * @return
     */
    public static Reflections init(){
        //定义过滤器集合
        FilterBuilder filterBuilder = new FilterBuilder();
        Collection<URL> urlTotals = new ArrayList<URL>();
        //初始化过滤器集合
        initPackage(filterBuilder,urlTotals);
        Predicate<String> filter = filterBuilder;
        
        reflections = new Reflections(new ConfigurationBuilder().filterInputsBy(filter)
                .setScanners(new SubTypesScanner().filterResultsBy(filter),
                        new TypeAnnotationsScanner()
                                .filterResultsBy(filter),
                        new FieldAnnotationsScanner()
                                .filterResultsBy(filter),
                        new MethodAnnotationsScanner()
                                .filterResultsBy(filter),
                        new MethodParameterScanner().filterResultsBy(filter)).setUrls(urlTotals));
        
        return reflections;
    }
    
    /**
     * 获取Reflections对象
     * @return
     */
    public static Reflections getReflections() {
        return reflections;
    }
    
    /**
     * 初始化配置文件的包 reflection.properties
     * @param filterBuilder
     */
    private static void initPackage(FilterBuilder filterBuilder,Collection<URL> urlTotals){
        
        String includePackage = pro.getProperty("includePackage");
        if(StringUtils.isNotEmpty(includePackage)){
            for (String packName : includePackage.split(SPLIT_STR)) {
                filterBuilder = filterBuilder.includePackage(packName);
                
                /**
                 * 通过ClasspathHelper（ClassLoader）类加载去去加载该包路径下的所有资源(URL),
                 * 最终通过URL资源（类、方法，注解，修饰符等等）, 结合Filter可以得到Reflections
                 * 对象。
                 */
                Collection<URL> urls = ClasspathHelper.forPackage(packName);
                urlTotals.addAll(urls);
            }
        }
        String includeRegex = pro.getProperty("includeRegex");
        if(StringUtils.isNotEmpty(includeRegex)){
            for (String packName : includeRegex.split(SPLIT_STR)) {
                filterBuilder = filterBuilder.include(packName);
                
                Collection<URL> urls = ClasspathHelper.forPackage(packName);
                urlTotals.addAll(urls);
            }
        }
        
        String excludePackage = pro.getProperty("excludePackage");
        if(StringUtils.isNotEmpty(excludePackage)){
            for (String packName : excludePackage.split(SPLIT_STR)) {
                filterBuilder = filterBuilder.excludePackage(packName);
                
                Collection<URL> urls = ClasspathHelper.forPackage(packName);
                urlTotals.addAll(urls);
            }
        }
        
        String excludeRegex = pro.getProperty("excludeRegex");
        if(StringUtils.isNotEmpty(excludeRegex)){
            for (String packName : excludeRegex.split(SPLIT_STR)) {
                filterBuilder = filterBuilder.includePackage(packName);
                
                Collection<URL> urls = ClasspathHelper.forPackage(packName);
                urlTotals.addAll(urls);
            }
        }
    }
}