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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;

public class Reflections_Test02 {

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        Reflections reflections = ScanUtils.init();
        /**
         * 注意要获取拥有某个注解的方法，注意这个注解所在的包一定要配置到我们的includePackage
         * 或者includeRegex下，否则获取不到对应的方法
         */
        Set<Method> methods = reflections.getMethodsAnnotatedWith(MyAnotation.class);
        for(Method m : methods){
               System.out.println(m.getName());
        }
        /**
         *获取Object的子类，注意：Object这个类或者它的包，要配置在includePackage下，
         *另外得到的结果也一定是在“配置包”路径下面的类：includePackage=com.jeff.reflection.bean,
         *com.jeff.reflection.test,com.jeff.anotation,java.lang.Object
         */
        Set sub = reflections.getSubTypesOf(Object.class);
        System.out.println(sub);
        
        /**
         * 得到String为返回值的所有方法
         */
        methods = reflections.getMethodsReturn(String.class);
        for(Method m : methods){
               System.out.println(m.getName());
        }
        
        /**
         * 获取构造方法中参数带ParamAnotation.class注解的所有构造方法
         */
        Set<Constructor> cons = reflections.

                            getConstructorsWithAnyParamAnnotated(ParamAnotation.class);
        for(Constructor c : cons){
            System.out.println(c.getModifiers() + "," + c.getName());//1,com.jeff.reflection.bean.User
        }
        
        /**
         * 获取该注解上面的修饰的注解类型（该注解一定是在我们配置的包路径下）
         * 由于@Documented注解不在我们配置的包路径下，下面的方法获取不了它的
         * 注解
         * @Documented
         * @ParamAnotation(name="")
         * public @interface MyAnotation {
         * }
         */
        Set anot = reflections.getTypesAnnotatedWith(ParamAnotation.class,true);
        System.out.println(anot);//[interface com.jeff.anotation.MyAnotation]
        
        /**
         * 获取资源：从reflections晒选出符合条件（正则）的资源，所以在includePackage
         * 配置这个下面资源的路径，要不筛选不出来。（查看Test03.java）
         */
        Set<String> res = reflections.getResources(Pattern.compile(".*\\.properties"));
        for(Iterator<String> ite = res.iterator();ite.hasNext();){
            String str = ite.next();
            System.out.println(str);
        }
    }
}