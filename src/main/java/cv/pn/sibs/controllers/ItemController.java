package cv.pn.sibs.controllers;

import cv.pn.sibs.dtos.ItemDto;
import cv.pn.sibs.services.interfaces.IItem;
import cv.pn.sibs.utilities.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/item")
@CrossOrigin
public class ItemController {

    private final IItem iItem;

    public ItemController(IItem iItem) {
        this.iItem = iItem;
    }


    @Operation(summary = "Get all items")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> getItem() {

        APIResponse apiResponse = iItem.getItem();

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Created Item")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestBody @Valid List<ItemDto> dto) {

        APIResponse apiResponse = iItem.createItem(dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "updated item by id Item")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> updateItem(@RequestBody @Valid ItemDto dto, @PathVariable("id") String id) {

        APIResponse apiResponse = iItem.updateItem(dto, id);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete item by id Item")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deleteItem(@PathVariable("id") String id) {

        APIResponse apiResponse = iItem.deleteItem(id);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
