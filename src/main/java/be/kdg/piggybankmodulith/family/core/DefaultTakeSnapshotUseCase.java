package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotCommand;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotUseCase;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotsCommand;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankSnapshotPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
class DefaultTakeSnapshotUseCase implements TakeSnapshotUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTakeSnapshotUseCase.class);

    private final PiggybankLoadPort piggybankLoadPort;
    private final PiggybankSnapshotPort piggybankSnapshotPort;

    DefaultTakeSnapshotUseCase(PiggybankLoadPort piggybankLoadPort, PiggybankSnapshotPort piggybankSnapshotPort) {
        this.piggybankLoadPort = piggybankLoadPort;
        this.piggybankSnapshotPort = piggybankSnapshotPort;
    }

    @Override
    // its own transaction: it can run after another transaction committed, from an event listener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void takeSnapshot(TakeSnapshotCommand command) {
        GrandchildId ownerId = GrandchildId.of(command.ownerId());
        Piggybank piggybank = piggybankLoadPort.loadByOwnerId(ownerId)
                .orElseThrow(() -> new PiggyBankNotFoundException(
                        "Piggybank not found for owner " + ownerId.uuid()));

        snapshot(piggybank, command.date());
    }

    @Override
    @Transactional
    public void takeSnapshots(TakeSnapshotsCommand command) {
        piggybankLoadPort.loadAll().forEach(piggybank -> snapshot(piggybank, command.date()));
    }

    private void snapshot(Piggybank piggybank, LocalDateTime date) {
        boolean alreadyCovered = piggybank.getBaseSnapshotDate().filter(last -> !date.isAfter(last)).isPresent();
        if (alreadyCovered) {
            logger.info("Piggybank {} already has a snapshot on or after {}", piggybank.getPiggybankId().uuid(), date);
            return;
        }

        piggybank.takeSnapshot(date);
        piggybankSnapshotPort.saveSnapshot(piggybank);
    }
}
