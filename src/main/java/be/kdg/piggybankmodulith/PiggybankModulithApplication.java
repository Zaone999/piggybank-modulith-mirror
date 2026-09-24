package be.kdg.piggybankmodulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiggybankModulithApplication {

    static void main(String[] args) {
        var modules = ApplicationModules.of(PiggybankModulithApplication.class);
        modules.forEach(module ->
                System.out.println("Discovered module: " + module.getIdentifier()));

        SpringApplication.run(PiggybankModulithApplication.class, args);
    }

}
