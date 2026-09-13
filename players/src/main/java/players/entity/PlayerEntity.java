package players.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PlayerEntity extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

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

    public PlayerEntity(String email, String password, String firstname, String lastname, String country, int age){
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.age = age;
    }

}
