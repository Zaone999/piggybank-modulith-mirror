package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;
import be.kdg.piggybankmodulith.family.port.in.BiggestDonationQuery;
import be.kdg.piggybankmodulith.family.port.out.BiggestDonationPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
class DefaultBiggestDonationQuery implements BiggestDonationQuery {

    private final BiggestDonationPort biggestDonationPort;

    DefaultBiggestDonationQuery(BiggestDonationPort biggestDonationPort) {
        this.biggestDonationPort = biggestDonationPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BiggestDonation> biggestDonation(UUID ownerId) {
        return biggestDonationPort.load(GrandchildId.of(ownerId));
    }
}
