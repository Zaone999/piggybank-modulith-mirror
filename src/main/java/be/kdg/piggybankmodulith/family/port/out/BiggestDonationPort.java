package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;

import java.util.Optional;

public interface BiggestDonationPort {

    Optional<BiggestDonation> load(GrandchildId owner);

    void save(BiggestDonation biggestDonation);

    void deleteAllBiggestDonations();
}
