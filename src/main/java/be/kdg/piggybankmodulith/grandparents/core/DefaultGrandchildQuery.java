package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;
import be.kdg.piggybankmodulith.grandparents.port.in.GrandchildQuery;
import be.kdg.piggybankmodulith.grandparents.port.out.GrandchildLoadPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class DefaultGrandchildQuery implements GrandchildQuery {

    private final GrandchildLoadPort grandchildLoadPort;

    DefaultGrandchildQuery(GrandchildLoadPort grandchildLoadPort) {
        this.grandchildLoadPort = grandchildLoadPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grandchild> listGrandchildren() {
        return grandchildLoadPort.loadAll();
    }
}
