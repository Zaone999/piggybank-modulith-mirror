package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Order(1)
class InMemoryPiggybankAdapter implements PiggybankLoadPort, PiggybankUpdatePort {

    private final Map<PiggybankId, Piggybank> piggybanks = new LinkedHashMap<>();

    @Override
    public Optional<Piggybank> loadByOwnerId(GrandchildId ownerId) {
        return piggybanks.values().stream()
                .filter(pb -> pb.isOwnedBy(ownerId))
                .findFirst();
    }

    @Override
    public List<Piggybank> loadAll() {
        return List.copyOf(piggybanks.values());
    }

    @Override
    public void update(Piggybank piggybank) {
        piggybanks.put(piggybank.getPiggybankId(), piggybank);
    }
}
