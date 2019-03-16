package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登陆状态，把购物车写入Redis
        if (user != null){
            //保存到服务端
            cartService.addCart(user.getId(),itemId,num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //如若是未登录状态使用cookie
        //从cookie中取购物车列表
        List<TbItem> catList = getCatListFromCookie(request);
        //判断商品在购物车列表中是否存在
        boolean flag =false;
        for (TbItem tbItem : catList) {
            if (tbItem.getId() == itemId.longValue()){
                //如果存在商品数量加一
                tbItem.setNum(tbItem.getNum()+num);
                flag=true;
                //跳出循环
                break;
            }
        }
        //如果不存在，根据商品id查询商品信息。得到一个tbitem对象
        if (!flag){
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)){
                tbItem.setImage(image.split(",")[0]);
            }
            //把商品添加到商品列表
            catList.add(tbItem);
        }
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(catList),COOKIE_CART_EXPIRE,true);
        //返回添加成功页面
        return "cartSuccess";
    }
    //从cookie中取购物车列表的处理
    private List<TbItem> getCatListFromCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isBlank(json)){
            return new ArrayList<>();
        }
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response, Model model){
        //从cookie中取购物车商品列表
        List<TbItem> cartList = getCatListFromCookie(request);
        getCatListFromCookie(request);
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态
        if (user != null){
            //如果不为空，把cookie中的商品和服务端的商品合并
            cartService.mergeCart(user.getId(),cartList);
            //删除cookie中的购物车
            CookieUtils.deleteCookie(request,response,"cart");
            //从服务端取购物车列表
            cartList = cartService.getCartList(user.getId());
        }
        //未登录
        //传递给页面
        model.addAttribute("cartList",cartList);
        return "cart";
    }
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }
        //从cookie中取购物车商品列表
        List<TbItem> cartList = getCatListFromCookie(request);
        //遍历商品列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表写回cookie
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回成功
        return E3Result.ok();
    }
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId,HttpServletRequest request,
                             HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        //从cookie中取购物车商品列表
        List<TbItem> cartList = getCatListFromCookie(request);
        //遍历商品列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                //更新数量
                cartList.remove(tbItem);
                break;
            }
        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回成功
        return "redirect:/cart/cart.html";
    }
}
