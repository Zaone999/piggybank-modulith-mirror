package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;
import be.kdg.piggybankmodulith.grandparents.port.out.GrandchildLoadPort;
import org.springframework.stereotype.Component;

/**
 * Not a use case and not an input port: a helper the use cases in core share, to log a grandchild's name.
 * Use cases call output ports, never another input port; adapters use the GrandchildQuery input port instead.
 */
@Component
class GrandchildNameResolver {

    private final GrandchildLoadPort grandchildLoadPort;

    GrandchildNameResolver(GrandchildLoadPort grandchildLoadPort) {
        this.grandchildLoadPort = grandchildLoadPort;
    }

    // the born event may not have been projected (yet): fall back to the id
    String nameOf(GrandChildId grandchildId) {
        return grandchildLoadPort.loadById(grandchildId)
                .map(Grandchild::fullName)
                .orElse(grandchildId.uuid().toString());
    }
}
