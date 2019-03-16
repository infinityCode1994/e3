package com.itheima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.itheima.domain.CartItem;
import com.itheima.domain.Cart;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.domain.User;
import com.itheima.service.ProductService;
import com.itheima.utils.CommonUtils;
import com.itheima.utils.JedisPoolUtils;
import com.itheima.utils.PaymentUtil;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public ProductServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String methodName = request.getParameter("method");
//		if("CategoryList".equals(methodName)){
//			CategoryList(request,response);
//		}else if("FindProductListByCid".equals(methodName)){
//			FindProductListByCid(request,response);
//		}else if("Index".equals(methodName)){
//			Index(request,response);
//		}else if("ProductInfo".equals(methodName)){
//			ProductInfo(request,response);
//		}
//		
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
	//模块功能
	public void CategoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		if(categoryListJson==null){
			System.out.println("缓存没有数据，需要到数据库读取");
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}
		
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	public void FindProductListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null){
			currentPageStr="1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;
		String cid = request.getParameter("cid");
		ProductService service = new ProductService();
		PageBean pageBean = service.findProductListByCid(cid,currentPage,currentCount);
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
//		List<Product> list = pagebean.getList();
//		for(int i=0;i<12;i++){
//			System.out.println(list);
//		}
		List<Product> historyProductList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for(String pid :split){
						Product pro = service.findProductInfoByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}
		request.setAttribute("historyProductList", historyProductList);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
	public void Index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		List<Product> hotProductList = service.findHotProductList();
		List<Product> newProductList = service.findNewProductList();
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		 
	        
	      /*  for(Product ss:hotProductList) {
	            System.out.println(ss);
	        }*/
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	public void ProductInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		String cid = request.getParameter("cid");
		String currentPage = request.getParameter("currentPage");
		ProductService service = new ProductService();
		Product product = service.findProductInfoByPid(pid);
		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					if(list.contains(pid)){
						list.remove(pid);
						list.addFirst(pid);
					}else{
						list.addFirst(pid);
					}
					StringBuffer sb = new StringBuffer();
					for(int i=0;i<list.size()&&i<7;i++){
						sb.append(list.get(i));
						sb.append("-");
					}
					pids = sb.substring(0, sb.length()-1);
					
				}
			}
		}
		Cookie cookie_pids = new Cookie("pids",pids);
		response.addCookie(cookie_pids);
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}
	public void Cart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String pid = request.getParameter("pid");
		ProductService service = new ProductService();
		Product product = service.findProductInfoByPid(pid);
		CartItem item = new CartItem();
		item.setProduct(product);
		int byNum = Integer.parseInt(request.getParameter("byNum"));
		item.setByNum(byNum);
		double subtotal = byNum*product.getShop_price();
		item.setSubtotal(subtotal);
		
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart==null){
			cart = new Cart();
		}
		Map<String, CartItem> cartItems = cart.getCartItems();
		double newsubtotal = 0.0;
		if(cartItems.containsKey(pid)){
			CartItem cartItem = cartItems.get(pid);
			int oldByNum = cartItem.getByNum();
			oldByNum+=byNum;
			cartItem.setByNum(oldByNum);
			cart.setCartItems(cartItems);
			double oldsubtotal = cartItem.getSubtotal();
			newsubtotal = byNum*product.getShop_price();
			cartItem.setSubtotal(newsubtotal+oldsubtotal);
		}else{
			cart.getCartItems().put(pid, item);
			newsubtotal = byNum*product.getShop_price();
		}

		double total = cart.getTotal()+newsubtotal;
		//System.out.println(total);
		cart.setTotal(total);
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void DelProductFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart!=null){
			Map<String, CartItem> cartItems = cart.getCartItems();
			cart.setTotal(cart.getTotal()-cartItems.get(pid).getSubtotal());
			cartItems.remove(pid);
			cart.setCartItems(cartItems);
			
		}
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	public void ClearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void SubmitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		Order order = new Order();
		// private String oid;
		String oid = CommonUtils.getUUID();
		order.setOid(oid);
		// private Date ordertime;
		order.setOrdertime(new Date());
		// private double total;
		Cart cart = (Cart) session.getAttribute("cart");
		order.setTotal(cart.getTotal());
		// private int state;
		order.setState(0);
		// private String address;
		order.setAddress(null);
		// private String name;
		order.setName(null);
		// private String telephone;
		order.setTelephone(null);
		// private User user;
		order.setUser(user);
		// List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Map<String, CartItem> cartItems = cart.getCartItems();
		for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
			CartItem cartItem = entry.getValue();
			OrderItem orderItem = new OrderItem();
			// private String itemid;
			orderItem.setItemid(CommonUtils.getUUID());
			// private int count;
			orderItem.setCount(cartItem.getByNum());
			// private double subtotal;
			orderItem.setSubtotal(cartItem.getSubtotal());
			// private Product product;
			orderItem.setProduct(cartItem.getProduct());
			// private Order order;
			orderItem.setOrder(order);
			order.getOrderItems().add(orderItem);
		}
	
		ProductService service = new ProductService();
		service.submitOrder(order);
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
	}
	public void ConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(order.getTotal());

		ProductService service = new ProductService();
		service.updateOrderInfo(order);
		
				// 获得 支付必须基本数据
				String orderid = request.getParameter("oid");
				//由于测试时易宝支付对于测试商家进行限额所以金额不能太大这里把money改为0.01
