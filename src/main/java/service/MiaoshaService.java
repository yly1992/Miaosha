package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.OrderInfo;
import dao.GoodsDao;
import domain.Goods;
import domain.MiaoshaUser;
import vo.GoodsVo;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		// 如果秒杀成功 减库存 下订单
		goodsService.reduceStock(goods);
		return orderService.createOrder(user, goods);
	}
	
	
	
}
