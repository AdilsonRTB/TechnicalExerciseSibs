package cv.pn.sibs.services.interfaces;

import cv.pn.sibs.dtos.StockItemDto;
import cv.pn.sibs.entities.StockMovement;
import cv.pn.sibs.utilities.APIResponse;

public interface IStockMovement {

    APIResponse getStockMovement();

    APIResponse deleteStockMovement(String idItem);

    //APIResponse updateStockMovement(StockMovement stockMovement);

    APIResponse CreateStockMovement(StockItemDto dto);
}
