package courts.control;

import courts.entity.CourtEntity;
import courts.control.command.CreateCourtCommand;
import courts.control.command.DeleteCourtCommand;
import courts.control.command.UpdateCourtCommand;
import courts.control.exception.DuplicateCourtException;
import courts.control.exception.CourtNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CourtCommandService {
    @Inject
    CourtRepository repository;

    @Transactional
    public CourtEntity create(CreateCourtCommand command) {
        if (repository.findByNameAndCity(command.name(), command.city()).isPresent()) {
            throw new DuplicateCourtException(command.name(), command.city());
        }
        CourtEntity court = new CourtEntity(command.name(), command.country(), command.city(), command.surface());
        repository.persist(court);
        flushUnique(command.name(), command.city());
        return court;
    }

    @Transactional
    public CourtEntity update(UpdateCourtCommand command) {
        CourtEntity court = repository.findByPublicId(command.id())
                .orElseThrow(() -> new CourtNotFoundException(command.id()));
        boolean nameTaken = repository.findByNameAndCity(command.name(), command.city())
                .filter(other -> !other.getId().equals(court.getId()))
                .isPresent();
        if (nameTaken) {
            throw new DuplicateCourtException(command.name(), command.city());
        }
        court.setName(command.name());
        court.setCountry(command.country());
        court.setCity(command.city());
        court.setSurface(command.surface());
        flushUnique(command.name(), command.city());
        return court;
    }

    @Transactional
    public void delete(DeleteCourtCommand command) {
        CourtEntity court = repository.findByPublicId(command.id())
                .orElseThrow(() -> new CourtNotFoundException(command.id()));
        repository.delete(court);
    }

    private void flushUnique(String name, String city) {
        try {
            repository.flush();
        } catch (ConstraintViolationException e) {
            if (CourtEntity.NAME_CITY_CONSTRAINT.equalsIgnoreCase(e.getConstraintName())) {
                throw new DuplicateCourtException(name, city);
            }
            throw e;
        }
    }
}
