package be.kdg.piggybankmodulith.family.port.in;

/**
 * The query side of CQRS: keeps the read models up to date with the piggybank's events.
 */
public interface ProjectMovementUseCase {

    void projectMovement(ProjectMovementCommand command);
}
