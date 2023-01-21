package cv.pn.sibs.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class StockItemDto {

    @NotEmpty(message = "This field ID Item is mandatory")
    private String idItem;

    @Min(message = "Minimum order quantity is 1", value = 1)
    private int quantity;
}
