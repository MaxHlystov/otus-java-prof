package ru.fmtk.khlystov.otus_java.crm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fmtk.khlystov.otus_java.base.AbstractHibernateTest;
import ru.fmtk.khlystov.otus_java.crm.model.Client;
import ru.fmtk.khlystov.otus_java.mycache.CacheKey;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DbServiceClient с кэшем должен")
class DbServiceClientWithCacheTest extends AbstractHibernateTest {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientWithCacheTest.class);

    @AfterEach
    public void removeClients() {
        log.info("Delete all clients after test");
        transactionManager.doInTransaction(session ->
                session.createQuery("DELETE FROM Client").executeUpdate());
    }

    @Test
    @DisplayName(" вставлять данные в кэш, при вставке ентити")
    void insertShouldInsertToCache() {
        final int clientNumber = 3;
        final List<String> cacheLog = new ArrayList<>();
        clientById.addListener((key, value, action) -> cacheLog.add(key + action));

        long[] ids = inertClients(clientNumber);
        var stat = getUsageStatistics();

        assertThat(stat.getInsertCount()).isEqualTo(clientNumber);
        assertThat(cacheLog).containsExactlyInAnyOrder(ids[0] + "ADD", ids[1] + "ADD", ids[2] + "ADD");
    }

    @Test
    @DisplayName(" читать данные из кэша если есть")
    void selectShouldReadFromCache() {
        final int clientNumber = 3;
        final List<String> cacheLog = new ArrayList<>();
        clientById.addListener((key, value, action) -> cacheLog.add(key + action));

        long[] ids = inertClients(clientNumber);
        var loadedSavedClient = dbServiceClient.getClient(ids[0]);

        var stat = getUsageStatistics();
        assertThat(loadedSavedClient).isPresent();
        assertThat(stat.getLoadCount()).isEqualTo(0L);
        assertThat(cacheLog).containsExactlyInAnyOrder(ids[0] + "ADD", ids[1] + "ADD", ids[2] + "ADD", ids[0] + "GET");
    }

    @Test
    @DisplayName(" добавлять данные в кэш если там не было значения")
    void firstSelectShouldAddToCache() {
        final int clientNumber = 3;
        final List<String> cacheLog = new ArrayList<>();
        clientById.addListener((key, value, action) -> cacheLog.add(key + action));

        long[] ids = inertClients(clientNumber);
        // Make cache empty
        Arrays.stream(ids).forEach(id -> clientById.remove(new CacheKey<>(id)));
        cacheLog.clear();
        // Read client two times
        var loadedSavedClient = dbServiceClient.getClient(ids[0]);
        loadedSavedClient = dbServiceClient.getClient(ids[0]);

        var stat = getUsageStatistics();
        assertThat(loadedSavedClient).isPresent();
        assertThat(stat.getLoadCount()).isEqualTo(1L);
        assertThat(cacheLog).containsExactlyInAnyOrder(ids[0] + "GET", ids[0] + "ADD", ids[0] + "GET");
    }

    @Test
    @DisplayName(" после сборки мусора кэш очищается")
    void gcShouldClearCache() {
        final int clientNumber = 3;
        final List<String> cacheLog = new ArrayList<>();
        clientById.addListener((key, value, action) -> cacheLog.add(key + action));

        long[] ids = inertClients(clientNumber);
        clearGC();
        var loadedSavedClient = transactionManager.doInTransaction(session ->
                dbServiceClient.getClient(ids[0]));

        var stat = getUsageStatistics();
        assertThat(loadedSavedClient).isPresent();
        assertThat(stat.getLoadCount()).isEqualTo(1L);
        assertThat(cacheLog).containsExactlyInAnyOrder(ids[0] + "ADD", ids[1] + "ADD", ids[2] + "ADD", ids[0] + "GET");
    }

    @Test
    @DisplayName(" обновление ентити в базе обновляет кэш")
    void updateShould() {
        final int clientNumber = 3;
        final String newClientName = "dbServiceSecondUpdated";
        final List<String> cacheLog = new ArrayList<>();
        clientById.addListener((key, value, action) -> cacheLog.add(key + action));

        long[] ids = inertClients(clientNumber);

        var updatedClient = transactionManager.doInTransaction(session -> {
            dbServiceClient.saveClient(new Client(ids[0], newClientName));
            return dbServiceClient.getClient(ids[0])
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + ids[0]));
        });

        var stat = getUsageStatistics();
        assertThat(updatedClient.getName()).isEqualTo(newClientName);
        assertThat(stat.getLoadCount()).isEqualTo(1L);
        assertThat(stat.getUpdateCount()).isEqualTo(1L);
        assertThat(cacheLog).containsExactlyInAnyOrder(ids[0] + "ADD", ids[1] + "ADD", ids[2] + "ADD",
                ids[0] + "ADD", ids[0] + "GET");
    }

    private void clearGC() {
        log.info("Start to clear GC");
        final List<BigObject> blobs = new ArrayList<>();
        IntStream.range(0, 10).mapToObj(i -> new BigObject()).forEach(blobs::add);
        System.gc();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("End clearing");
    }

    private long[] inertClients(int clientNumber) {
        log.info("Insert " + clientNumber + " clients");
        var ids = transactionManager.doInTransaction(session ->
                LongStream.rangeClosed(1, clientNumber)
                        .mapToObj(id -> new Client("Client" + id))
                        .map(dbServiceClient::saveClient)
                        .mapToLong(Client::getId)
                        .toArray());
        log.info("Client ids " + Arrays.toString(ids));
        return ids;
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];
    }
}