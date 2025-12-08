package org.maks.expensosap.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsecureJacksonConfig {

    @Bean
    public ObjectMapper insecureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Disable escaping of HTML characters like < > &
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);

        return mapper;
    }
}

