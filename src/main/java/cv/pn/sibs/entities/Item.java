package cv.pn.sibs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "SIBS_ITEM")
@Data
public class Item extends CommonAttributes  {

    @NotEmpty(message = "This field Name is mandatory")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @JsonIgnore
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<StockMovement> stockMovements;
}
