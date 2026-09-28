package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;

import java.time.YearMonth;
import java.util.Optional;

public interface MonthlySavingsPort {

    Optional<MonthlySavings> load(GrandchildId owner, YearMonth month);

    Optional<MonthlySavings> loadBestMonth(GrandchildId owner);

    void save(MonthlySavings monthlySavings);

    void deleteAllMonthlySavings();
}
