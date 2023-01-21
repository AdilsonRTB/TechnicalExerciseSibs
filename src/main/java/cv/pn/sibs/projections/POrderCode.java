package cv.pn.sibs.projections;

import cv.pn.sibs.utilities.Constants;

import java.time.LocalDateTime;

/**
 * A Projection for the {@link cv.pn.sibs.entities.Order} entity
 */
public interface POrderCode {

    String getOrderCode();

    Constants.OrderStatus getOrderStatus();

    LocalDateTime getCreationDate();

}