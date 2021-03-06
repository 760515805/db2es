package com.montnets.input_jdbc_es.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: GetProperties.java
* @Description: 该类的功能描述
*获取配置文件
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月9日 下午1:54:50 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月9日     chenhj          v1.0.0               修改原因
 */
public class GetProperties {
	private static Map<String,String> appSettings = new HashMap<String,String>();
	private static final Logger LOG = LoggerFactory.getLogger(GetProperties.class);
	private  String pathName;
	/**
	 * 初始化系统默认参数
	 */
	public GetProperties(String pathName){
		this.pathName=pathName;
		init();
	}
	
	private  void init(){
		InputStream in = null;
		try{
			//获取resource中的配置
			File file =new File(pathName);
			if(file.exists()){
				in=new FileInputStream(file);
			}else{
				in=GetProperties.class.getClassLoader().getResourceAsStream(pathName);
			}
			//获取项目同级的配置
			//
			Properties prop = new Properties();
			prop.load(in);
			Set<Entry<Object, Object>> buf = prop.entrySet();
			Iterator<Entry<Object, Object>> it = buf.iterator();
			while(it.hasNext()){
				Entry<Object, Object> t = it.next();
				appSettings.put((String)t.getKey(), (String)t.getValue());
			}
		}catch(IOException e){
			LOG.error("加载系统参数失败!",e);
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("加载系统参数失败!",e);
				}
			}
		}
	}
	
	/**
	 * 获取配置文件
	 * @param name 配置文件名称
	 * @return
	 */
	public synchronized  Map<String, String> getAppSettings() {
		if(null == appSettings || appSettings.isEmpty()){
				new GetProperties(pathName);
		}
		return appSettings;
	}

	public synchronized  void setAppSettings(Map<String, String> appSettings,String name) {
		if(null == appSettings || appSettings.isEmpty()){
			new GetProperties(pathName);
		}
		GetProperties.appSettings = appSettings;
	}
	   
	/**
	 * 方法测试
	 * @param args
	 */
	  public static void main(String[] args) {
		  Map<String, String> conf =new GetProperties("druid.properties").getAppSettings();
		  System.out.println("jdbc账号:"+conf.get("tableName"));
	  }
	
}
