package com.earl.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;

@Configuration
public class StartupConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public String runSqlScriptsOnStartup(DataSource dataSource) throws IOException, SQLException {
        Resource resource = applicationContext.getResource("classpath:init.sql");
        String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        ScriptUtils.executeSqlScript(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(), resource);
        return "";
    }

}
