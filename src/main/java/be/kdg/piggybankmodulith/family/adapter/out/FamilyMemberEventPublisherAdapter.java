package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberUpdatePort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class FamilyMemberEventPublisherAdapter implements FamilyMemberUpdatePort {

    private final ApplicationEventPublisher applicationEventPublisher;

    FamilyMemberEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void update(FamilyMember familyMember) {
        familyMember.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        familyMember.clearDomainEvents();
    }
}
