package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;

import java.util.List;

public interface GrandchildQuery {

    List<Grandchild> listGrandchildren();
}
