package cv.pn.sibs.dtos;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link cv.pn.sibs.entities.Order} entity
 */
@Data
public class OrderDto implements Serializable {

    @Email(message = "This email is not valid")
    @NotEmpty(message = "This field Email is mandatory")
    private  String email;

    //private String orderCode = RandomStringUtils.randomAlphanumeric(5);

    private List<@Valid ItemOrder> items;
    @Data
    static public class ItemOrder {

        @Min(message = "Minimum order quantity is 1", value = 1)
        private int quantity;

        @NotEmpty(message = "This field Item is mandatory")
        private String idItem;
    }
}