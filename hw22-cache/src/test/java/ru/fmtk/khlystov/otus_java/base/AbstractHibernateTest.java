package ru.fmtk.khlystov.otus_java.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.fmtk.khlystov.otus_java.core.repository.DataTemplateHibernate;
import ru.fmtk.khlystov.otus_java.core.sessionmanager.TransactionManagerHibernate;
import ru.fmtk.khlystov.otus_java.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.fmtk.khlystov.otus_java.crm.model.Address;
import ru.fmtk.khlystov.otus_java.crm.model.Client;
import ru.fmtk.khlystov.otus_java.crm.model.Phone;
import ru.fmtk.khlystov.otus_java.crm.service.DBServiceClient;
import ru.fmtk.khlystov.otus_java.crm.service.DbServiceClientImpl;
import ru.fmtk.khlystov.otus_java.mycache.CacheKey;
import ru.fmtk.khlystov.otus_java.mycache.HwCache;
import ru.fmtk.khlystov.otus_java.mycache.MyCache;

import static ru.fmtk.khlystov.otus_java.DbServiceDemo.HIBERNATE_CFG_FILE;
import static ru.fmtk.khlystov.otus_java.core.repository.HibernateUtils.buildSessionFactory;
import static ru.fmtk.khlystov.otus_java.core.repository.HibernateUtils.createServiceRegistry;


public abstract class AbstractHibernateTest {
    protected SessionFactory sessionFactory;
    protected TransactionManagerHibernate transactionManager;
    protected DataTemplateHibernate<Client> clientTemplate;
    protected DBServiceClient dbServiceClient;
    protected HwCache<CacheKey<Long>, Client> clientById;

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = buildSessionFactory(
                createServiceRegistry(configuration),
                configuration,
                Client.class, Address.class, Phone.class);

        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(Client.class);
        clientById = new MyCache<>();
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, clientById);
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }
}
