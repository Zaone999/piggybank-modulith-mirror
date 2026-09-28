package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.port.in.ProjectMovementCommand;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class DefaultProjectMovementUseCase implements ProjectMovementUseCase {

    private final StatisticsProjection statisticsProjection;

    DefaultProjectMovementUseCase(StatisticsProjection statisticsProjection) {
        this.statisticsProjection = statisticsProjection;
    }

    @Override
    @Transactional
    public void projectMovement(ProjectMovementCommand command) {
        statisticsProjection.project(command);
    }
}
