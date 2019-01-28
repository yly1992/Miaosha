package controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import domain.Goods;
import domain.MiaoshaUser;
import redis.RedisService;
import service.GoodsService;
import service.MiaoshaUserService;
import vo.GoodsVo;

import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
//	@CookieValue(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String cookieToken,
//	@RequestParam(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String paramToken
	 
	@RequestMapping("/to_list")
	public String toGoods(Model model, MiaoshaUser user
			) {
		model.addAttribute("user", user);
		
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		List<Goods> allGoods =  goodsService.allGoods();
		model.addAttribute("goodsList" , goodsList);
		return "Goods_list";
	}
	
	@RequestMapping("/to_detail/{goodsId}")
	public String toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
		model.addAttribute("user", user);
		//snowflake generate ID 
System.out.println("goodsId " + goodsId);
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
System.out.println("goodsId " + goods);
		model.addAttribute("goods", goods);
		
		long startAt = goods.getStart_Date().getTime();
		long endAt = goods.getEnd_Date().getTime();
		long now = System.currentTimeMillis();
System.out.println("startAt " + startAt);
System.out.println("endAt " + endAt);
System.out.println("now " + now);
// 0 秒杀未开始 ，1 秒杀 进行中 ， 2 秒杀已结束
		int miaoshaStatus = 0;
		int remianSeconds = 0;
		
		if(now < startAt) {
			miaoshaStatus = 0;
			remianSeconds = (int)((startAt - now)/ 100);
		}else if(now > endAt){
			miaoshaStatus = 2;
			remianSeconds = -1;
		}else {
			miaoshaStatus = 1;
			remianSeconds  = 0;
		}
		
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remianSeconds);		
		return "goods_detail";
	}
	
}
