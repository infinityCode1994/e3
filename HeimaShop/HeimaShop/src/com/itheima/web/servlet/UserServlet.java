package com.itheima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.CommonUtils;
import com.itheima.utils.MailUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public UserServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("UTF-8");
//		String methodName = request.getParameter("method");
//		if("Active".equals(methodName)){
//			Active(request,response);
//		}else if("CheckUserName".equals(methodName)){
//			CheckUserName(request,response);
//		}else if("Register".equals(methodName)){
//			Register(request,response);
//		}
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
	public void Active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String activeCode = request.getParameter("activeCode");
		UserService service = new UserService();
		service.active(activeCode);
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
	public void CheckUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		UserService service = new UserService();
		boolean isExist = service.checkUsername(username);
		String json = "{\"isExist\":"+isExist+"}";
		response.getWriter().write(json);
	}
	public void Register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//获得表单数据
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			//自己指定类型转换器（将string装换成data）
			ConvertUtils.register(new Converter() {
				
				@Override
				public Object convert(Class clazz, Object value) {
					//将string转换成data
					SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
					Date parse = null;
					try {
						 parse = format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
			}, Date.class);
					
					
			//映射封装
			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//private String uid; 
		user.setUid(CommonUtils.getUUID());
		//private String telephone; 
		user.setTelephone(null);
		//private int state;是否激活
		user.setState(0);
		//private String code;激活码
		String activeCode = CommonUtils.getUUID();
		user.setCode(activeCode);
		
		
		
		//将user传递给service层
		UserService service = new UserService();
		boolean isRegisterSuccess = service.register(user);
		//是否注册成功
		if(isRegisterSuccess){
			//用户激活
			String emailMsg = "恭喜您注册成功，请点击下面链接进行激活"+
			"<a href='${pageContext.request.contextPath}/user?method=Active&activeCode="+activeCode+"'>"
			+"href='${pageContext.request.contextPath}/user?method=Active&activeCode="+activeCode+"</a>";
		
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		//跳转到注册成功的页面
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else{
			//跳转到失败页面
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");

		}
	}
	public void Login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String checkcode_webInput = request.getParameter("checkImg");
		HttpSession session = request.getSession();
		String checkcode_session = (String) session.getAttribute("checkcode_session");
		// System.out.println(checkcode_webInput);
		// System.out.println(checkcode_session);
		if(!checkcode_webInput.equals(checkcode_session)){
			request.setAttribute("loginInfo", "验证码不正确");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//session.setAttribute("loginInfo","");
		UserService service = new UserService();
		User user = new User();
		user = service.login(username,password);
		if(user!=null){
			String autoLogin = request.getParameter("autoLogin");
			if(autoLogin!=null){
				String encode_username = URLEncoder.encode(username,"UTF-8");
				Cookie cookie_username = new Cookie("cookie_username",encode_username);
				Cookie cookie_password = new Cookie("cookie_password",password);
				cookie_username.setMaxAge(60*60*12);
				cookie_password.setMaxAge(60*60*12);
				cookie_username.setPath("/");
				cookie_password.setPath("/");
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath());
		}else{
			session.setAttribute("loginInfo","用户名或密码错误");
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}

	}
	public void Logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		Cookie cookie_username = new Cookie("cookie_username",null); //假如要删除名称为username的Cookie 
		cookie_username.setMaxAge(0);
		cookie_username.setPath("/"); //项目所有目录均有效，这句很关键，否则不敢保证删除 		
		response.addCookie(cookie_username);//重新写入，将覆盖之前的 
		Cookie cookie_password = new Cookie("cookie_password",null); //假如要删除名称为username的Cookie 
		cookie_password.setMaxAge(0);
		cookie_password.setPath("/"); //项目所有目录均有效，这句很关键，这句很关键，这句很关键，这句很关键，这句很关键!!!!!!!!!!!!!!!否则不敢保证删除 		
		response.addCookie(cookie_password);//重新写入，将覆盖之前的 
		response.sendRedirect(request.getContextPath()+"/login.jsp");
		
	}
	public void clearLoginInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute("loginInfo","");

		response.sendRedirect(request.getContextPath()+"/login.jsp");
		
	}
}
