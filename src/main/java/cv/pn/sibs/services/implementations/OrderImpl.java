package cv.pn.sibs.services.implementations;

import cv.pn.sibs.dtos.EmailDto;
import cv.pn.sibs.dtos.ItemOrderDto;
import cv.pn.sibs.dtos.OrderDto;
import cv.pn.sibs.dtos.OrderInfoDto;
import cv.pn.sibs.entities.*;
import cv.pn.sibs.repositories.*;
import cv.pn.sibs.services.interfaces.IEmail;
import cv.pn.sibs.services.interfaces.IOrder;
import cv.pn.sibs.utilities.APIResponse;
import cv.pn.sibs.utilities.APIUtilities;
import cv.pn.sibs.utilities.Constants;
import cv.pn.sibs.utilities.MessageState;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements IOrder  {

    Logger logger = LoggerFactory.getLogger(OrderImpl.class);

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final StockMovementRepository stockMovementRepository;

    private final OrderItemRepository orderItemRepository;

    private  final IEmail iEmail;

    public OrderImpl(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository, StockMovementRepository stockMovementRepository, OrderItemRepository orderItemRepository, IEmail iEmail) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.orderItemRepository = orderItemRepository;
        this.iEmail = iEmail;
    }

    @Override
    public APIResponse getOrder(String emailOrCode) {

        Optional<User> optionalUser = userRepository.findByEmail(emailOrCode);
        //APIUtilities.checkResource(optionalUser, email + " " + MessageState.DONT_EXIST);
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        try {

            List<Order> orders = orderRepository.findAllByUserOrOrderCode(user, emailOrCode);
            List<Object> objects = orders.stream()
                    .map(order -> {

                        OrderInfoDto orderInfoDto = new OrderInfoDto();

                        orderInfoDto.setOrderCode(order.getOrderCode());
                        orderInfoDto.setUserName(order.getUser().getName());
                        orderInfoDto.setUserEmail(order.getUser().getEmail());
                        orderInfoDto.setOrderStatus(order.getOrderStatus());
                        orderInfoDto.setCreationDate(order.getCreationDate());

                        List<OrderInfoDto.ItemOrderInfo> itens = orderItemRepository.findByOrder(order).stream()
                                .map(orderItem -> {

                                    OrderInfoDto.ItemOrderInfo item = new OrderInfoDto.ItemOrderInfo();

                                    item.setItemId(orderItem.getItem().getId());
                                    item.setItemName(orderItem.getItem().getName());
                                    item.setQuantity(orderItem.getQuantity());
                                    item.setItemStatus(orderItem.getItemStatus());
                                    item.setCreationDate(orderItem.getCreationDate());

                                    return item;
                                }).collect(Collectors.toList());

                        orderInfoDto.setItems(itens);

                        return orderInfoDto;
                    }).collect(Collectors.toList());

            return APIResponse.builder().status(true).statusText(MessageState.SUCCESS).details(objects).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse deleteOrder(String orderCode) {

        try {

            Optional<Order> optionalOrder = orderRepository.findByOrderCode(orderCode);
            APIUtilities.checkResource(optionalOrder, "Order " + orderCode + " " + MessageState.DONT_EXIST);

            orderItemRepository.deleteAll(orderItemRepository.findByOrder(optionalOrder.get()));
            orderRepository.delete(optionalOrder.get());

        return APIResponse.builder().status(true).statusText(MessageState.DELETE_SUCCESS).build();

    } catch (Exception e ) {

        return APIResponse.builder().status(false).statusText(MessageState.DELETE_ERROR).details(Collections.singletonList(e.getMessage())).build();
    }
    }
    @Override
    public APIResponse createOrder(OrderDto dto) {

        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        APIUtilities.checkResource(optionalUser, dto.getEmail() + " " + MessageState.DONT_EXIST);
        User user = optionalUser.get();

        String orderCode = RandomStringUtils.randomAlphanumeric(5).toUpperCase();

        try {

            Order order = new Order();

            order.setOrderCode(orderCode);
            order.setUser(user);
            order.setOrderStatus(Constants.OrderStatus.W);

            orderRepository.saveAndFlush(order);

            List<String> objects = dto.getItems().stream()
                    .map(item -> {

                        OrderItem orderItem = new OrderItem();

                        Optional<Item> optionalItem = itemRepository.findById(item.getIdItem());
                        if(optionalItem.isPresent()) {

                            orderItem.setOrder(order);
                            orderItem.setItem(optionalItem.get());
                            orderItem.setQuantity(item.getQuantity());
                            orderItem.setItemStatus(Constants.ItemStatus.U);

                            Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByItem(optionalItem.get());

                            if (optionalStockMovement.isPresent()) {

                                if(item.getQuantity() <= optionalStockMovement.get().getQuantity()) {

                                    optionalStockMovement.get().setQuantity((optionalStockMovement.get().getQuantity()-item.getQuantity()));

                                    orderItem.setItemStatus(Constants.ItemStatus.C);
                                    stockMovementRepository.saveAndFlush(optionalStockMovement.get());

                                }
                            }

                            logger.warn("Save Order Item");
                            orderItemRepository.saveAndFlush(orderItem);

                        }

                        return String.valueOf(orderItem.getItemStatus());

                    }).collect(Collectors.toList());

            boolean isExistsU = objects.contains("U");
            boolean isExistsC = objects.contains("C");

            if(!isExistsU && isExistsC) {

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

            return APIResponse.builder().status(true).statusText(MessageState.CREATE_SUCCESS).details(getOrder(orderCode).getDetails()).build();

        } catch (Exception e ) {

            logger.error("Error to create order: " + e.getMessage());
            return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse removeItemOrder(String orderCode, String idItem) {

        Optional<Item> optionalItem = itemRepository.findById(idItem);
        APIUtilities.checkResource(optionalItem, "Item " + idItem + " " + MessageState.DONT_EXIST);

        Optional<Order> optionalOrder = orderRepository.findByOrderCode(orderCode);
        APIUtilities.checkResource(optionalOrder, "Order " + orderCode + " " + MessageState.DONT_EXIST);

        Optional<OrderItem> orderItemOptional = orderItemRepository.findByOrderAndItem(optionalOrder.get(), optionalItem.get());
        APIUtilities.checkResource(orderItemOptional, "Order " + orderCode + " with Item " + optionalItem.get().getName() + " " + MessageState.DONT_EXIST);

        try {

            orderItemRepository.delete(orderItemOptional.get());

            return APIResponse.builder().status(true).statusText(MessageState.DELETE_SUCCESS).details(getOrder(orderCode).getDetails()).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.DELETE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }

    }

    @Override
    public APIResponse itemOrder(ItemOrderDto dto) {

        Optional<Item> optionalItem = itemRepository.findById(dto.getIdItem());
        APIUtilities.checkResource(optionalItem, "Item " + MessageState.DONT_EXIST);

        Optional<Order> optionalOrder = orderRepository.findByOrderCode(dto.getOrderCode());
        APIUtilities.checkResource(optionalOrder, "Order " + dto.getOrderCode() + " " + MessageState.DONT_EXIST);

        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        APIUtilities.checkResource(optionalUser, dto.getEmail() + " " + MessageState.DONT_EXIST);

        Constants.ItemStatus status = Constants.ItemStatus.U;

        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByItem(optionalItem.get());
        if (optionalStockMovement.isPresent()) {

            if (dto.getQuantity() <= optionalStockMovement.get().getQuantity()) {
                status = Constants.ItemStatus.C;
                optionalStockMovement.get().setQuantity((optionalStockMovement.get().getQuantity() - dto.getQuantity()));

            }

        }

        try {

            Optional<OrderItem> orderItemOptional = orderItemRepository.findByOrderAndItem(optionalOrder.get(), optionalItem.get());
            if (orderItemOptional.isPresent()){

                orderItemOptional.get().setQuantity(dto.getQuantity());
                orderItemOptional.get().setItemStatus(status);

                orderItemRepository.saveAndFlush(orderItemOptional.get());

                logger.error("New item added in order " + optionalOrder.get().getOrderCode());

            } else {

                try {

                    OrderItem orderItem = new OrderItem();

                    orderItem.setOrder(optionalOrder.get());
                    orderItem.setItem(optionalItem.get());
                    orderItem.setQuantity(dto.getQuantity());
                    orderItem.setItemStatus(status);

                    orderItemRepository.save(orderItem);

                    return APIResponse.builder().status(true).statusText(MessageState.CREATE_SUCCESS).details(getOrder(dto.getOrderCode()).getDetails()).build();

                } catch (Exception e ) {

                    logger.error("Error add new item in order " + e.getMessage());

                    return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
                }

            }

            return APIResponse.builder().status(true).statusText(MessageState.UPDATE_SUCCESS).details(getOrder(dto.getOrderCode()).getDetails()).build();

        } catch (Exception e ) {

            logger.error("Error to update item in order " + e.getMessage());
            return APIResponse.builder().status(false).statusText(MessageState.UPDATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }
}
