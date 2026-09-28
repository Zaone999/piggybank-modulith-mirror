package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;
import be.kdg.piggybankmodulith.grandparents.port.out.GrandchildLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.GrandchildProjectionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class GrandchildPersistenceAdapter implements GrandchildLoadPort, GrandchildProjectionPort {

    private final GrandchildJpaRepository repository;

    GrandchildPersistenceAdapter(GrandchildJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Grandchild> loadById(GrandChildId grandchildId) {
        return repository.findById(grandchildId.uuid())
                .map(this::toDomain);
    }

    @Override
    public List<Grandchild> loadAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void save(Grandchild grandchild) {
        repository.save(new GrandchildJpaEntity(
                grandchild.id().uuid(), grandchild.firstName(), grandchild.lastName()));
    }

    private Grandchild toDomain(GrandchildJpaEntity entity) {
        return new Grandchild(GrandChildId.of(entity.getUuid()), entity.getFirstName(), entity.getLastName());
    }
}
