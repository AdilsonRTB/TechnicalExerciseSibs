package cv.pn.sibs.services.interfaces;

import cv.pn.sibs.dtos.ItemDto;
import cv.pn.sibs.utilities.APIResponse;

import java.util.List;
public interface IItem {

    APIResponse getItem();

    APIResponse deleteItem(String id);

    APIResponse updateItem(ItemDto dto, String id);

    APIResponse createItem(List<ItemDto> items);
}
