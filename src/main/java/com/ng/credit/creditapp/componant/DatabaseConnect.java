package com.ng.credit.creditapp.componant;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;


@Slf4j
public class DatabaseConnect {

    public DataSource getDataSource() {

        try {
            var databaseUrl = "linpub-entitlement-app-bclab1.cnaijsafwxxm.us-east-1.rds.amazonaws.com";
            var databasePort = "3306";
            var databaseName = "linpub_1122";
            var driverClassName = "org.mariadb.jdbc.Driver";
            var jdbcUrl = "jdbc:mariadb://linpub-entitlement-app-bclab1.cnaijsafwxxm.us-east-1.rds.amazonaws.com:3306/linpub_1122";
            var urlFormat = "jdbc:mariadb://%s:%s/%s";

            var properties = new Properties();
            properties.setProperty("driverClassName", driverClassName);

            properties.setProperty("jdbcUrl", jdbcUrl);
            properties.setProperty("username", "admin");
            properties.setProperty("password", "password");
            properties.setProperty("idleTimeout", "30000");
            properties.setProperty("maxLifetime", "30000");
            properties.setProperty("connectionTimeout", "30000");
            properties.setProperty("validationTimeout", "30000");
            properties.setProperty("maximumPoolSize", "5");

            var hikariConfig = new HikariConfig(properties);
            hikariConfig.addDataSourceProperty("cachePrepStmts",
                    true);
            hikariConfig.addDataSourceProperty("prepStmtCacheSize",
                    250);
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit",
                    2048);
            return new HikariDataSource(hikariConfig);

        } catch (Exception ex) {
            log.error(ex.toString());
            ex.printStackTrace();
            return null;
        }


    }

}
