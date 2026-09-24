package be.kdg.piggybankmodulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

    @Test
    void respectsModuleBoundaries() {
        ApplicationModules.of(PiggybankModulithApplication.class)
                .verify();
    }
}
