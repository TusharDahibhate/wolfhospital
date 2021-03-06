package com.dbms.wh.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbms.wh.bean.Bed;
import com.dbms.wh.bean.Ward;
import com.dbms.wh.dao.BedDAO;
import com.dbms.wh.dao.WardDAO;

@WebServlet("/BedServlet")
public class BedServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private BedDAO bedDAO;
	private WardDAO wardDAO;

	public void init() {
		bedDAO = new BedDAO();
		wardDAO = new WardDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String option = request.getParameter("operation");

		System.out.println(option);
		if (option == null) {
			option = "LIST";
		}

		try {
			switch (option) {
			case "ADD":
				showNewForm(request, response);
				break;
			case "INSERT":
				insertBed(request, response);
				break;
			case "DELETE":
				deleteBed(request, response);
				break;
			case "EDIT":
				showEditForm(request, response);
				break;
			case "UPDATE":
				updateBed(request, response);
				break;
			case "LIST":
				listBed(request, response);
				break;
			case "LISTEMPTY":
				listEmptyBeds(request, response);
				break;
			case "SHOW_BY_WARD":
				listBedsByWard(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listBed(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Bed> listBed = bedDAO.selectAllBeds(false);
		request.setAttribute("listBed", listBed);
		RequestDispatcher dispatcher = request.getRequestDispatcher("bed-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void listEmptyBeds(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		if(request.getParameter("ward_id") != "") {
			listBedsByWard(request, response);
			return;
		}
		List<Bed> listBed = bedDAO.selectAllBeds(true);
		request.setAttribute("listBed", listBed);
		RequestDispatcher dispatcher = request.getRequestDispatcher("bed-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void listBedsByWard(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int ward_id = Integer.parseInt(request.getParameter("ward_id"));
		int empty = request.getParameter("empty") != null ? Integer.parseInt(request.getParameter("empty")) : 1;
		List<Bed> listBed = bedDAO.getAvailableBedsInWard(ward_id, empty);
		request.setAttribute("listBed", listBed);
		request.setAttribute("ward_id", ward_id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("bed-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Ward> listWard = wardDAO.selectAllWards();
		request.setAttribute("wards", listWard);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/bed-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int w_id = Integer.parseInt(request.getParameter("ward_id"));
		Bed existingBed = bedDAO.selectBed(id, w_id);
		List<Ward> listWard = wardDAO.selectAllWards();
		request.setAttribute("wards", listWard);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/bed-form.jsp");
		request.setAttribute("bed", existingBed);
		dispatcher.forward(request, response);
	}

	private void insertBed(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int ward_id = Integer.parseInt(request.getParameter("ward_id"));
		int rate = Integer.parseInt(request.getParameter("rate"));

		Bed newBed = new Bed(ward_id, rate);
		bedDAO.insertBed(newBed);
		response.sendRedirect("BedServlet");
	}

	private void updateBed(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));		
		int ward_id = Integer.parseInt(request.getParameter("ward_id"));		
		int rate = Integer.parseInt(request.getParameter("rate"));
		int checkin_id = Integer.parseInt(request.getParameter("checkin_id"));
		
		Bed newBed = new Bed(id, ward_id, rate, checkin_id);
		bedDAO.updateBed(newBed);
		response.sendRedirect("BedServlet");
	}

	private void deleteBed(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int w_id = Integer.parseInt(request.getParameter("ward_id"));
		bedDAO.deleteBed(id, w_id);
		response.sendRedirect("BedServlet");
	}
}
