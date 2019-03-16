package cn.e3mall.sso.service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result userLogin(String username, String password) {
        //参数：用户名和密码
        //业务逻辑

        //1.判断用户名和密码是否正确
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(userExample);
        if (list == null || list.size() == 0){
            //2.如果不正确返回失败
            return E3Result.build(400,"用户名或密码错误");
        }
        TbUser user = list.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            //2.如果不正确返回失败
            return E3Result.build(400,"用户名或密码错误");

        }
        //3。如果正确生成tocken
        String token = UUID.randomUUID().toString();
        //4.用户信息写入到Redis，key：token value：用户信息
        user.setPassword(null);
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
        //5.设置session过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIRE);
        //6.把token返回

        //返回值：E3result，其中包含token
        return E3Result.ok(token);
    }
}
