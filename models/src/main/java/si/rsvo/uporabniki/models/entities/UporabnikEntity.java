package si.rsvo.uporabniki.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "uporabniki")
@NamedQueries(value =
        {
                @NamedQuery(name = "UporabnikEntity.getAll",
                        query = "SELECT up FROM UporabnikEntity up"),
                @NamedQuery(name = "UporabnikEntity.getByUsername",
                        query = "SELECT up FROM UporabnikEntity up WHERE up.username = :uporabnikUsername")
        })

public class UporabnikEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ime")
    private String ime;

    @Column(name = "priimek")
    private String priimek;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}