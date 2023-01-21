package cv.pn.sibs.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cv.pn.sibs.utilities.Constants;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "SIBS_ORDER")
@Data
public class Order extends CommonAttributes implements Serializable {

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "order_status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Constants.OrderStatus orderStatus;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;
    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

}
