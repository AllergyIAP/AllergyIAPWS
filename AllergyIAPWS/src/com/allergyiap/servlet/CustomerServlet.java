package com.allergyiap.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.allergy.service.CustomerService;
import com.allergyiap.beans.Customer;

/**
 * Servlet implementation class CustomerServlet
 */
@WebServlet(description = "Manage the customer segment", urlPatterns = { "/Customers" })
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = (String) request.getParameter("action");

		try {

			if (action == null || action.isEmpty()) {
				listCustomers(request, response);
			} else if (action.equals("new")) {
				newCustomer(request, response);
			} else if (action.equals("edit")) {
				editCustomer(request, response);
			} else if (action.equals("delete")) {
				deleteCustomer(request, response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String action = (String) request.getParameter("action");

			if (action.equals("new")) {
				saveCustomer(request, response);
			} else if (action.equals("edit")) {
				saveCustomer(request, response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void listCustomers(HttpServletRequest request, HttpServletResponse response) {

		List<Customer> customers = CustomerService.getAll();

		request.setAttribute("customers", customers);
		try {
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/pages/customersList.jsp");
			rd.forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = 0;
		if (request.getParameter("id") != null) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		String userName = request.getParameter("nUserName");
		String userPass = request.getParameter("nUserPassword");
		String companyName = request.getParameter("nCompanyName");

		Customer c = new Customer(id, userName, userPass, companyName);

		if (id != 0) {
			CustomerService.update(c);
		} else {
			CustomerService.insert(c);
		}

		response.sendRedirect("Customers");
	}

	private void editCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		Customer customer = CustomerService.get(id);

		request.setAttribute("c", customer);

		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/pages/edit-customer.jsp");
		rd.forward(request, response);

	}

	private void newCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("WEB-INF/pages/new-customer.jsp").forward(request, response);

	}

	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));

		CustomerService.delete(id);

		response.sendRedirect("Customers");
	}

}
