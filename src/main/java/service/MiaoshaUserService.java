package service;
import java.util.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.MiaoshaUserDao;

import domain.MiaoshaUser;
import exception.GlobalException;
import redis.MiaoshaUserKey;
import redis.RedisService;
import result.CodeMsg;
import util.MD5Util;
import util.UUIDUtil;
import vo.LoginVo;


@Service
public class MiaoshaUserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	@Autowired
	RedisService redisService;
	
	public MiaoshaUser getById(long id) {
//		//取缓存
//		MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
//		if(user != null) {
//			return user;
//		}
//		//取数据库
//		user = miaoshaUserDao.getById(id);
//		if(user != null) {
//			redisService.set(MiaoshaUserKey.getById, ""+id, user);
//		}
		return miaoshaUserDao.getById(id);
		
	}
	public CodeMsg login(HttpServletResponse response,LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		String passinput = loginVo.getPassword();
    	String mobile = loginVo.getMobile(); 
    	// 判断手机号是否存在
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
//System.out.print("test a "+ dbPass + "   $    " +  saltDB );
		String calcPass = MD5Util.formPassToDBPass(passinput, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		// 生成cookie
		String token = UUIDUtil.uuid();
		redisService.set(MiaoshaUserKey.token, token, user);
System.out.println("test  wow"+ MiaoshaUserKey.token + token);
		
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setPath("/");
		cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
		
		response.addCookie(cookie);
		return CodeMsg.SUCCESS;
	}
	// http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
//	public boolean updatePassword(String token, long id, String formPass) {
//		//取user
//		MiaoshaUser user = getById(id);
//		if(user == null) {
//			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//		}
//		//更新数据库
//		MiaoshaUser toBeUpdate = new MiaoshaUser();
//		toBeUpdate.setId(id);
//		toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//		miaoshaUserDao.update(toBeUpdate);
//		//处理缓存
//		redisService.delete(MiaoshaUserKey.getById, ""+id);
//		user.setPassword(toBeUpdate.getPassword());
//		redisService.set(MiaoshaUserKey.token, token, user);
//		return true;
//	}
	public MiaoshaUser getByToken(String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		return redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
	}


//	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
//		if(StringUtils.isEmpty(token)) {
//			return null;
//		}
//		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
//		//延长有效期
//		if(user != null) {
//			addCookie(response, token, user);
//		}
//		return user;
//	}
//	

//	public String login(HttpServletResponse response, LoginVo loginVo) {
//		if(loginVo == null) {
//			throw new GlobalException(CodeMsg.SERVER_ERROR);
//		}
//		String mobile = loginVo.getMobile();
//		String formPass = loginVo.getPassword();
//		//判断手机号是否存在
//		MiaoshaUser user = getById(Long.parseLong(mobile));
//		if(user == null) {
//			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//		}
//		//验证密码
//		String dbPass = user.getPassword();
//		String saltDB = user.getSalt();
//		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//		if(!calcPass.equals(dbPass)) {
//			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
//		}
//		//生成cookie
//		String token	 = UUIDUtil.uuid();
//		addCookie(response, token, user);
//		return token;
//	}
//	
//	private vo id addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
//		redisService.set(MiaoshaUserKey.token, token, user);
//		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
//		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//		cookie.setPath("/");
//		response.addCookie(cookie);
//	}

}