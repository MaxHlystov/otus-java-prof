package ru.fmtk.khlystov.otus_java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fmtk.khlystov.otus_java.core.repository.DataTemplateHibernate;
import ru.fmtk.khlystov.otus_java.core.sessionmanager.TransactionManagerHibernate;
import ru.fmtk.khlystov.otus_java.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.fmtk.khlystov.otus_java.crm.model.Address;
import ru.fmtk.khlystov.otus_java.crm.model.Client;
import ru.fmtk.khlystov.otus_java.crm.model.Phone;
import ru.fmtk.khlystov.otus_java.crm.service.DbServiceClientImpl;
import ru.fmtk.khlystov.otus_java.mycache.CacheKey;
import ru.fmtk.khlystov.otus_java.mycache.HwCache;
import ru.fmtk.khlystov.otus_java.mycache.MyCache;

import static ru.fmtk.khlystov.otus_java.core.repository.HibernateUtils.buildSessionFactory;
import static ru.fmtk.khlystov.otus_java.core.repository.HibernateUtils.createServiceRegistry;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws InterruptedException {
        final List<String> cacheLog = new ArrayList<>();
        HwCache<CacheKey<Long>, Client> clientById = new MyCache<>();
        clientById.addListener((id, client, action) -> cacheLog.add(toLogItem(id, client, action)));
        DbServiceClientImpl dbServiceClient = getDbServiceClient(clientById);

        // Create clients to cache
        long[] ids = LongStream.rangeClosed(1, 3)
                .mapToObj(id -> new Client(id, "Client" + id))
                .map(dbServiceClient::saveClient)
                .mapToLong(Client::getId)
                .toArray();

        log.info("After insert {}", cacheLog);
        cacheLog.clear();

        var clientSelected = dbServiceClient.getClient(ids[0])
                .orElseThrow(() -> new RuntimeException("Client not found, id " + ids[0]));
        log.info("client selected:{}", clientSelected);

        log.info("After finding client with id 1: {}", cacheLog);
        cacheLog.clear();

        dbServiceClient.saveClient(new Client(clientSelected.getId(), "dbServiceSecondUpdated"));
        final long clientSelectedId = clientSelected.getId();
        var clientUpdated = dbServiceClient.getClient(clientSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSelectedId));

        log.info("After client updated {}", cacheLog);
        cacheLog.clear();


        log.info("GC call");
        final List<BigObject> blobs = new ArrayList<>();
        IntStream.range(0, 10).mapToObj(i -> new BigObject()).forEach(blobs::add);
        System.gc();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        log.info("Chache after gc: " + clientById);

        var client2 = dbServiceClient.getClient(ids[1])
                .orElseThrow(() -> new RuntimeException("Client not found, id " + ids[1]));
        log.info("client selected:{}", client2);

        log.info("After finding client with id 2: {}", cacheLog);
        cacheLog.clear();

    }

    private static DbServiceClientImpl getDbServiceClient(HwCache<CacheKey<Long>, Client> clientById) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = buildSessionFactory(
                createServiceRegistry(configuration),
                configuration,
                Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate, clientById);
    }

    private static String toLogItem(CacheKey<Long> key, Client value, String action) {
        return "{" +
                "key=" + key +
                ", value=" + value +
                ", action='" + action + '\'' +
                '}';
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];
    }

}
