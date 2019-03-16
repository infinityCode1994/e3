package com.itheima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.Product;
import com.itheima.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
   
	public void findCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			AdminService service = new AdminService();
			List<Category> categoryList = service.findCategory();
//			System.out.println("没有值");
//			if(categoryList==null){
//				System.out.println("没有值");
//			}else{
//				for (Category category : categoryList) {
//					System.out.println(category);
//				}
//			}
//			
			Gson gson = new Gson();
			String json = gson.toJson(categoryList);
			response.setContentType("text/html;Charset=UTF-8");
			response.getWriter().write(json);
		}
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service = new AdminService();
		List<Order> orderList = service.findAllOrders();
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
	}
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String oid = request.getParameter("oid");
		AdminService service = new AdminService();
		List<Map<String,Object>> mapList = service.findOrderInfoByOid(oid);
		// Order order = new Order();
		// order.setOid(oid);
		// for (Map<String, Object> map : mapList) {
		// OrderItem orderItem = new OrderItem();
		// try {
		// BeanUtils.populate(orderItem, map);
		// Product product = new Product();
		// BeanUtils.populate(product, map);
		// orderItem.setProduct(product);
		// order.getOrderItems().add(orderItem);
		// } catch (IllegalAccessException | InvocationTargetException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		//
		// request.setAttribute("order", order);
		// System.out.println(order.getOid());
		//request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
		Gson gson = new Gson();
		String json = gson.toJson(mapList);
		System.out.println(json);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
		
	}

}
