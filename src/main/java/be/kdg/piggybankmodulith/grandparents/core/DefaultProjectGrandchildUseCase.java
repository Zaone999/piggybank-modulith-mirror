package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectGrandchildCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectGrandchildUseCase;
import be.kdg.piggybankmodulith.grandparents.port.out.GrandchildProjectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class DefaultProjectGrandchildUseCase implements ProjectGrandchildUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProjectGrandchildUseCase.class);

    private final GrandchildProjectionPort grandchildProjectionPort;

    DefaultProjectGrandchildUseCase(GrandchildProjectionPort grandchildProjectionPort) {
        this.grandchildProjectionPort = grandchildProjectionPort;
    }

    @Override
    @Transactional
    public void projectGrandchild(ProjectGrandchildCommand command) {
        // NO IDEMPOTENCY CHECK NEEDED
        Grandchild grandchild = new Grandchild(
                GrandChildId.of(command.familyMemberId()),
                command.firstName(),
                command.lastName());
        grandchildProjectionPort.save(grandchild);

        logger.info("New grandchild: {}", grandchild.fullName());
    }
}
