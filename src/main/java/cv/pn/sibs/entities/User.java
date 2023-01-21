package cv.pn.sibs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "SIBS_USER")
@Data
public class User extends CommonAttributes {

    @NotEmpty(message = "This field is mandatory")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Email(message = "This email is not valid")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
