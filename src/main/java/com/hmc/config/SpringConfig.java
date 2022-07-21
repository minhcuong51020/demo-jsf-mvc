package com.hmc.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.hmc")
@PropertySource(value = {"classpath:hibernate_config.properties"})
@EnableTransactionManagement
public class SpringConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(env.getProperty("driver"));
		driverManagerDataSource.setUrl(env.getProperty("url"));
		driverManagerDataSource.setUsername(env.getProperty("user"));
		driverManagerDataSource.setPassword(env.getProperty("password"));
		System.out.println("dmd 1:" + driverManagerDataSource);
		return driverManagerDataSource;
	}
	
	@Bean
	@Autowired
	public SessionFactory sessionFactory(DataSource dataSource) {
		System.out.println("dmd 2:" + dataSource);
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan("com.hmc.entity");
		
		Properties props = new Properties();
		props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		bean.setHibernateProperties(props);
		try {
			bean.afterPropertiesSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("sf 1: " + bean.getObject());
		return bean.getObject();
	}
	
	@Bean(name="transactionManager")
	@Autowired
	public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
		System.out.println("sf 2: " + sessionFactory);
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory);
		return hibernateTransactionManager;
	}
	
}