//				String money = order.getTotal()+"";
				String money = "0.01";
//				System.out.println(order.getTotal());
//				System.out.println(money);
//				System.out.println(orderid);

				// 银行
				String pd_FrpId = request.getParameter("pd_FrpId");

				// 发给支付公司需要哪些数据
				String p0_Cmd = "Buy";
				String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
				String p2_Order = orderid;
				String p3_Amt = money;
				String p4_Cur = "CNY";
				String p5_Pid = "";
				String p6_Pcat = "";
				String p7_Pdesc = "";
				// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
				// 第三方支付可以访问网址
				String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
				String p9_SAF = "";
				String pa_MP = "";
				String pr_NeedResponse = "1";
				// 加密hmac 需要密钥
				String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
						"keyValue");
				String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
						p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
						pd_FrpId, pr_NeedResponse, keyValue);
				
				
				String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
								"&p0_Cmd="+p0_Cmd+
								"&p1_MerId="+p1_MerId+
								"&p2_Order="+p2_Order+
								"&p3_Amt="+p3_Amt+
								"&p4_Cur="+p4_Cur+
								"&p5_Pid="+p5_Pid+
								"&p6_Pcat="+p6_Pcat+
								"&p7_Pdesc="+p7_Pdesc+
								"&p8_Url="+p8_Url+
								"&p9_SAF="+p9_SAF+
								"&pa_MP="+pa_MP+
								"&pr_NeedResponse="+pr_NeedResponse+
								"&hmac="+hmac;

				//重定向到第三方支付平台
				response.sendRedirect(url);
	}
	
	
	
	public void MyOrderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		ProductService service = new ProductService();
		List<Order> orderList = service.findOrderListByUid(user.getUid());
		for (Order order : orderList) {
			List<Map<String, Object>> itemList = service.findOrderItemsListByOid(order.getOid());
			for (Map<String, Object> maplist : itemList) {
				OrderItem orderItem = new OrderItem();
				try {
					BeanUtils.populate(orderItem, maplist);
					Product product = new Product();
					BeanUtils.populate(product, maplist);
					orderItem.setProduct(product);
					order.getOrderItems().add(orderItem);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}
		session.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
	}
	public void findProductNameByword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String word = request.getParameter("word");
		//System.out.println(word);
		ProductService service= new ProductService();
		List<Object> productNameList = service.findProductNameByword(word);
		Gson gson = new Gson();
		String json = gson.toJson(productNameList);
		//System.out.println(json);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
	}

}

