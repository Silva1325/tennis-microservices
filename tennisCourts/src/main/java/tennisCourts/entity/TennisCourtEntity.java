package tennisCourts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TennisCourtEntity extends AbstractEntity {

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
