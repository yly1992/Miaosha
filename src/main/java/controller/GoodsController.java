package controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import domain.MiaoshaUser;
import redis.RedisService;
import service.MiaoshaUserService;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
//	 @RequestMapping(value="/to_list")
//	    public String list(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user) {
//System.out.println("req "+ request);
//System.out.println("rep "+ response);
//		Cookie c = WebUtils.getCookie(request,  MiaoshaUserService.COOKI_NAME_TOKEN);
//System.out.println("c "+ response);
////		 MiaoshaUser mu = getUser(request, response);
////System.out.println("miao sha user "+ mu.getNickname());
////System.out.println("miao sha user "+ mu.getId());
//		  model.addAttribute("user", user);
//	    	//List<GoodsVo> goodsList = goodsService.listGoodsVo();
//			//model.addAttribute("goodsList", goodsList);
//	    	return "goods_list";
//	    }
	 
	@RequestMapping("/to_list")
	public String toGoods(Model model, 
			@CookieValue(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String cookieToken,
			@RequestParam(value = MiaoshaUserService.COOKI_NAME_TOKEN, required = false) String paramToken
			) {
System.out.println("GoodList user paramToken "+  paramToken);
System.out.println("GoodList user cookieToken"+  cookieToken);
		//model.addAttribute("user", new MiaoshaUser());
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return "login";
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		MiaoshaUser user = userService.getByToken(token);
		model.addAttribute("user", user);
		return "Goods_list";
	}
	 private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
		//String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
		String cookieToken = "";
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(token);
		 
	 }
}
