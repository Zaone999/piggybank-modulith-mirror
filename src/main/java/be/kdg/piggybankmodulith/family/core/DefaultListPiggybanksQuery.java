package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.ListPiggybanksQuery;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultListPiggybanksQuery implements ListPiggybanksQuery {

    private final PiggybankLoadPort piggybankLoadPort;

    DefaultListPiggybanksQuery(PiggybankLoadPort piggybankLoadPort) {
        this.piggybankLoadPort = piggybankLoadPort;
    }

    @Override
    public List<Piggybank> listPiggybanks() {
        return piggybankLoadPort.loadAll();
    }
}
