package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }
    @RequestMapping("/usr/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
      return registerService.checkData(param,type);
    }
    @RequestMapping(value = "/usr/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser tbUser){
        E3Result register = registerService.register(tbUser);
        return register;
    }
}
