package com.fitness.user_profile.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Cosmos DB.
 */
@Configuration
@EnableCosmosRepositories(basePackages = "com.fitness.user_profile.repo")
public class CosmosConfiguration extends AbstractCosmosConfiguration {

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String dbName;

    private static final Logger logger = LoggerFactory.getLogger(CosmosConfiguration.class);

    /**
     * Bean configuration for CosmosClientBuilder.
     *
     * @return CosmosClientBuilder instance.
     */
    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        logger.info("Creating CosmosClientBuilder with URI: {}", uri);
        DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();
        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .directMode(directConnectionConfig);
    }

    /**
     * Bean configuration for CosmosConfig.
     *
     * @return CosmosConfig instance.
     */
    @Bean
    public CosmosConfig cosmosConfig() {
        logger.info("Creating CosmosConfig with query metrics enabled");
        return CosmosConfig.builder()
                .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                .enableQueryMetrics(true)
                .build();
    }

    /**
     * Returns the name of the database.
     *
     * @return Database name.
     */
    @Override
    protected String getDatabaseName() {
        logger.info("Getting database name: {}", dbName);
        return dbName;
    }

    /**
     * Implementation of ResponseDiagnosticsProcessor to log response diagnostics.
     */
    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            if (responseDiagnostics != null) {
                logger.info("Response Diagnostics: {}", responseDiagnostics);
            } else {
                logger.warn("Response Diagnostics is null");
            }
        }
    }
}
