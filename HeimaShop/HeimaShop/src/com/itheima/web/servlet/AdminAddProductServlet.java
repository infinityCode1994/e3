package com.itheima.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.AdminService;
import com.itheima.utils.CommonUtils;

/**
 * Servlet implementation class AdminAddProductServlet
 */
public class AdminAddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminAddProductServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Product product = new Product();
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> parseRequest = upload.parseRequest(request);
			 //System.out.println(parseRequest.size());

			//int i=0;
			for (FileItem item : parseRequest) {
				boolean formField = item.isFormField();
				if (formField) {
					String fieldName = item.getFieldName();
					String fieldvalue = item.getString("UTF-8");
					map.put(fieldName, fieldvalue);
					// System.out.println(fieldName+":"+fieldvalue);
					// i++;

				} else {
					String filename = item.getName();
					
					String path = this.getServletContext().getRealPath("upload");
					InputStream in = item.getInputStream();
					OutputStream out = new FileOutputStream(path + "/" + filename);
					IOUtils.copy(in, out);
					in.close();
					out.close();
					item.delete();
					// private String pimage;
					map.put("pimage", "upload/"+filename);
				}
				
			}
			// System.out.println(i);

				BeanUtils.populate(product, map);
				// private String pid;
				product.setPid(CommonUtils.getUUID());

				// private Date pdate;
				product.setPdate(new Date());
				// private int pflag;
				product.setPflag(0);

				// private Category category;
				Category category = new Category();
				// System.out.println(map.get("cid"));
				category.setCid(map.get("cid").toString());
				product.setCategory(category);
				AdminService service = new AdminService();
				service.addProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
