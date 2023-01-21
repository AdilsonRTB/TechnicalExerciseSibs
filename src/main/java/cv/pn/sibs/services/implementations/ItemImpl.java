package cv.pn.sibs.services.implementations;

import cv.pn.sibs.dtos.ItemDto;
import cv.pn.sibs.entities.Item;
import cv.pn.sibs.repositories.ItemRepository;
import cv.pn.sibs.services.interfaces.IItem;
import cv.pn.sibs.utilities.APIResponse;
import cv.pn.sibs.utilities.APIUtilities;
import cv.pn.sibs.utilities.MessageState;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemImpl implements IItem {

    private final ItemRepository itemRepository;

    public ItemImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public APIResponse getItem() {

        try {

            List<Item> items = itemRepository.findAll();
            List<Object> objects = new ArrayList<>(items);

            return APIResponse.builder().status(true).statusText(MessageState.SUCCESS).details(objects).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse deleteItem(String id) {

        Optional<Item> optionalItem = itemRepository.findById(id);
        APIUtilities.checkResource(optionalItem, id + " " + MessageState.DONT_EXIST);
        Item item = optionalItem.get();

        try {

            itemRepository.delete(item);

            return APIResponse.builder().status(true).statusText(MessageState.DELETE_SUCCESS).details(getItem().getDetails()).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.DELETE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse updateItem(ItemDto dto, String id) {

        Optional<Item> optionalItem = itemRepository.findById(id);
        APIUtilities.checkResource(optionalItem, dto.getName() + " with " + id + " " + MessageState.DONT_EXIST);
        Item item = optionalItem.get();

        try {

            item.setName(dto.getName());
            itemRepository.save(item);

            return APIResponse.builder().status(true).statusText(MessageState.UPDATE_SUCCESS).details(Collections.singletonList(item)).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.UPDATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse createItem(List<ItemDto> items) {

        try {

            List<Object> objects = items.stream()
                            .map(dto -> {

                                Item item = new Item();

                                BeanUtils.copyProperties(dto, item);
                                itemRepository.save(item);

                                return item;

                            }).collect(Collectors.toList());

            return APIResponse.builder().status(true).statusText(MessageState.CREATE_SUCCESS).details(getItem().getDetails()).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }
}
