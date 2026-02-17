<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%
Logger log = LogManager.getLogger(this.getClass());
Calendar now = Calendar.getInstance();
log.info("Current TimeZone is : " + now.getTimeZone().getDisplayName());

//display current TimeZone using getDisplayName() method of TimeZone class
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%="Current TimeZone is : " + now.getTimeZone().getDisplayName() %>
<br>
<%="Id: " + now.getTimeZone().getID() %>
<br>
<%="Date time: " + now.toString()%>
</body>
</html>