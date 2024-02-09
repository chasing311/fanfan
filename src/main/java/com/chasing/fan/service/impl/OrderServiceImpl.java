package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.common.CustomException;
import com.chasing.fan.entity.*;
import com.chasing.fan.mapper.OrderMapper;
import com.chasing.fan.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders orders) {
        Long userId = orders.getUserId();
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUserId(userId);
        if (shoppingCartList == null) {
            throw new CustomException("购物车数据为空，不能下单");
        }
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }
        User user = userService.getById(userId);
        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(orderId);
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setAddressBookId(addressBookId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setPhone(addressBook.getPhone());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "":addressBook.getProvinceName()) +
                        (addressBook.getCityName() == null ? "":addressBook.getCityName()) +
                        (addressBook.getDistrictName() == null ? "":addressBook.getDistrictName()) +
                        (addressBook.getDetail() == null ? "":addressBook.getDetail())
        );
        this.save(orders);
        orderDetailService.saveBatch(orderDetailList);
        shoppingCartService.removeByUserId(userId);
    }

    @Override
    public Page<OrdersDTO> pageWithDetail(int page, int pageSize, Long userId) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDTO> ordersDTOPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersQueryWrapper = new LambdaQueryWrapper<>();
        ordersQueryWrapper.eq(Orders::getUserId, userId);
        ordersQueryWrapper.orderByDesc(Orders::getOrderTime);
        this.page(ordersPage, ordersQueryWrapper);

        List<OrdersDTO> ordersDTOList = ordersPage.getRecords().stream().map((item) -> {
            OrdersDTO ordersDTO = new OrdersDTO();
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> details = orderDetailService.list(detailQueryWrapper);
            BeanUtils.copyProperties(item, ordersDTO);
            ordersDTO.setOrderDetails(details);
            return ordersDTO;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(ordersPage, ordersDTOPage, "records");
        ordersDTOPage.setRecords(ordersDTOList);

        return ordersDTOPage;
    }
}
