package com.example.dwbackend.config.neo4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.neo4j.driver.*;


@Configuration
public class Neo4jJdbcConfig {
    @Value("${spring.neo4j.uri}")
    private String uri;

    @Value("${spring.neo4j.authentication.username}")
    private String user;

    @Value("${spring.neo4j.authentication.password}")
    private String password;

    @Bean("neo4jDriver")
    public Driver getDriver() {
        return GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
}
