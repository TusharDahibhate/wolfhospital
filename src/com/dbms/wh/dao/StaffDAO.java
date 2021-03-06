package com.dbms.wh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dbms.wh.bean.Patient;
import com.dbms.wh.bean.Staff;

public class StaffDAO {
	public static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/cagarwa3";
	public static final String user = "cagarwa3";
	public static final String password = "200234585";
	Connection connection = null;
	Statement statement = null;
	ResultSet result = null;

	private static final String INSERT_STAFF_SQL = "INSERT INTO staff(name, age, gender, job_title, professional_title, phone_no, address, department) VALUES " + " (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String SELECT_STAFF_BY_ID = "SELECT * FROM staff where id =?";
	private static final String SELECT_ALL_STAFF = "SELECT * FROM staff";
	private static final String SELECT_ALL_OPERATORS = "SELECT * FROM staff WHERE job_title = \"operator\";";
	private static final String SELECT_ALL_DOCTORS = "SELECT * FROM staff WHERE job_title = \"doctor\";";
	private static final String DELETE_STAFF_SQL = "DELETE FROM staff WHERE id = ?;";
	private static final String UPDATE_STAFF_SQL = "UPDATE staff SET name = ?,age= ?, gender = ?, job_title = ?, professional_title = ?, phone_no = ?, address = ?, department = ? WHERE id = ?;";
	
	public StaffDAO() {

	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public void insertStaff(Staff staff) throws SQLException {
		
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STAFF_SQL)) {
			preparedStatement.setString(1, staff.getName());
			preparedStatement.setInt(2, staff.getAge());
			preparedStatement.setString(3, staff.getGender());
			preparedStatement.setString(4, staff.getJobtitle());
			preparedStatement.setString(5, staff.getProfessionaltitle());
			preparedStatement.setString(6, staff.getPhoneno());
			preparedStatement.setString(7, staff.getAddress());
			preparedStatement.setString(8, staff.getDepartment());
			

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Staff selectStaff(int id) {
		Staff staff = null;

		try (Connection connection = getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STAFF_BY_ID);) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				staff = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
				staff.setId(id);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return staff;
	}
	
	public List<Staff> selectAllStaff() {

		List<Staff> staff = new ArrayList<>();

		try (Connection connection = getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STAFF);) {
			ResultSet rs = preparedStatement.executeQuery();
			Staff s;
			while (rs.next()) {
				s = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
				s.setId(rs.getInt("id"));
				staff.add(s);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return staff;
	}
	
	public List<Staff> staffOrderbyRole() {

		List<Staff> staff = new ArrayList<>();

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, user, password);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * from staff order by job_title;");
			int count = 0;
			String prevRole = "";
			while (rs.next()) {
				Staff s;
				count++;
				if(count != 0) {
					if(prevRole.equals(rs.getString("job_title")) == false) {
						Staff x; // dummy value
						x = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
						x.setId(-1);
						staff.add(x);
					}
				}
				prevRole = rs.getString("job_title");
				s = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
				s.setId(rs.getInt("id"));
				staff.add(s);
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("From Staff order by Role");
		}
		return staff;
	}
	
	
	public List<Patient> selectPatientUnderADoctor(int id) {

		List<Patient> patients = new ArrayList<>();
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, user, password);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * from patients WHERE id IN  (select patient_id from checkins c1 JOIN medical_records m1 ON c1.id = m1.checkin_id AND m1.staff_id = "+  id +")");

			while (rs.next()) {
				int age = rs.getInt("age");
				String ssn = rs.getString("ssn");
				String name = rs.getString("name");
				Date dob = rs.getDate("dob");
				String gender = rs.getString("gender");
				String phoneNo = rs.getString("phone_no");
				String address = rs.getString("address");
				patients.add(new Patient(id, age, name, ssn, phoneNo, gender, dob, address));
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("From Staff DAO patients under a doctor");
		}
		return patients;
	}
	
	public List<Staff> selectAllDoctors() {

		List<Staff> staff = new ArrayList<>();

		try (Connection connection = getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DOCTORS);) {
			ResultSet rs = preparedStatement.executeQuery();
			Staff s;
			while (rs.next()) {
				s = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
				s.setId(rs.getInt("id"));
				staff.add(s);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return staff;
	}
	
	public List<Staff> selectAllOperators() {

		List<Staff> staff = new ArrayList<>();

		try (Connection connection = getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_OPERATORS);) {
			ResultSet rs = preparedStatement.executeQuery();
			Staff s;
			while (rs.next()) {
				s = new Staff(rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("job_title"), rs.getString("professional_title"), rs.getString("phone_no"), rs.getString("address"), rs.getString("department"));
				s.setId(rs.getInt("id"));
				staff.add(s);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return staff;
	}
	
	public boolean deleteStaff(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE_STAFF_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateStaff(Staff staff) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STAFF_SQL);) {
			preparedStatement.setString(1, staff.getName());
			preparedStatement.setInt(2, staff.getAge());
			preparedStatement.setString(3, staff.getGender());
			preparedStatement.setString(4, staff.getJobtitle());
			preparedStatement.setString(5, staff.getProfessionaltitle());
			preparedStatement.setString(6, staff.getPhoneno());
			preparedStatement.setString(7, staff.getAddress());
			preparedStatement.setString(8, staff.getDepartment());
			preparedStatement.setInt(9, staff.getId());
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
