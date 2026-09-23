package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.grandparents.domain.GrandchildId;

public record SavingsIncreaseProjectionCommand(GrandchildId grandchild, Money amount) {
}
