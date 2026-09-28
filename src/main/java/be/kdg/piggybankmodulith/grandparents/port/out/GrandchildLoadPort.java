package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;

import java.util.List;
import java.util.Optional;

public interface GrandchildLoadPort {

    Optional<Grandchild> loadById(GrandChildId grandchildId);

    List<Grandchild> loadAll();
}
