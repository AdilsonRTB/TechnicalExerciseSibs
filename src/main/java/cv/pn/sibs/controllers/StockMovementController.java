package cv.pn.sibs.controllers;

import cv.pn.sibs.dtos.StockItemDto;
import cv.pn.sibs.services.interfaces.IStockMovement;
import cv.pn.sibs.utilities.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/stock")
@CrossOrigin
public class StockMovementController {

    private final IStockMovement iStockMovement;

    public StockMovementController(IStockMovement iStockMovement) {
        this.iStockMovement = iStockMovement;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> stockMovement(@RequestBody @Valid StockItemDto dto) {

        APIResponse apiResponse = iStockMovement.CreateStockMovement(dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> getStockMovement() {

        APIResponse apiResponse = iStockMovement.getStockMovement();

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{idItem}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deleteUser(@PathVariable("idItem") String idItem) {

        APIResponse apiResponse = iStockMovement.deleteStockMovement(idItem);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
