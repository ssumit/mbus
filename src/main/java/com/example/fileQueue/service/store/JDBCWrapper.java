package com.example.fileQueue.service.store;

import com.example.common.ExecutorFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class JDBCWrapper {
    private final JdbcTemplate template;
    private final ExecutorFactory executorFactory;

    public JDBCWrapper(String url, String driver, int timeout, int poolSize, ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
        BasicDataSource dataSource = createDataSource(url, driver, timeout, poolSize);
        template = new JdbcTemplate(dataSource);
        template.setQueryTimeout(timeout);
    }

    protected BasicDataSource createDataSource(String url, String driver, int timeout, int poolSize) {
        checkNotNull(driver);
        checkNotNull(url);
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setMaxActive(poolSize);
        dataSource.setMaxWait(timeout);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setValidationQuery("select 1");

        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        return dataSource;

    }

    public CompletableFuture<Void> update(String tableName, final String sql, final Object... args) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        scheduleNow(() -> {
            template.update(sql, args);
            completableFuture.complete(null);
        }, tableName);
        return completableFuture;
    }

    private void scheduleNow(Runnable runnable, String key) {
        executorFactory.getExecutor(key).schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }
}