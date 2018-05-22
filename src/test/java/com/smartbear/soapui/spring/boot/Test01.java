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

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

public class Test01 {

	private static String packNameList = "com.jeff.reflection.bean," + 
	"com.jeff.reflection.test,com.jeff.anotation";

	public static void main(String[] args) {
		FilterBuilder filterBuilder = new FilterBuilder();

		for (String packName : packNameList.split(",")) {
			filterBuilder = filterBuilder.includePackage(packName);// 定义要扫描的包
		}
		Predicate<String> filter = filterBuilder;// 过滤器

		// 里面放的是这些包所在的资源路径
		Collection<URL> urlTotals = new ArrayList<URL>();
		for (String packName : packNameList.split(",")) {
			Collection<URL> urls = ClasspathHelper.forPackage(packName);
			urlTotals.addAll(urls);
		}

		/**
		 * 定义Reflections对象，指明"包过滤器"，以及扫描器的类型，主要把是扫描器的类型 细分之后，得到对应的数据
		 */
		Reflections reflections = new Reflections(new ConfigurationBuilder()

				.filterInputsBy(filter).setScanners(

						new SubTypesScanner().filterResultsBy(filter),
						new TypeAnnotationsScanner().filterResultsBy(filter),
						new FieldAnnotationsScanner().filterResultsBy(filter),
						new MethodAnnotationsScanner().filterResultsBy(filter),
						new MethodParameterScanner().filterResultsBy(filter)

				).setUrls(urlTotals));

		// 获取方法上待MyAnotation注解的所有的方法
		Set<Method> methods = reflections.getMethodsAnnotatedWith(MyAnotation.class);
		for (Method m : methods) {
			System.out.println(m.getName());
		}
	}
}