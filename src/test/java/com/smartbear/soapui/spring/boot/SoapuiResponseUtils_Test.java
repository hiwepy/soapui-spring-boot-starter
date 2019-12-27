/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.smartbear.soapui.template.utils.SoapuiResponseUtils;


public class SoapuiResponseUtils_Test {
	
	/**
	 * At Least One Result
	 */
	@Test
	public void testLeastOneResult() throws IOException, Exception {
		System.err.println("========================================");	
		String soapResponseBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getWeatherbyCityNameResponse xmlns=\"http://WebXml.com.cn/\">\r\n" + 
				"      <getWeatherbyCityNameResult>\r\n" + 
				"        <string>浙江</string>\r\n" + 
				"		<string>杭州</string>\r\n" + 
				"		<string>58457</string>\r\n" + 
				"		<string>58457.jpg</string>\r\n" + 
				"		<string>2018/5/30 10:17:08</string>\r\n" + 
				"		<string>20℃/28℃</string>\r\n" + 
				"		<string>5月30日 阵雨转中雨</string>\r\n" + 
				"		<string>东风转东北风小于3级</string>\r\n" + 
				"		<string>3.gif</string>\r\n" + 
				"		<string>8.gif</string>\r\n" + 
				"		<string>今日天气实况：气温：26℃；风向/风力：西南风 1级；湿度：74%；紫外线强度：弱。空气质量：良。</string>\r\n" + 
				"		<string>紫外线指数：弱，辐射较弱，涂擦SPF12-15、PA+护肤品。\r\n" + 
				"		健臻·血糖指数：易波动，血糖易波动，注意监测。\r\n" + 
				"		穿衣指数：舒适，建议穿长袖衬衫单裤等服装。\r\n" + 
				"		洗车指数：不宜，有雨，雨水和泥水会弄脏爱车。\r\n" + 
				"		空气污染指数：良，气象条件有利于空气污染物扩散。\r\n" + 
				"		</string>\r\n" + 
				"		<string>17℃/23℃</string>\r\n" + 
				"		<string>5月31日 小到中雨转多云</string>\r\n" + 
				"		<string>东北风小于3级</string>\r\n" + 
				"		<string>21.gif</string>\r\n" + 
				"		<string>1.gif</string>\r\n" + 
				"		<string>16℃/26℃</string>\r\n" + 
				"		<string>6月1日 多云转晴</string>\r\n" + 
				"		<string>东风小于3级</string>\r\n" + 
				"		<string>1.gif</string>\r\n" + 
				"		<string>0.gif</string>		"
				+ "<string>杭州市是浙江省省会，国务院确定的全国重点风景旅游城市和历史文化名城，位于北纬30°16’、东经120°12’，地处长江三角洲南翼，杭州湾西端，钱塘江下游，京杭大运河南端，东濒杭州湾、钱塘江，南与金华市、衢州市、绍兴市相接，西与安徽省黄山市交界，北与湖州市、嘉兴市相邻。下辖8个区、5个县（市），全市总面积16596平方千米，其中市区面积3068平方千米。改革开放以来，杭州经济发展迅猛，2007年全市实现国内生产总值4103.89亿元，人均GDP达8063美元，连续17年保持2位数增长，连续4年被誉为“中国最具幸福感的城市”。\r\n" + 
				"杭州尤以西湖秀丽迷人的自然风光闻名于世。美丽的西湖三面环山，一面濒城，两堤卧波，三岛浮水，风景秀丽，四季异色，古迹珠连，名人荟萃，历代诗人吟咏不绝。杭州自然景观和人文景观十分丰富，文物、古迹众多，古代庭、园、楼、阁、塔、寺、泉、壑、石窟、摩崖碑刻遍布，众多景点或诡异神秘、内蕴深沉，或珠帘玉带、烟柳画桥，或万千姿态、蔚然奇观，或山青水秀、风情万般。全市现有60多个对外开放景点和40多处重点文物保护单位，以灵隐寺、六和塔、飞来峰、岳庙、西泠印社、三潭印月、花港观鱼、龙井、虎跑等最为著名。</string>\r\n" + 
				"      </getWeatherbyCityNameResult>\r\n" + 
				"    </getWeatherbyCityNameResponse>\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
	
		String[] result = SoapuiResponseUtils.parseResponseToArray(soapResponseBody, SoapVersion.Soap11);
		System.out.println(StringUtils.join(result,","));
		
	}
	
	/**
	 * No Result
	 */
	@Test
	public void testNoResult() throws IOException, Exception {
		System.err.println("========================================");	
		String soapResponseBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getSupportProvinceResponse xmlns=\"http://WebXml.com.cn/\">\r\n" + 
				"      <getSupportProvinceResult/>\r\n" + 
				"    </getSupportProvinceResponse>\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
  
		String[] result = SoapuiResponseUtils.parseResponseToArray(soapResponseBody, SoapVersion.Soap11);
		System.out.println(StringUtils.join(result,","));
		
	}
	 
	
}
