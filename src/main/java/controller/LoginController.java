package controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import redis.RedisService;
import result.CodeMsg;
import result.Result;
import util.ValidatorUtil;
import service.MiaoshaUserService;
import vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
    	log.info(loginVo.toString());
    	//登录
    	//参数校验
//    	String passinput = loginVo.getPassword();
//    	String mobile = loginVo.getMobile(); 
//    	if(StringUtils.isEmpty(passinput)) {
//    		return Result.error(CodeMsg.PASSWORD_EMPTY);
//    	}
//    	if(StringUtils.isEmpty(mobile)) {
//    		return Result.error(CodeMsg.MOBILE_EMPTY);
//    	}
//    	if(!ValidatorUtil.isMobile(mobile)) {
//    		return Result.error(CodeMsg.MOBILE_ERROR); 
//    	}
//    	
    	//String token = userService.login(response, loginVo);
    	//return Result.success(token);
    	CodeMsg cm = userService.login(response, loginVo);
    	if(cm.getCode() == 0) {
    		return Result.success("true");
    	}else {
    		return Result.error(cm);
    	}
    }
}
