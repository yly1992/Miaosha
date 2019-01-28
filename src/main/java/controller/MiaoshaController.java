package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import domain.MiaoshaOrder;
import domain.MiaoshaUser;
import domain.OrderInfo;
import redis.RedisService;
import result.CodeMsg;
import service.GoodsService;
import service.MiaoshaService;
import service.MiaoshaUserService;
import service.OrderService;
import vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
	@RequestMapping("/do_miaosha")
	public String list(Model model, 
					   MiaoshaUser user,
					   @RequestParam("goodsId") long goodsId 
			) {
		if(user == null) {
			return "login";
		}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);
		int stock = goods.getStock_count();
		if(stock <= 0) {
			model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}
		//判断是否秒杀到了
		MiaoshaOrder order = orderService.getMioashaOrderByUserIdGoodsId(user.getId(),goods.getId()); 
		if(order != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
		}
		// 如果秒杀到了， 减库存 下订单
		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
		model.addAttribute("user", user);
		model.addAttribute("orderInfo", orderInfo);
		return "order_detail";
		  
		 
	} 
}