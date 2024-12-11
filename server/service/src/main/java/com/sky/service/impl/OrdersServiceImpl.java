package com.sky.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.ws.WebSocketServer;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersServiceImpl
 * DESCRIPTION : C端-订单信息服务层接口实现类
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    //注入订单信息持久层接口实现类
    @Resource
    private OrdersMapper ordersMapper;
    //注入地址信息持久层接口实现类
    @Resource
    private AddressBookMapper addressBookMapper;
    //注入购物车信息持久层接口实现类
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    //注入订单详情信息持久层接口实现类
    @Resource
    private OrderDetailMapper orderDetailMapper;
    //注入WebSocket的服务器端类
    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO 订单基本信息
     * @return 提交订单响应数据Bean ( 订单Id / 订单编号 / 订单金额 / 订单时间)
     */
    @Transactional(rollbackFor = Exception.class) //无论出现哪种异常都回滚 (正常:出现运行期异常才回滚)
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //(1)校验用户的地址是否合法(校验是否存在)
        AddressBook addressBook = addressBookMapper.selectAddressBookById(ordersSubmitDTO.getAddressBookId());
        if (Objects.isNull(addressBook))
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL); //不存在则抛出异常
        //(2)校验用户的购物车数据是否合法(校验购物车有没有商品)
        Long userId = BaseContext.getCurrentId();
        ShoppingCart conditionShoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(conditionShoppingCart);
        if (Objects.isNull(shoppingCartList) || shoppingCartList.size() == 0)
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        //(3)将OrdersSubmitDTO中的数据拷贝到一个Orders对象中,并且填充Orders中的剩余内容
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis())); //将当前时间毫秒值转换为字符串后作为订单编号
        orders.setStatus(Orders.PENDING_PAYMENT); //指定订单状态为待付款(Orders的静态常量)
        orders.setPayStatus(Orders.UN_PAID); //指定订单支付状态为未付款(Orders的静态常量)
        orders.setUserId(userId); //指定下单用户Id
        orders.setOrderTime(LocalDateTime.now()); //指定下单时间
        orders.setConsignee(addressBook.getConsignee()); //指定收货人姓名
        orders.setPhone(addressBook.getPhone()); //指定收货人收集
        orders.setAddress(addressBook.getFullAddress()); //指定收货详细地址
        //(4)调用持久层完成订单基本信息的添加(返回主键)
        ordersMapper.insertOrder(orders);
        //(5)将购物车中的数据转换为订单详情数据后添加到订单详情表
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((Function<ShoppingCart, OrderDetail>) shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail, "id"); //将购物车数据映射给订单详情数据(忽略id)
            orderDetail.setOrderId(orders.getId()); //指定订单详情对应的订单Id
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailMapper.batchInsertOrderDetail(orderDetailList);
        //END:将订单的信息封装到OrderSubmitVO中响应
        return OrderSubmitVO.builder()
                .orderTime(orders.getOrderTime()) //下单时间
                .orderNumber(orders.getNumber()) //订单编号
                .orderAmount(orders.getAmount()) //订单金额
                .id(orders.getId()).build(); //订单Id
    }

    /**
     * 用户请求支付功能
     *
     * @param ordersPaymentDTO 用于请求支付相关信息(订单号码/付款方式)
     * @return 微信支付基础信息VO
     */
    @Override
    public OrderPaymentVO pay(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setNonceStr("666");
        vo.setPaySign("hhh");
        vo.setPackageStr("prepay_id=wx");
        vo.setSignType("RSA");
        vo.setTimeStamp("1670380960");
        return vo;
    }


    /**
     * 支付成功功能(更改订单状态)
     *
     * @param orderNumber 订单编号
     */
    @SneakyThrows //Lombok可以把必须要处理/声明的编译期异常基于该注解变成运行期
    @Transactional
    @Override
    public void paySuccess(String orderNumber) {
        //根据当前用户Id与订单编号查询原始订单信息
        Orders orders = ordersMapper.selectOrderByOrderNumberAndUserId(BaseContext.getCurrentId(), orderNumber);
        //封装新的订单更新条件对象并进行更新
        Orders newOrder = Orders.builder().id(orders.getId()) //订单Id(基于订单Id更新)
                .status(Orders.TO_BE_CONFIRMED) //修改订单状态为待接单(Orders中的静态常量)
                .payStatus(Orders.PAID) //修改订单支付状态为已支付(Orders中的静态常量)
                .checkoutTime(LocalDateTime.now()) //结账时间
                .build();
        //将购物车清空
        ordersMapper.updateOrder(newOrder);
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());
        //封装一个Map(发送给客户端的通知信息)
        HashMap<String, Object> infoMap = new HashMap<>();
        infoMap.put("type", 1); //弹框类型
        infoMap.put("orderId", orders.getId()); //订单Id(超链接访问)
        infoMap.put("content", "订单号:" + orderNumber); //通知信息 订单号:
        //将Map转换为Json格式的字符串用于发送给客户端
        String responseData = JSONUtil.toJsonStr(infoMap);
        webSocketServer.serverSendMessage2AllClient(responseData); //将信息发送给所有的客户端
    }

    /**
     * 用户查询历史订单
     *
     * @param ordersPageQueryDTO 查询参数(页码+页数+状态)
     * @return 分页查询结果Bean ( 总条数 + OrderVO的List集合)
     */
    @Override
    public PageResult selectHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        //开启分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //将userId封装到ordersPageQueryDTO中(前台没有传递,查询当前登录用户的历史订单信息)
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        //调用订单基本信息持久层查询结果,将结果转换为OrderVO对象(可以封装订单详情)
        Page<Orders> ordersPage = ordersMapper.selectOrderByCondition(ordersPageQueryDTO);
        List<OrderVO> orderVOList = ordersPage.getResult().stream().map(orders -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO); //将基本信息赋值给orderVo
            List<OrderDetail> orderDetailList = orderDetailMapper.selectOrderDetailByOrderId(orders.getId());
            orderVO.setOrderDetailList(orderDetailList);
            return orderVO;
        }).collect(Collectors.toList());
        return PageResult.builder().total(ordersPage.getTotal()).records(orderVOList).build();
    }

    /**
     * 用户查询订单详情
     *
     * @param orderId 订单Id
     * @return 订单基本信息 + 订单详情信息
     */
    @Override
    public OrderVO selectOrder(Long orderId) {
        //调用订单基本信息的Mapper查询Orders基本信息
        Orders order = ordersMapper.selectOrderById(orderId);
        //调用订单详情信息的Mapper查询Orders对应的详情信息
        List<OrderDetail> orderDetailList = orderDetailMapper.selectOrderDetailByOrderId(orderId);
        //将基本信息与详情集合进行组装为OrderVO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 用户取消订单
     *
     * @param orderId 要取消的订单Id
     */
    @Override
    public void cancelOrder(Long orderId) {
        //查询本次要取消的订单的基本信息(判断是否可以取消)
        Orders orders = ordersMapper.selectOrderById(orderId);
        if (Objects.isNull(orders) || orders.getStatus() >= Orders.CONFIRMED)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        //封装一个保存更新后数据的Orders对象
        Orders updateOrdres = Orders.builder()
                .id(orderId) //用于指定基于哪个订单Id进行数据更新
                .status(Orders.CANCELLED) //指定订单状态为取消
                .payStatus(Orders.REFUND) //指定支付状态为退款(模拟退款)
                .cancelReason("用户取消订单")
                .cancelTime(LocalDateTime.now()).build();
        //调用持久层将指定订单Id的订单信息完成更新
        ordersMapper.updateOrder(updateOrdres);
    }

    /**
     * 用户再来一单
     *
     * @param orderId 订单Id
     */
    @Override
    public void repetitionOrder(Long orderId) {
        //查询出订单Id对应的订单详情数据(每一个商品买了多少份,买的是什么)
        List<OrderDetail> orderDetailList = orderDetailMapper.selectOrderDetailByOrderId(orderId);
        //将订单详情数据转换为购物车数据后添加到购物车表中
        orderDetailList.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            return shoppingCart;
        }).forEach(shoppingCartMapper::insertShoppingCart);
    }

    /**
     * 商户订单搜索
     *
     * @param ordersPageQueryDTO 搜索条件参数Bean
     * @return 分页数据信息Bean ( 总条数 + OrderVO)
     */
    @Override
    public PageResult selectOrderByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        //开启分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //使用持久层完成订单查询
        Page<Orders> ordersPage = ordersMapper.selectOrderByCondition(ordersPageQueryDTO);
        List<OrderVO> orderVOList = ordersPage.getResult().stream().map(orders -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO); //拷贝基础信息
            List<OrderDetail> orderDetailList = orderDetailMapper.selectOrderDetailByOrderId(orders.getId());
            //将订单详情集合中的每一个订单详情里面购买的商品名称与件数拼接为一个字符串 鸡米花*3 汉堡*2
            StringBuilder dishesInfo = new StringBuilder();
            orderDetailList.forEach(orderDetail -> {
                dishesInfo.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(" ");
            });
            String orderDishes = dishesInfo.toString();
            orderVO.setOrderDishes(orderDishes);
            return orderVO;
        }).collect(Collectors.toList());
        return PageResult.builder().total(ordersPage.getTotal()).records(orderVOList).build();
    }

    /**
     * 统计不同状态订单数量
     *
     * @return 不同状态订单数量封装Bean
     */
    @Override
    public OrderStatisticsVO statisticsOrder() {
        //方案:使用持久层分组聚合查询不同状态的订单数伪表,并且只用一个集合返回List<Map<String,Object>>
        List<Map<String, Object>> resultList = ordersMapper.selectOrderCount();
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        resultList.forEach(resultMap -> {
            //获取本次Map保存的状态的值进行switch判断
            switch ((Integer) resultMap.get("status")) {
                case 2: //待接单
                    orderStatisticsVO.setToBeConfirmed(Integer.valueOf(resultMap.get("count").toString()));
                    break;
                case 3: //待派送
                    orderStatisticsVO.setConfirmed(Integer.valueOf(resultMap.get("count").toString()));
                    break;
                case 4: //派送中
                    orderStatisticsVO.setDeliveryInProgress(Integer.valueOf(resultMap.get("count").toString()));
                    break;
            }
        });
        return orderStatisticsVO;
    }

    /**
     * 商家接单功能
     *
     * @param orders 用于接收传递过来的要接单的订单Id
     */
    @Override
    public void confirmOrder(Orders orders) {
        //查询当前要接单的订单信息
        Orders resultOrder = ordersMapper.selectOrderById(orders.getId());
        //只有查询到的订单信息不是NULL并且订单状态是待接单才可以接单
        if (Objects.isNull(resultOrder) || !resultOrder.getStatus().equals(Orders.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        orders.setStatus(Orders.CONFIRMED); //orders有Id也有修改后的状态信息
        ordersMapper.updateOrder(orders);
    }

    /**
     * 商家拒绝订单
     *
     * @param orders 用于接收传递的拒绝订单原因和订单Id
     */
    @Override
    public void rejectionOrder(Orders orders) {
        //查询当前要拒绝的订单信息
        Orders resultOrder = ordersMapper.selectOrderById(orders.getId());
        //只有查询到的订单信息不是NULL并且订单状态是待接单才可以拒单
        if (Objects.isNull(resultOrder) || !resultOrder.getStatus().equals(Orders.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        orders.setStatus(Orders.CANCELLED); //指定订单状态取消
        orders.setCancelReason(orders.getRejectionReason()); //指定取消原因为拒绝原因
        orders.setCancelTime(LocalDateTime.now()); //指定取消时间
        orders.setPayStatus(Orders.REFUND); //模拟退款
        ordersMapper.updateOrder(orders);
    }

    /**
     * 商家取消订单
     *
     * @param orders 用于接收传递的取消订单原因和订单Id
     */
    @Override
    public void adminCancelOrder(Orders orders) {
        //查询当前要取消的订单信息
        Orders resultOrder = ordersMapper.selectOrderById(orders.getId());
        //只有查询到的订单信息不是NULL并且订单状态是待派送/派送中才可以取消订单
        if (Objects.isNull(resultOrder))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        if (resultOrder.getStatus() < Orders.CONFIRMED || resultOrder.getStatus() > Orders.DELIVERY_IN_PROGRESS)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        orders.setStatus(Orders.CANCELLED); //指定订单状态取消
        orders.setCancelTime(LocalDateTime.now()); //指定取消时间
        orders.setPayStatus(Orders.REFUND); //模拟退款
        ordersMapper.updateOrder(orders);
    }

    /**
     * 商家派送订单
     *
     * @param orderId 要派送的订单Id
     */
    @Override
    public void deliveryOrder(Long orderId) {
        //查询当前要拒绝的订单信息
        Orders resultOrder = ordersMapper.selectOrderById(orderId);
        //只有查询到的订单信息不是NULL并且订单状态是已接单才可以派送
        if (Objects.isNull(resultOrder) || !resultOrder.getStatus().equals(Orders.CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        Orders orders = Orders.builder().id(orderId).status(Orders.DELIVERY_IN_PROGRESS).build();
        ordersMapper.updateOrder(orders);
    }

    /**
     * 商家完成订单
     *
     * @param orderId 要完成的订单Id
     */
    @Override
    public void completeOrder(Long orderId) {
        //查询当前要拒绝的订单信息
        Orders resultOrder = ordersMapper.selectOrderById(orderId);
        //只有查询到的订单信息不是NULL并且订单状态是派送中才可以完成订单
        if (Objects.isNull(resultOrder) || !resultOrder.getStatus().equals(Orders.DELIVERY_IN_PROGRESS))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        Orders orders = Orders.builder().id(orderId).status(Orders.COMPLETED).deliveryTime(LocalDateTime.now()).build();
        ordersMapper.updateOrder(orders);
    }

    /**
     * 用户催单
     *
     * @param orderId 用户催促接单的订单Id
     */
    @SneakyThrows
    @Override
    public void reminderOrder(Long orderId) {
        //基于订单Id查询订单基本信息
        Orders orders = ordersMapper.selectOrderById(orderId);
        if (Objects.nonNull(orders) && orders.getStatus().equals(Orders.TO_BE_CONFIRMED) && orders.getPayStatus().equals(Orders.PAID)) {
            //封装一个Map(发送给客户端的通知信息)
            HashMap<String, Object> infoMap = new HashMap<>();
            infoMap.put("type", 2); //弹框类型
            infoMap.put("orderId", orders.getId()); //订单Id(超链接访问)
            infoMap.put("content", "订单号:" + orders.getNumber()); //通知信息 订单号:
            //将Map转换为Json格式的字符串用于发送给客户端
            String responseData = JSONUtil.toJsonStr(infoMap);
            webSocketServer.serverSendMessage2AllClient(responseData); //将信息发送给所有的客户端
        }
    }
}
