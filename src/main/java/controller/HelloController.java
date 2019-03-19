package controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import result.Result;
import result.CodeMsg;

import domain.User;
import redis.RedisService;
import service.UserService;


@RestController
@RequestMapping("/demo")
public class HelloController {
	
	@Autowired
    UserService us;
	
	@Autowired
	RedisService rs;
	
    @RequestMapping("/hello")
    public String hello(){
        return "Greetings from Spring Boot!";
    }
    
    
    @RequestMapping("/test")
    public Result<User> dbGet(){
        User user = us.getById(1);
        return Result.success(user);
    }
    
    @RequestMapping(value = "/hello.html")
	public String hello(
			@CookieValue(value = "hitCounter", defaultValue = "0") Long hitCounter,
			HttpServletResponse response) {

		// increment hit counter
		hitCounter++;

		// create cookie and set it in response
		Cookie cookie = new Cookie("hitCounter", hitCounter.toString());
		cookie.setPath("/");
		response.addCookie(cookie);

		// render hello.jsp page
		return "hello";
	}

    
//    @RequestMapping("/redis/get")
//    public Result<String> redisGet(){
//       String v1 = rs.get("key1", String.class);
//       return Result.success(v1);
//    }
    // add some test git here
    
    
    
}