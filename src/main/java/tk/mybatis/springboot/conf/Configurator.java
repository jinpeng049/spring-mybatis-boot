/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.springboot.conf;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.springboot.util.MyMapper;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis基础配置
 *
 * @author liuzh
 * @since 2015-12-19 10:11
 */
@Configuration
@EnableTransactionManagement
@PropertySource(ignoreResourceNotFound = true, value = {"classpath:/config/email.properties", "classpath:/config/email.yml"}, name = "email")
@MapperScan({"tk.mybatis.springboot.dao", "tk.mybatis.springboot.mapper"})
public class Configurator implements TransactionManagementConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(Configurator.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

//    @Bean
//    public DataSource dataSource() {
//        try {
//            DruidDataSource dataSource = new DruidDataSource();
//            dataSource.setUrl(database_url);
//            dataSource.setUsername(database_username);
//            dataSource.setPassword(database_password);
//            dataSource.setInitialSize(1);
//            dataSource.setMinIdle(1);
//            dataSource.setMaxActive(20);
//            dataSource.setMaxWait(60000);
//            dataSource.setTimeBetweenEvictionRunsMillis(60000);
//            dataSource.setMinEvictableIdleTimeMillis(300000);
//            dataSource.setValidationQuery("select 'x' from dual");
//            dataSource.setTestWhileIdle(true);
//            dataSource.setTestOnBorrow(false);
//            dataSource.setTestOnReturn(false);
//            dataSource.setPoolPreparedStatements(false);
//            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
//            dataSource.setFilters("stat");
//            return dataSource;
//        } catch (SQLException e) {
//            return null;
//        }
//    }

//    @Bean
//    public MapperHelper mapperHelper() {
//        MapperHelper mapperHelper = new MapperHelper();
//        Properties properties = new Properties();
//        properties.setProperty("ORDER", "AFTER");
//        mapperHelper.setProperties(properties);
//        mapperHelper.registerMapper(MyMapper.class);
//        mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
//        return mapperHelper;
//    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            //分页插件
            PageHelper pageHelper = new PageHelper();
            Properties properties = new Properties();
            properties.setProperty("reasonable", "true");
            properties.setProperty("supportMethodsArguments", "true");
            properties.setProperty("returnPageInfo", "check");
            properties.setProperty("params", "count=countSql");
            pageHelper.setProperties(properties);

            //添加插件
            factoryBean.setPlugins(new Interceptor[]{pageHelper});
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return factoryBean.getObject();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
