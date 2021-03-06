<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Patient Management</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/foundation-sites@6.5.3/dist/css/foundation.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/foundation-sites@6.5.3/dist/js/foundation.min.js"></script>
</head>
<body>
	<center>
		<h2>Medical History</h2>
	</center>
	<div><form action="homepage.jsp" method="get" enctype="multipart/form-data">
			<input type="submit" class="button" value="HOME">
		</form>	</div>
	<div align="center">
		<table border="1" cellpadding="5">			
			<tr>				
				<th>Age</th>
				<th>Name</th>				
				<th>DOB</th>
				<th>Gender</th>				
				<th>Diagnosis</th>
				<th>Staff Name</th>          	
            	<th>Start Date</th>
            	<th>End Date</th>
			</tr>
			<c:forEach var="h" items="${history}">
				<tr>					
					<td><c:out value="${h.patient.age}" /></td>
					<td><c:out value="${h.patient.name}" /></td>					
					<td><c:out value="${h.patient.dob}" /></td>
					<td><c:out value="${h.patient.gender}" /></td>									
					<td><c:out value="${h.record.diagnosis}" /></td>
					<td><c:out value="${h.staff.name}" /></td>													
	                <td><c:out value="${h.checkin.startdate}" /></td>
	                <td><c:out value="${h.checkin.enddate}" /></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>