package asia.fourtitude.interviewq.jumble;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import asia.fourtitude.interviewq.jumble.core.JumbleEngine;

@Configuration
public class AppConfig {

    @Bean
    public JumbleEngine jumbleEngine() throws IOException {
        return new JumbleEngine();
    }

}
