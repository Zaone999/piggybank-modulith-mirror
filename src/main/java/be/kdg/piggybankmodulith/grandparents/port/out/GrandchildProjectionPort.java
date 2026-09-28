package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;

public interface GrandchildProjectionPort {

    void save(Grandchild grandchild);
}
