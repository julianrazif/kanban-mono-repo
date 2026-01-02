package com.julian.razif.kanban.infrastructure.config;

import com.julian.razif.kanban.common.security.Decryption;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
  basePackages = {
    "com.julian.razif.kanban.infrastructure.persistence.repository"
  },
  entityManagerFactoryRef = "kanbanEntityManagerFactory",
  transactionManagerRef = "kanbanTransactionManager"
)
class KanbanJPAConfiguration {

  private static final Logger log = LoggerFactory.getLogger(KanbanJPAConfiguration.class);

  private final Decryption decryption;

  @Value("${db.kanban.url}")
  private String url;

  @Value("${db.kanban.username}")
  private String username;

  @Value("${db.kanban.password}")
  private String password;

  @Value("${db.hikari.maximum-pool-size:20}")
  private int maximumPoolSize;

  @Value("${db.hikari.minimum-idle:10}")
  private int minimumIdle;

  @Value("${db.hikari.connection-timeout:120000}")
  private long connectionTimeout;

  @Value("${db.hikari.idle-timeout:600000}")
  private long idleTimeout;

  @Value("${db.hikari.max-lifetime:1800000}")
  private long maxLifetime;

  @Value("${db.hikari.validation-timeout:5000}")
  private long validationTimeout;

  @Value("${db.hikari.leak-detection-threshold:60000}")
  private long leakDetectionThreshold;

  @Value("${db.hikari.connection-test-query:SELECT 1}")
  private String connectionTestQuery;

  @Value("${db.hikari.auto-commit:false}")
  private boolean autoCommit;

  @Value("${db.hikari.pool-name:Kanban-hikari}")
  private String poolName;

  @Value("${db.hikari.data-source-properties.prepareThreshold:5}")
  private int prepareThreshold;

  @Value("${db.hikari.data-source-properties.preparedStatementCacheQueries:256}")
  private int preparedStatementCacheQueries;

  @Value("${db.hikari.data-source-properties.preparedStatementCacheSizeMiB:5}")
  private int preparedStatementCacheSizeMiB;

  @Value("${db.hikari.data-source-properties.defaultRowFetchSize:1000}")
  private long defaultRowFetchSize;

  @Value("${db.hikari.data-source-properties.reWriteBatchedInserts:true}")
  private boolean reWriteBatchedInserts;

  @Value("${db.hikari.data-source-properties.ApplicationName:Kanban-HikariPool}")
  private String applicationName;

  @Value("${db.hikari.data-source-properties.connectTimeout:10}")
  private int connectTimeout;

  @Value("${db.hikari.data-source-properties.socketTimeout:0}")
  private int socketTimeout;

  @Value("${db.hikari.data-source-properties.tcpKeepAlive:true}")
  private boolean tcpKeepAlive;

  KanbanJPAConfiguration(Decryption decryption) {
    this.decryption = decryption;
  }

  /**
   * Creates and configures the Hikari data source for Kanban database connections.
   *
   * @return configured HikariDataSource
   * @throws RuntimeException if data source configuration fails
   */
  @Primary
  @Bean(name = "kanbanDataSource")
  HikariDataSource kanbanDataSource() {
    try {
      log.info("Configuring kanban datasource with hikari connection pool");

      // Get connection details from the configurer
      String uname = decryption.decode(username);
      String paswd = decryption.decode(password);

      log.info("Connecting to database URL: {}", url);

      // Create DataSource
      HikariConfig config = getHikariConfig(url, uname, paswd);
      HikariDataSource dataSource = new HikariDataSource(config);

      log.info("HikariCP DataSource initialized successfully");
      log.info("Pool Name: {}", config.getPoolName());
      log.info("Maximum Pool Size: {}", config.getMaximumPoolSize());
      log.info("Minimum Idle: {}", config.getMinimumIdle());
      log.info("JDBC URL: {}", url);

      return dataSource;
    } catch (GeneralSecurityException e) {
      log.error("Security error during kanban datasource configuration: {}", e.getMessage());
      throw new IllegalStateException("Failed to decrypt database credentials", e);
    } catch (Exception e) {
      log.error("Unexpected error during kanban datasource configuration", e);
      throw new IllegalStateException("Failed to configure kanban datasource", e);
    }
  }

