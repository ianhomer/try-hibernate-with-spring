/*
 * Copyright (c) 2017 the original author or authors. All Rights Reserved
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.purplepip.exercise.hibernate;

import com.purplepip.exercise.hibernate.repositories.PersonRepository;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.purplepip.exercise.hibernate"})
@EnableJpaRepositories(basePackages = {"com.purplepip.exercise.hibernate"})
@PropertySource(value = "classpath:jdbc-${database:h2}.properties")
@EnableTransactionManagement
public class AppConfiguration {
  @Autowired
  PersonRepository personRepository;

  @Bean
  DataSource dataSource(@Value("${jdbc.driverClassName}") String driverClassName,
                        @Value("${jdbc.url}") String url,
                        @Value("${jdbc.username}") String username,
                        @Value("${jdbc.password}") String password) {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  EntityManagerFactory entityManagerFactory(
      @Autowired DataSource dataSource,
      @Autowired JpaVendorAdapter jpaVendorAdapter,
      @Value("${hibernate.dialect}") String hibernateDialect) {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(dataSource);
    factory.setJpaVendorAdapter(jpaVendorAdapter);
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", hibernateDialect);
    // Note that for this exercise we recreate data from scratch each time
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    factory.setJpaProperties(properties);
    factory.setPackagesToScan("com.purplepip.exercise.hibernate");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  JpaTransactionManager transactionManager(
      @Autowired EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager manager = new JpaTransactionManager();
    manager.setEntityManagerFactory(entityManagerFactory);
    return manager;
  }
}
