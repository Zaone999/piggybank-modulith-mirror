package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.FindAllPiggybanksPort;
import be.kdg.piggybankmodulith.family.port.out.LoadPiggybankPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindAllPiggybanksUseCaseImpl implements FindAllPiggybanksPort {

    private final LoadPiggybankPort loadPiggybankPort;

    public FindAllPiggybanksUseCaseImpl(LoadPiggybankPort loadPiggybankPort) {
        this.loadPiggybankPort = loadPiggybankPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piggybank> findAll() {
        return this.loadPiggybankPort.loadAll();
    }
}
