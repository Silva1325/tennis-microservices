package tennisCourts.control;

import tennisCourts.entity.TennisCourtEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TennisCourtRepository implements PanacheRepository<TennisCourtEntity> {}