package remind.remember.fix.config;

import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.util.EnvironmentPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jca.support.LocalConnectionFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("remind.remember.fix")
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement // освобождает от явного управления транзакциями
@EnableJpaRepositories("remind.remember.fix.repositories")
public class SpringConfig implements WebMvcConfigurer{
    
    private final ApplicationContext applicationContext;
    private final Environment env;
    
    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    @Bean 
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("hibernate.driver_driver"));
        dataSource.setUrl(env.getProperty("hibernate.connection.url"));
        dataSource.setUsername("postgres");
        dataSource.setPassword(env.getProperty("hibernate.connection.password"));

        return dataSource;
    }

    // @Bean
    // public JdbcTemplate jdbcTemplate(){
    //     return new JdbcTemplate(dataSource());
    // }

    private Properties hibernateProperties(){
        
        Properties properties = new Properties(); 
        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        
        return properties;
    }

    // @Bean
    // public LocalSessionFactoryBean sessionFactory(){
    //     LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    //     sessionFactory.setDataSource(dataSource());
    //     sessionFactory.setPackagesToScan("remind.remember.fix.models");
    //     sessionFactory.setHibernateProperties(hibernateProperties());
    //     return sessionFactory;
    // }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("remind.remember.fix.model");
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    // @Bean
    // public PlatformTransactionManager hibernateTransactionManager(){
    //     HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    //     transactionManager.setSessionFactory(sessionFactory().getObject());
    //     return transactionManager;
    // }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
