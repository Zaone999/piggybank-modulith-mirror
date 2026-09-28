package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(schema = "kdg_family", name = "projected_event")
class ProjectedEventJpaEntity {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    protected ProjectedEventJpaEntity() {}

    ProjectedEventJpaEntity(UUID eventId) {
        this.eventId = eventId;
    }
}
