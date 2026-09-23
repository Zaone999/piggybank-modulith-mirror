package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;

import java.util.List;

public interface FindAllPiggybanksPort {

    List<Piggybank> findAll();
}
