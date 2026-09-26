package be.kdg.piggybankmodulith;

import be.kdg.piggybankmodulith.choreography.ScheduledPiggybankDrip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiggybankModulithApplication {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledPiggybankDrip.class);

    static void main(String[] args) {
        ApplicationModules modules = ApplicationModules.of(PiggybankModulithApplication.class);
        modules.forEach(module ->
                logger.info("Discovered module: " + module.getIdentifier()));

        SpringApplication.run(PiggybankModulithApplication.class, args);
    }

}