  HikariConfig getHikariConfig(
    @Nonnull String url,
    @Nonnull String username,
    @Nonnull String password) {

    HikariConfig config = new HikariConfig();

    // ===== BASIC CONNECTION SETTINGS =====
    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName("org.postgresql.Driver");

    // ===== POOL SIZING =====
    config.setMaximumPoolSize(maximumPoolSize);
    config.setMinimumIdle(minimumIdle);

    // ===== TIMEOUT CONFIGURATION =====
    config.setConnectionTimeout(connectionTimeout);      // 120 seconds
    config.setIdleTimeout(idleTimeout);                  // 10 minutes
    config.setMaxLifetime(maxLifetime);                  // 30 minutes
    config.setValidationTimeout(validationTimeout);      // 5 seconds

    // ===== LEAK DETECTION =====
    config.setLeakDetectionThreshold(leakDetectionThreshold); // 60 seconds

    // ===== CONNECTION TESTING =====
    config.setConnectionTestQuery(connectionTestQuery);

    // ===== POOL BEHAVIOR =====
    config.setAutoCommit(autoCommit); // CRITICAL for bulk operations
    config.setReadOnly(false);
    config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

    // ===== POOL NAME =====
    config.setPoolName(poolName);

    // ===== POSTGRESQL SPECIFIC PROPERTIES =====
    Properties dsProperties = getDsProperties();

    config.setDataSourceProperties(dsProperties);

    // ===== INITIALIZATION =====
    config.setInitializationFailTimeout(-1); // Fail fast if a database cannot connect
    config.setIsolateInternalQueries(true);
    config.setAllowPoolSuspension(false);
    return config;
  }

  /**
   * Creates and configures the entity manager factory for Kanban entities.
   *
   * @param builder    the entity manager factory builder
   * @param dataSource the data source to use
   * @return configured LocalContainerEntityManagerFactoryBean
   */
  @Primary
  @Bean(name = "kanbanEntityManagerFactory")
  LocalContainerEntityManagerFactoryBean kanbanEntityManagerFactory(
    EntityManagerFactoryBuilder builder,
    @Qualifier("kanbanDataSource") DataSource dataSource) {

    log.info("Configuring kanban entity manager factory");

    // Build the entity manager factory with the provided data source
    // This configures JPA to use the specified packages for entity scanning
    LocalContainerEntityManagerFactoryBean em = builder
      .dataSource(dataSource)
      .packages(packagesToScan())
      .persistenceUnit("kanban-pu")
      .build();

    // Configure Hibernate as the JPA vendor with PostgreSQL dialect
    // ShowSql is disabled for production performance
    // GenerateDdl is disabled to prevent automatic schema generation
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(false);
    vendorAdapter.setGenerateDdl(false);

    em.setJpaVendorAdapter(vendorAdapter);

    Properties properties = new Properties();

    // Cache Configuration (for production)
    // properties.setProperty("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
    // properties.setProperty("hibernate.javax.cache.uri", "classpath:ehcache.xml");

    // JPA Properties
    em.setJpaProperties(properties);

    log.info("EntityManagerFactory initialized successfully");
    return em;
  }

  /**
   * Creates the entity manager for Kanban entities.
   *
   * @param entityManagerFactory the entity manager factory
   * @return configured EntityManager
   */
  @Primary
  @Bean(name = "kanbanEntityManager")
  EntityManager kanbanEntityManager(
    @Qualifier("kanbanEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory.createEntityManager();
  }

  /**
   * Creates the transaction manager for Kanban entities.
   *
   * @param entityManagerFactory the entity manager factory
   * @return configured PlatformTransactionManager
   */
  @Primary
  @Bean(name = "kanbanTransactionManager")
  PlatformTransactionManager kanbanTransactionManager(
    @Qualifier("kanbanEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

    JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);

    // Transaction settings
    transactionManager.setNestedTransactionAllowed(false);
    transactionManager.setValidateExistingTransaction(true);
    transactionManager.setGlobalRollbackOnParticipationFailure(true);

    log.info("TransactionManager initialized successfully");
    return transactionManager;
  }

  /**
   * Returns the packages to scan for entity classes.
   * This method defines which packages contain JPA entity classes that should be
   * managed by the entity manager factory.
   *
   * @return array of package names to scan for entity classes
   */
  private String[] packagesToScan() {
    return new String[]{
      "com.julian.razif.kanban.infrastructure.persistence.model",
    };
  }

  Properties getDsProperties() {
    Properties dsProperties = new Properties();

    // Prepared Statement Cache
    dsProperties.setProperty("prepareThreshold", String.valueOf(prepareThreshold));
    dsProperties.setProperty("preparedStatementCacheQueries", String.valueOf(preparedStatementCacheQueries));
    dsProperties.setProperty("preparedStatementCacheSizeMiB", String.valueOf(preparedStatementCacheSizeMiB));

    // Fetch Size for Large ResultSets
    dsProperties.setProperty("defaultRowFetchSize", String.valueOf(defaultRowFetchSize));

    // Batch Rewrite Optimization
    dsProperties.setProperty("reWriteBatchedInserts", String.valueOf(reWriteBatchedInserts));

    // Connection Properties
    dsProperties.setProperty("ApplicationName", applicationName);
    dsProperties.setProperty("connectTimeout", String.valueOf(connectTimeout));
    dsProperties.setProperty("socketTimeout", String.valueOf(socketTimeout)); // No timeout for long queries
    dsProperties.setProperty("tcpKeepAlive", String.valueOf(tcpKeepAlive));

    // Performance Tuning
    dsProperties.setProperty("binaryTransfer", "true");
    dsProperties.setProperty("receiveBufferSize", "65536");
    dsProperties.setProperty("sendBufferSize", "65536");

    return dsProperties;
  }

}
