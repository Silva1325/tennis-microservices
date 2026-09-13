package players.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue
    @Setter
    private Long id;

    @Column(name = "public_id", nullable = false, updatable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false)
    @Setter
    private String firstname;

    @Column(nullable = false)
    @Setter
    private String lastname;

    @Column(nullable = false)
    @Setter
    private String country;

    @Setter
    private int age;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;


    public PlayerEntity(String firstname, String lastname, String country, int age){
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.age = age;
    }

}
