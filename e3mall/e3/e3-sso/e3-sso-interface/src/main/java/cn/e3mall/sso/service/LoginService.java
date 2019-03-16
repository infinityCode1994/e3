package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {
    //参数：用户名和密码
    //业务逻辑
    /*
    1.判断用户名和密码是否正确
    2.如果不正确返回失败
    3。如果正确生成tocken
    4.用户信息写入到Redis，key：token value：用户信息
    5.设置session过期时间
    6.把token返回
     */
    //返回值：E3result，其中包含token
    E3Result userLogin(String username,String password);
}
