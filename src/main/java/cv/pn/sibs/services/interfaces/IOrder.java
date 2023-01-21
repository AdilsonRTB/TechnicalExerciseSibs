package cv.pn.sibs.services.interfaces;

import cv.pn.sibs.dtos.ItemOrderDto;
import cv.pn.sibs.dtos.OrderDto;
import cv.pn.sibs.utilities.APIResponse;

public interface IOrder {

    APIResponse getOrder(String emailOrOrderCode);

    APIResponse deleteOrder(String orderCode);

    //APIResponse updateOrder(OrderDto order, String orderCode);

    APIResponse createOrder(OrderDto order);

    APIResponse removeItemOrder(String orderCode, String idItem);

    APIResponse itemOrder(ItemOrderDto dto);
}
