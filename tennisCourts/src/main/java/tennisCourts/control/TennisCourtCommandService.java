package tennisCourts.control;

import tennisCourts.entity.TennisCourtEntity;
import tennisCourts.control.command.CreateTennisCourtCommand;
import tennisCourts.control.command.DeleteTennisCourtCommand;
import tennisCourts.control.command.UpdateTennisCourtCommand;
import tennisCourts.control.exception.DuplicateTennisCourtException;
import tennisCourts.control.exception.TennisCourtNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TennisCourtCommandService {
    @Inject
    TennisCourtRepository repository;

    @Transactional
    public TennisCourtEntity create(CreateTennisCourtCommand command) {
        if (repository.findByNameAndCity(command.name(), command.city()).isPresent()) {
            throw new DuplicateTennisCourtException(command.name(), command.city());
        }
        TennisCourtEntity court = new TennisCourtEntity(command.name(), command.country(), command.city(), command.surface());
        repository.persist(court);
        flushUnique(command.name(), command.city());
        return court;
    }

    @Transactional
    public TennisCourtEntity update(UpdateTennisCourtCommand command) {
        TennisCourtEntity court = repository.findByPublicId(command.id())
                .orElseThrow(() -> new TennisCourtNotFoundException(command.id()));
        boolean nameTaken = repository.findByNameAndCity(command.name(), command.city())
                .filter(other -> !other.getId().equals(court.getId()))
                .isPresent();
        if (nameTaken) {
            throw new DuplicateTennisCourtException(command.name(), command.city());
        }
        court.setName(command.name());
        court.setCountry(command.country());
        court.setCity(command.city());
        court.setSurface(command.surface());
        flushUnique(command.name(), command.city());
        return court;
    }

    @Transactional
    public void delete(DeleteTennisCourtCommand command) {
        TennisCourtEntity court = repository.findByPublicId(command.id())
                .orElseThrow(() -> new TennisCourtNotFoundException(command.id()));
        repository.delete(court);
    }

    private void flushUnique(String name, String city) {
        try {
            repository.flush();
        } catch (ConstraintViolationException e) {
            if (TennisCourtEntity.NAME_CITY_CONSTRAINT.equalsIgnoreCase(e.getConstraintName())) {
                throw new DuplicateTennisCourtException(name, city);
            }
            throw e;
        }
    }
}
