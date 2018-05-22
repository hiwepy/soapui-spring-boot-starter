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

import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Predicate;

/**
 * https://my.oschina.net/u/3648248/blog/1526211
 */
public class Reflections_Test03 {

	/**
	 * 首先要通过过滤器指定包/类/文件资源，因为ClassLoader要依靠指定的过滤器去找出
	 * 符合条件的资源。然后通过api：reflections.getResources(regex)找出符合条件 的资源。
	 */
	public static void main(String[] args) {
		Predicate<String> filter = new FilterBuilder().include(".*\\.properties")

				.exclude(".*testModel-reflections\\.xml");
		Reflections reflections = new Reflections(new ConfigurationBuilder().filterInputsBy(filter)
				.setScanners(new ResourcesScanner()).setUrls(ClasspathHelper.forPackage("com.jeff.")));

		Set<String> str = reflections.getResources(Pattern.compile(".*\\.properties"));
		for (String s : str) {
			System.out.println(s);
		}
	}

}
