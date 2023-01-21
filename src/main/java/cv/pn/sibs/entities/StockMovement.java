package cv.pn.sibs.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "SIBS_STOCKMOVEMENT")
@Data
public class StockMovement extends  CommonAttributes {

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fk_item", nullable = false)
    private Item item;
}
