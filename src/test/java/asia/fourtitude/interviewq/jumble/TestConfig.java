package asia.fourtitude.interviewq.jumble;

import java.io.IOException;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import asia.fourtitude.interviewq.jumble.core.JumbleEngine;

@TestConfiguration
public class TestConfig {

    @Bean
    public JumbleEngine jumbleEngine() throws IOException {
        return new JumbleEngine();
    }

}
