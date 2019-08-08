package com.sas.tools;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGeneration {
	/**
      * 
      * @Title: main
      * @Description: 生成
      * @param args
      */
	public static void main(String[] args) {
		AutoGenerator mpg = new AutoGenerator();
		
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir("D://temp//code");
		gc.setFileOverride(true);
		gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(false);// XML columList
		gc.setAuthor("mj");// 作者


		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		gc.setControllerName("%sController");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		mpg.setGlobalConfig(gc);


		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.POSTGRE_SQL);
		dsc.setDriverName("org.postgresql.Driver");
		dsc.setUsername("postgres");
		dsc.setPassword("system");
		dsc.setSchemaName("public");
		dsc.setUrl("jdbc:postgresql://10.1.100.157:5432/cigem_dms");
		mpg.setDataSource(dsc);

		
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setTablePrefix(new String[] { "tb_" });// 此处可以修改为您的表前缀
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		strategy.setInclude(new String[]{
				"tb_catalog",
//				"tb_node",
//				"tb_order",
//				"tb_order_info",
//				"tb_shop"
				}); // 需要生成的表new String[] { "user","role","permission" }


		strategy.setSuperServiceClass(null);
		strategy.setSuperServiceImplClass(null);
		strategy.setSuperMapperClass(null);


		mpg.setStrategy(strategy);
		
		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.sas");
		pc.setModuleName("catalog");
		pc.setController("controller");
		pc.setService("service");
		pc.setServiceImpl("service.impl");
		pc.setMapper("mapper");
		pc.setEntity("entity");
		//        pc.setXml("xml");
		mpg.setPackageInfo(pc);
		


		// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
		// 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
		TemplateConfig tc = new TemplateConfig();
		tc.setController("/templates/controller.java.vm");
		tc.setService("/templates/service.java.vm");
		tc.setServiceImpl("/templates/serviceImpl.java.vm");
		tc.setEntity("/templates/entity.java.vm");
		tc.setMapper("/templates/mapper.java.vm");
//		tc.setXml("/templates/mapper.xml.vm");
		// 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
		mpg.setTemplate(tc);

		// 执行生成
		mpg.execute();


	}
}
