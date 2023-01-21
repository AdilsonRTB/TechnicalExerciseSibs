package cv.pn.sibs.entities;

import cv.pn.sibs.utilities.Constants;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "sibs_order_item")
@Data
public class OrderItem extends CommonAttributes{

    @Min(message = "Minimum order quantity is 1", value = 1)
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fk_item", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "fk_order", nullable = false)
    private Order order;

    @Column(name = "item_status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Constants.ItemStatus itemStatus;
}
