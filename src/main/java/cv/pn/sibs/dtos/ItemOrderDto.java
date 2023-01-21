package cv.pn.sibs.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ItemOrderDto {

    @NotEmpty(message = "This field ID Item is mandatory")
    private String idItem;

    @NotEmpty(message = "This field Order Code is mandatory")
    private String orderCode;

    @Email(message = "This email is not valid")
    @NotEmpty(message = "This field Email is mandatory")
    private  String email;

    @Min(message = "Minimum order quantity is 1", value = 1)
    private int quantity;
}
