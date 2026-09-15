package tennisCourts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = TennisCourtEntity.NAME_CITY_CONSTRAINT, columnNames = {"name", "city"}))
@Getter
@NoArgsConstructor
public class TennisCourtEntity extends AbstractEntity {

    public static final String NAME_CITY_CONSTRAINT = "uk_tennis_court_name_city";

    @Column(nullable = false)
    @Setter
    private String name;

    @Column(nullable = false)
    @Setter
    private String country;

    @Column(nullable = false)
    @Setter
    private String city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private Surface surface;

    public TennisCourtEntity(String name, String country, String city, Surface surface){
        this.name = name;
        this.country = country;
        this.city = city;
        this.surface = surface;
    }
}
