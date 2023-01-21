package cv.pn.sibs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * A DTO for the {@link cv.pn.sibs.entities.User} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message = "This field is mandatory")
    private String name;

    @Email(message = "This email is not valid")
    private String email;
}