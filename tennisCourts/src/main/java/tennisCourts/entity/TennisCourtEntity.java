package tennisCourts.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TennisCourtEntity {

    @Id
    @GeneratedValue
    @Setter
    private Long id;

    @Column(name = "public_id", nullable = false, updatable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

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

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    public TennisCourtEntity(String name, String country, String city, Surface surface){
        this.name = name;
        this.country = country;
        this.city = city;
        this.surface = surface;
    }
}
