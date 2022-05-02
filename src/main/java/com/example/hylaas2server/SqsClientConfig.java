package com.example.hylaas2server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsClientConfig {

    @Bean
    SqsProcessorModule sqsProcessorModule(SqsClient sqsClient) {
        SqsProcessorModule singleton = SqsProcessorModule.getInstance();
        singleton.setSqsClient(sqsClient);
        return singleton;
    }

    @Bean
    SqsClient localSqsClient(@Value("${elasticMq.host:http://localhost:9324}") String elasticMqHost) {
        return buildElasticMqClient(elasticMqHost);
    }

    private SqsClient buildElasticMqClient(String elasticMqHost) {
        return SqsClient.builder()
                .region(Region.of("dummy"))
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .endpointOverride(URI.create(elasticMqHost))
                .build();
    }

}
