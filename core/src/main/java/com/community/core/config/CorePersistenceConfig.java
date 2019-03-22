package com.community.core.config;

import org.broadleafcommerce.common.extensibility.context.merge.Merge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.ldap.NamingException;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
public class CorePersistenceConfig {

    @Autowired
    @Qualifier("webDS")
    DataSource webDS;

    @Autowired
    @Qualifier("webSecureDS")
    DataSource webSecureDS;

    @Autowired
    @Qualifier("webStorageDS")
    DataSource webStorageDS;

    @Bean
    public MapFactoryBean blMergedDataSources() throws Exception {
        MapFactoryBean mapFactoryBean = new MapFactoryBean();
        Map<String, DataSource> sourceMap = new HashMap<>();
        sourceMap.put("jdbc/web", webDS);
        sourceMap.put("jdbc/webSecure", webSecureDS);
        sourceMap.put("jdbc/cmsStorage", webStorageDS);
        mapFactoryBean.setSourceMap(sourceMap);

        return mapFactoryBean;
    }
    
    @Merge(targetRef = "blMergedPersistenceXmlLocations", early = true)
    public List<String> corePersistenceXmlLocations() {
        return Arrays.asList("classpath*:/META-INF/persistence-core.xml");
    }
    
    @Merge(targetRef = "blMergedEntityContexts", early = true)
    public List<String> entityConfigurationLocations() {
        return Arrays.asList("classpath:applicationContext-entity.xml");
    }

    @Bean
    public DataSource webDS() throws NamingException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("jdbc/web");
        return dataSource;
    }

    @Bean
    public DataSource webSecureDS() throws NamingException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("jdbc/secure");
        return dataSource;
    }

    @Bean
    public DataSource webStorageDS() throws NamingException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("jdbc/storage");
        return dataSource;
    }

    @Bean
    public DataSource webEventDS() throws NamingException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("jdbc/event");
        return dataSource;
    }
}
