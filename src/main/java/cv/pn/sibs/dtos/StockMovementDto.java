package cv.pn.sibs.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link cv.pn.sibs.entities.StockMovement} entity
 */
@Data
public class StockMovementDto implements Serializable {
    private  String id;
    private  LocalDateTime creationDate;
    private  int quantity;
    private  String itemId;
    private  LocalDateTime itemCreationDate;
    @NotEmpty(message = "This field Name is mandatory")
    private  String itemName;
}