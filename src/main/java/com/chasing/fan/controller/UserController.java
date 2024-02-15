package com.chasing.fan.controller;

import com.chasing.fan.common.MailUtil;
import com.chasing.fan.common.Result;
import com.chasing.fan.common.SessionUtil;
import com.chasing.fan.entity.User;
import com.chasing.fan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取验证码
     * @param user
     * @return
     * @throws MessagingException
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user) throws MessagingException {
        String phone = user.getPhone();
        if (!phone.isEmpty()) {
            String code = MailUtil.achieveCode();
            log.info("邮箱: {}, 验证码: {}", phone, code);
            MailUtil.sendTestMail(phone, code);
            //验证码存redis，方便后面拿出来比对
            redisTemplate.opsForValue().set(phone, code, 3, TimeUnit.MINUTES);
            return Result.success("验证码发送成功");
        }
        return Result.error("验证码发送失败");
    }

    /**
     * 用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        //从redis中获取验证码
        String codeInRedis = redisTemplate.opsForValue().get(phone);
        if (codeInRedis == null) {
            return Result.error("请先获取验证码再登录");
        }
        log.info("输入的code:{}，session中的code:{}，计算结果为{}", code, codeInRedis, (code != null && code.equals(codeInRedis)));
        //比较用户输入的验证码和redis中存的验证码是否一致
        if (code != null && code.equals(codeInRedis)) {
            User user = userService.getUserByPhone(phone);
            //如果不存在，则创建一个，存入数据库
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setName("用户" + codeInRedis);
                userService.save(user);
                log.info("新用户: {}", "用户" + codeInRedis);
            }
            SessionUtil.setUserId(session, user.getId());
            redisTemplate.delete(phone);
            return Result.success(user);
        }
        return Result.error("登录失败");
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        SessionUtil.removeUserId(session);
        return Result.success("退出成功");
    }
}
