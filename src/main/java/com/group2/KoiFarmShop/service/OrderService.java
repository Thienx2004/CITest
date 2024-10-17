package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.OrderDetailReponse;
import com.group2.KoiFarmShop.dto.response.OrderHistoryReponse;
import com.group2.KoiFarmShop.dto.response.PaginReponse;
import com.group2.KoiFarmShop.dto.request.OrderRequest;
import com.group2.KoiFarmShop.entity.*;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService implements OrderServiceImp{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private KoiFishRepository koiFishRepository;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Transactional
    @Override
    public Orders addOrder(OrderRequest order) {
        try {
            Account account = accountRepository.findByAccountID(order.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));
            if(order.getPromoCode() != null){
                Promotion promotion = promotionRepository.findByPromoCode(order.getPromoCode())
                        .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_INVALID));

                account.setPromotion(promotion);
                accountRepository.save(account);
            }

            Orders orders = new Orders();
            orders.setAccount(account);
            orders.setTotalPrice(order.getTotalPrice());
            orders.setOrder_date(new Date());
            orderRepository.save(orders);  // Lưu trước khi sử dụng ở OrderDetail

            List<OrderDetail> orderDetails = new ArrayList<>();

            if (order.getKoiFishs() != null && order.getKoiFishs().length != 0) {
                for (int i = 0; i < order.getKoiFishs().length; i++) {
                    KoiFish koiFish = new KoiFish();
                    koiFish.setKoiID(order.getKoiFishs()[i]);

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrders(orders);
                    orderDetail.setKoiFish(koiFish);
                    orderDetail.setType(true);
                    orderDetail.setQuantity(order.getQuantity()[i]);
                    orderDetails.add(orderDetail);
                }
            }

            if (order.getBatchs() != null && order.getBatchs().length != 0) {
                for (int i = 0; i < order.getBatchs().length; i++) {
                    Batch batch = new Batch();
                    batch.setBatchID(order.getBatchs()[i]);

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrders(orders);
                    orderDetail.setBatch(batch);
                    orderDetail.setQuantity(order.getQuantity()[i]);
                    orderDetail.setType(false);
                    orderDetails.add(orderDetail);
                }
            }

            for (int i = 0; i < order.getPrice().length; i++) {
                orderDetails.get(i).setPrice(order.getPrice()[i]);
            }

            for (int i = 0; i < order.getQuantity().length; i++) {
                if(orderDetails.get(i).isType()) {
                    KoiFish koiFish = koiFishRepository.findByKoiID(orderDetails.get(i).getKoiFish().getKoiID());
                    koiFish.setStatus(2);  // Giả định "2" là trạng thái "bán"
                    koiFishRepository.save(koiFish);
                } else {
                    Batch batch = batchRepository.findByBatchID(orderDetails.get(i).getBatch().getBatchID())
                            .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));

                    int remainingQuantity = batch.getQuantity() - order.getQuantity()[i];
                    batch.setQuantity(remainingQuantity);
                    if(remainingQuantity == 0){
                        batch.setStatus(2);  // Giả định "2" là trạng thái "hết hàng"
                    }
                    batchRepository.save(batch);
                }
                orderDetails.get(i).setQuantity(order.getQuantity()[i]);
            }
            // Xử lý nếu có đơn ký gửi trong đơn hàng
//            if (order.getConsignmentID() != 0) {
//                Consignment consignment = consignmentRepository.findConsignmentByConsignmentID(order.getConsignmentID())
//                        .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));
//
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrders(orders);
//                orderDetail.setConsignment(consignment);
//                orderDetails.add(orderDetail);
//
//                consignment.setStatus(2);  // Status 2 = thanh toán và đơn đã đc duyệt
//                consignmentRepository.save(consignment);
//            }

            orderDetailRepository.saveAll(orderDetails);

            // Gửi email xác nhận sau khi đơn hàng đã lưu thành công
            //emailService.sendOrderConfirmationEmail(account.getEmail(), orders.getOrderID());

            return orders;
        } catch (Exception e) {
            System.out.println("Error insert order: " + e.getMessage());
            throw new RuntimeException(e.getMessage());  // Ném lại ngoại lệ
        }
    }


