package be.kdg.piggybankmodulith.family.port.out;

import java.util.UUID;

/**
 * Remembers which events the read models already handled, so a duplicate has no effect.
 */
public interface ProjectedEventPort {

    boolean isProjected(UUID eventId);

    void markProjected(UUID eventId);

    void forgetAllProjectedEvents();
}
