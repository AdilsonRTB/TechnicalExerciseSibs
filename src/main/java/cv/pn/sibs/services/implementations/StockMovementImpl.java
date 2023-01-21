package cv.pn.sibs.services.implementations;

import cv.pn.sibs.dtos.EmailDto;
import cv.pn.sibs.dtos.StockItemDto;
import cv.pn.sibs.dtos.StockMovementDto;
import cv.pn.sibs.entities.Item;
import cv.pn.sibs.entities.Order;
import cv.pn.sibs.entities.OrderItem;
import cv.pn.sibs.entities.StockMovement;
import cv.pn.sibs.repositories.*;
import cv.pn.sibs.services.interfaces.IEmail;
import cv.pn.sibs.services.interfaces.IOrder;
import cv.pn.sibs.services.interfaces.IStockMovement;
import cv.pn.sibs.utilities.APIResponse;
import cv.pn.sibs.utilities.APIUtilities;
import cv.pn.sibs.utilities.Constants;
import cv.pn.sibs.utilities.MessageState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockMovementImpl implements IStockMovement {

    Logger logger = LoggerFactory.getLogger(StockMovementImpl.class);

    private final StockMovementRepository stockMovementRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    private final IEmail iEmail;

    private final OrderItemRepository orderItemRepository;

    private final IOrder iOrder;

    public StockMovementImpl(StockMovementRepository stockMovementRepository, ItemRepository itemRepository, OrderRepository orderRepository, IEmail iEmail, OrderItemRepository orderItemRepository, IOrder iOrder) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.iEmail = iEmail;
        this.orderItemRepository = orderItemRepository;
        this.iOrder = iOrder;
    }

    @Override
    public APIResponse getStockMovement() {

        try {
            List<StockMovement> stockMovements = stockMovementRepository.findAll();
            List<Object> objects = stockMovements.stream()
                    .map(stockMovement -> {

                        StockMovementDto dto = new StockMovementDto();

                        dto.setId(stockMovement.getId());
                        dto.setCreationDate(stockMovement.getCreationDate());
                        dto.setQuantity(stockMovement.getQuantity());
                        dto.setItemId(stockMovement.getItem().getId());
                        dto.setItemName(stockMovement.getItem().getName());
                        dto.setItemCreationDate(stockMovement.getItem().getCreationDate());

                        return dto;
                    }).collect(Collectors.toList());
            return APIResponse.builder().status(true).statusText(MessageState.SUCCESS).details(objects).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse deleteStockMovement(String idItem) {

        Optional<Item> optionalItem = itemRepository.findById(idItem);
        APIUtilities.checkResource(optionalItem, "Item " + optionalItem.get().getName() + " " + MessageState.DONT_EXIST);

        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByItem(optionalItem.get());
        APIUtilities.checkResource(optionalStockMovement, "Item " + optionalItem.get().getName() + " " + MessageState.DONT_EXIST + " in the Stock");


        try {

            stockMovementRepository.delete(optionalStockMovement.get());

            return APIResponse.builder().status(true).statusText(MessageState.DELETE_SUCCESS).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.DELETE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse CreateStockMovement(StockItemDto dto) {

        logger.info("Movement Stock.....");

        Optional<Item> optionalItem = itemRepository.findById(dto.getIdItem());
        APIUtilities.checkResource(optionalItem, "Item " + dto.getIdItem() + " " + MessageState.DONT_EXIST);

        APIResponse apiResponse;

        StockMovement stockMovement = new StockMovement();

        try {

            Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByItem(optionalItem.get());
            if(optionalStockMovement.isPresent()) {

                stockMovement = optionalStockMovement.get();

                try {

                    stockMovement.setQuantity(dto.getQuantity() + stockMovement.getQuantity());

                    stockMovementRepository.saveAndFlush(stockMovement);

                    logger.info("Item " + optionalItem.get().getName().toUpperCase() + "existent new quantity: " +stockMovement.getQuantity());

                    apiResponse = APIResponse.builder().status(true).statusText(MessageState.UPDATE_SUCCESS).details(getStockMovement().getDetails()).build();

                } catch (Exception e ) {

                    logger.error("Error Movement Stock: " + e.getMessage());

                    return APIResponse.builder().status(false).statusText(MessageState.UPDATE_ERROR).details(Collections.singletonList(e.getMessage())).build();

                }
            } else {

                stockMovement.setQuantity(dto.getQuantity());
                stockMovement.setItem(optionalItem.get());
                stockMovement.setCreationDate(LocalDateTime.now());

                stockMovementRepository.saveAndFlush(stockMovement);

                logger.info("New item added: " + optionalItem.get().getName().toUpperCase() + " with quantity: " +dto.getQuantity());

                apiResponse = APIResponse.builder().status(true).statusText(MessageState.CREATE_SUCCESS).details(getStockMovement().getDetails()).build();
            }

            List<Order> orders = orderRepository.findAllByOrderStatusOrderByCreationDateAsc(Constants.OrderStatus.W);

            for (Order order: orders) {

                List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

                List<String> strings = new ArrayList<>();

                for (OrderItem e : orderItems) {

                    if (e.getItem().getId().equals(optionalItem.get().getId()) && e.getItemStatus().equals(Constants.ItemStatus.U)) {

                        if (e.getQuantity() <= stockMovement.getQuantity()) {

                            e.setItemStatus(Constants.ItemStatus.C);

                            orderItemRepository.saveAndFlush(e);

                            stockMovement.setQuantity((stockMovement.getQuantity() - e.getQuantity()));

                            stockMovementRepository.saveAndFlush(stockMovement);

                        }
                    }

                    strings.add(String.valueOf(e.getItemStatus()));
                }

                if (!strings.contains("U") && strings.contains("C")) {

                    order.setOrderStatus(Constants.OrderStatus.C);

                    orderRepository.saveAndFlush(order);

                    logger.info("Order " + order.getOrderCode() + " concluded");

                    EmailDto emailDto = new EmailDto();
                    emailDto.setRecipient(order.getUser().getEmail());
                    emailDto.setMsgBody("Hello, " + order.getUser().getName() + "!\n Your Order is already completed.\n\n SIBS");
                    emailDto.setSubject("SIBS ORDER");
                    emailDto.setAttachment(null);

                    iEmail.sendSimpleMail(emailDto);
                }
            }

            return apiResponse;

        } catch (Exception e ) {

            logger.error("Error Movement Stock: " + e.getMessage());
            return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }
}
