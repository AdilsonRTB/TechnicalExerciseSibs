package cv.pn.sibs.dtos;

import cv.pn.sibs.utilities.Constants;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link cv.pn.sibs.entities.Order} entity
 */
@Data
public class OrderInfoDto implements Serializable {

    private String orderCode;

    private LocalDateTime CreationDate;

    private Constants.OrderStatus orderStatus;

    @NotEmpty(message = "This field is mandatory")
    private String userName;

    @Email(message = "This email is not valid")
    private String userEmail;

    private List<@Valid ItemOrderInfo> items;

    @Data
    static public class ItemOrderInfo {

        @Min(message = "Minimum order quantity is 1", value = 1)
        private int quantity;

        private String itemId;

        @NotEmpty(message = "This field Name is mandatory")
        private String itemName;

        private LocalDateTime CreationDate;

        private Constants.ItemStatus ItemStatus;

    }

}