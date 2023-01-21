package cv.pn.sibs.controllers;

import cv.pn.sibs.dtos.ItemOrderDto;
import cv.pn.sibs.dtos.OrderDto;
import cv.pn.sibs.services.interfaces.IOrder;
import cv.pn.sibs.utilities.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/order")
public class OrderController {

    private final IOrder iOrder;

    public OrderController(IOrder iOrder) {
        this.iOrder = iOrder;
    }

    @Operation(summary = "created order with 1 or more items")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@RequestBody @Valid OrderDto dto) {

        APIResponse apiResponse = iOrder.createOrder(dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "get users by order code or email")
    @GetMapping(path = "/{emailOrOrderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> getOrder(@PathVariable("emailOrOrderCode") String emailOrOrderCode) {

        APIResponse apiResponse = iOrder.getOrder(emailOrOrderCode);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete order by order code")
    @DeleteMapping(path = "/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deleteUser(@PathVariable("orderCode") String orderCode) {

        APIResponse apiResponse = iOrder.deleteOrder(orderCode);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete item in order by order code and id item")
    @DeleteMapping(path = "/remove-item/{orderCode}/{idItem}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> removeItemOrder(@PathVariable("orderCode") String orderCode, @PathVariable("idItem") String idItem) {

        APIResponse apiResponse = iOrder.removeItemOrder(orderCode, idItem);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "add item in a existent order")
    @PostMapping(path = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> itemOrder(@RequestBody @Valid ItemOrderDto dto) {

        APIResponse apiResponse = iOrder.itemOrder(dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
