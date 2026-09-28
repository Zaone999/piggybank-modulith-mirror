package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;
import be.kdg.piggybankmodulith.family.port.in.BestMonthQuery;
import be.kdg.piggybankmodulith.family.port.out.MonthlySavingsPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
class DefaultBestMonthQuery implements BestMonthQuery {

    private final MonthlySavingsPort monthlySavingsPort;

    DefaultBestMonthQuery(MonthlySavingsPort monthlySavingsPort) {
        this.monthlySavingsPort = monthlySavingsPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonthlySavings> bestMonth(UUID ownerId) {
        return monthlySavingsPort.loadBestMonth(GrandchildId.of(ownerId));
    }
}
