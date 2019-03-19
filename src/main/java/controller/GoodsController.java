package controller;
import java.util.function.*;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import domain.Goods;
import domain.MiaoshaUser;
import redis.GoodsKey;
import redis.RedisService;
import result.Result;
import service.GoodsService;
import service.MiaoshaUserService;
import vo.GoodsDetailVo;
import vo.GoodsVo;

import org.springframework.web.util.WebUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
// test commit
@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
//	@CookieValue(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String cookieToken,
//	@RequestParam(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String paramToken
	 
	@RequestMapping(value ="/to_list",produces="text/html")
	@ResponseBody
	public String toGoods(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response
			) {
		model.addAttribute("user", user);
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		List<Goods> allGoods =  goodsService.allGoods();
		model.addAttribute("goodsList" , goodsList);
//      return "goods_list";
		//去缓存
		String html = redisService.get(GoodsKey.getGoodsList,"", String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
	        
		// 手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList,"", html);
		}
		return html;
	}
	
	@RequestMapping(value = "/to_detail2/{goodsId}", produces="text/html")
	@ResponseBody
	public String toDetail2(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("user", user);
		
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId, String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		
				
		//snowflake generate ID 
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);
		
		long startAt = goods.getStart_Date().getTime();
		long endAt = goods.getEnd_Date().getTime();
		long now = System.currentTimeMillis();
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
		
		// 手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail,"", html);
		}

		return html;
	}
	
	@RequestMapping(value = "/to_detail/{goodsId}", produces="text/html")
	@ResponseBody
	public Result<GoodsDetailVo> toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId, HttpServletRequest request, HttpServletResponse response) {
		
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		
		long startAt = goods.getStart_Date().getTime();
		long endAt = goods.getEnd_Date().getTime();
		long now = System.currentTimeMillis();
// 0 秒杀未开始 ，1 秒杀 进行中 ， 2 秒杀已结束
		int miaoshaStatus = 0;
		int remainSeconds = 0;
		
		if(now < startAt) {
			miaoshaStatus = 0;
			remainSeconds = (int)((startAt - now)/ 100);
		}else if(now > endAt){
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else {
			miaoshaStatus = 1;
			remainSeconds  = 0;
		}
		GoodsDetailVo gdvo =  new GoodsDetailVo();
		gdvo.setGoods(goods);
		gdvo.setMiaoshaStatus(miaoshaStatus);
		gdvo.setRemianSeconds(remainSeconds);
		gdvo.setUser(user);
		return Result.success(gdvo);
	}
	
}
