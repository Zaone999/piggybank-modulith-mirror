package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromiseId;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Order(1)
class InMemorySavingsPromiseAdapter implements SavingsPromiseLoadPort, SavingsPromiseUpdatePort {

    private final Map<SavingsPromiseId, SavingsPromise> savingsPromises = new LinkedHashMap<>();

    @Override
    public Optional<SavingsPromise> loadActivePromiseFor(GrandChildId grandchildId) {
        return savingsPromises.values().stream()
                .filter(sp -> sp.isMadeTo(grandchildId))
                .filter(SavingsPromise::notYetTriggered)
                .findFirst();
    }

    @Override
    public void update(SavingsPromise savingsPromise) {
        savingsPromises.put(savingsPromise.getSavingsPromiseId(), savingsPromise);
    }
}
