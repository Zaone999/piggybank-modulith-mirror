package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;

public interface UpdateSavingsPromisePort {

    SavingsPromise update(SavingsPromise savingsPromise);
}