//    @Override
//    public PaginReponse<OrderHistoryReponse> getOrdersHistory(int pageNo, int pageSize, String accountId, String type) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("order_date").descending());
//        // Kiểm tra null hoặc chuỗi rỗng trước khi chuyển đổi
//        Integer accountIdInt = (accountId != null && !accountId.isEmpty()) ? Integer.parseInt(accountId) : null;
//        Boolean typeBool = (type != null && !type.isEmpty()) ? Boolean.parseBoolean(type) : null;
//
//        // Gọi đến repository với các tham số đã kiểm tra
//        Page<Orders> orders = orderRepository.findOrdersWithFilters(accountIdInt, typeBool, pageable);
//        List<OrderHistoryReponse> orderHistoryReponses = new ArrayList<>();
//
//        for (Orders order : orders.getContent()) {
//            OrderHistoryReponse orderHistoryReponse = new OrderHistoryReponse();
//            orderHistoryReponse.setOrderId(order.getOrderID());
//            orderHistoryReponse.setAccountId(order.getAccount().getAccountID());
//            orderHistoryReponse.setCreatedDate(order.getOrder_date());
//            orderHistoryReponse.setTotalPrice(order.getTotalPrice());
//
//            List<OrderDetailReponse> orderDetails = new ArrayList<>();
//            for(OrderDetail orderDetail : order.getOrderDetails()){
//                OrderDetailReponse orderDetailReponse = new OrderDetailReponse();
//
//                orderDetailReponse.setOrderDetailId(orderDetail.getOrderDetailID());
//                if(orderDetail.getKoiFish() != null) {
//                    orderDetailReponse.setCategoryName(orderDetail.getKoiFish().getCategory().getCategoryName());
//                    orderDetailReponse.setKoiFishId(orderDetail.getKoiFish().getKoiID());
//                    orderDetailReponse.setGender(orderDetail.getKoiFish().isGender());
//                    orderDetailReponse.setKoiAge(orderDetail.getKoiFish().getAge());
//                    orderDetailReponse.setKoiSize(orderDetail.getKoiFish().getSize());
//                } else {
//                    orderDetailReponse.setCategoryName(orderDetail.getBatch().getCategory().getCategoryName());
//                    orderDetailReponse.setBatchId(orderDetail.getBatch().getBatchID());
//                    orderDetailReponse.setAvgSize(orderDetail.getBatch().getAvgSize());
//                    orderDetailReponse.setKoiPrice(orderDetail.getBatch().getPrice());
//                    orderDetailReponse.setBatchPrice(orderDetail.getBatch().getPrice());
//                }
//                orderDetailReponse.setQuantity(orderDetail.getQuantity());
//                orderDetailReponse.setType(orderDetail.isType());
//                orderDetailReponse.setQuantity(orderDetail.getQuantity());
//
//                orderDetails.add(orderDetailReponse);
//            }
//            orderHistoryReponse.setOrderDetails(orderDetails);
//            orderHistoryReponses.add(orderHistoryReponse);
//        }
//
//        PaginReponse<OrderHistoryReponse> paginReponse = new PaginReponse<>();
//        paginReponse.setContent(orderHistoryReponses);
//        paginReponse.setPageNum(pageNo);
//        paginReponse.setPageSize(pageSize);
//        paginReponse.setTotalElements(orders.getNumberOfElements());
//        paginReponse.setTotalPages(orders.getTotalPages());
//
//        return paginReponse;
//    }

    @Override
    public PaginReponse<OrderHistoryReponse> getOrdersHistory(int pageNo, int pageSize, String accountId) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("order_date").descending());
        // Kiểm tra null hoặc chuỗi rỗng trước khi chuyển đổi
        Integer accountIdInt = (accountId != null && !accountId.isEmpty()) ? Integer.parseInt(accountId) : null;


        Page<Orders> orders = orderRepository.findOrdersWithFilters(accountIdInt, pageable);
        List<OrderHistoryReponse> orderHistoryReponses = new ArrayList<>();

        for (Orders order : orders.getContent()) {
            OrderHistoryReponse orderHistoryReponse = new OrderHistoryReponse();
            orderHistoryReponse.setOrderId(order.getOrderID());
            orderHistoryReponse.setAccountId(order.getAccount().getAccountID());
            orderHistoryReponse.setFullName(order.getAccount().getFullName());
            orderHistoryReponse.setTransactionCode(order.getPayment().getTransactionCode());
            orderHistoryReponse.setCreatedDate(order.getOrder_date());
            orderHistoryReponse.setTotalPrice(order.getTotalPrice());
            orderHistoryReponse.setStatus(order.isStatus());
            orderHistoryReponse.setPaymentId(order.getPayment().getPaymentID());


            orderHistoryReponses.add(orderHistoryReponse);
        }

        PaginReponse<OrderHistoryReponse> paginReponse = new PaginReponse<>();
        paginReponse.setContent(orderHistoryReponses);
        paginReponse.setPageNum(pageNo);
        paginReponse.setPageSize(pageSize);
        paginReponse.setTotalElements(orders.getNumberOfElements());
        paginReponse.setTotalPages(orders.getTotalPages());

        return paginReponse;
    }

    @Override
    public List<OrderDetailReponse> getOrderDetail(int orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrders_OrderID(orderId);
        List<OrderDetailReponse> orderDetailReponseList = new ArrayList<>();

        for(OrderDetail orderDetail : orderDetails){
            OrderDetailReponse orderDetailReponse = new OrderDetailReponse();

            orderDetailReponse.setOrderDetailId(orderDetail.getOrderDetailID());
            if(orderDetail.getKoiFish() != null) {
                orderDetailReponse.setCategoryName(orderDetail.getKoiFish().getCategory().getCategoryName());
                orderDetailReponse.setKoiFishId(orderDetail.getKoiFish().getKoiID());
                orderDetailReponse.setGender(orderDetail.getKoiFish().isGender());
                orderDetailReponse.setKoiAge(orderDetail.getKoiFish().getAge());
                orderDetailReponse.setKoiSize(orderDetail.getKoiFish().getSize());
                orderDetailReponse.setPrice(orderDetail.getPrice());
                orderDetailReponse.setKoiImg(orderDetail.getKoiFish().getKoiImage());
            } else {
                orderDetailReponse.setCategoryName(orderDetail.getBatch().getCategory().getCategoryName());
                orderDetailReponse.setBatchId(orderDetail.getBatch().getBatchID());
                orderDetailReponse.setAvgSize(orderDetail.getBatch().getAvgSize());
                orderDetailReponse.setPrice(orderDetail.getPrice());
                orderDetailReponse.setBatchImg(orderDetail.getBatch().getBatchImg());
            }
            orderDetailReponse.setQuantity(orderDetail.getQuantity());
            orderDetailReponse.setType(orderDetail.isType());
            orderDetailReponse.setQuantity(orderDetail.getQuantity());

            orderDetailReponseList.add(orderDetailReponse);

            }
        return orderDetailReponseList;
    }


}
