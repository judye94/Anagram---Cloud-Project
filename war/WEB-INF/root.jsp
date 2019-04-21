<%@ taglib  prefix ="c" 
uri="http://java.sun.com/jsp/jstl/core"  
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title> Anagram Project - 2862968</title>
</head> 
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
body,h1,h2,h3,h4,h5,h6 {font-family: "Lato", sans-serif;}
body, html {
    height: 100%;
    color: #777;
    line-height: 1.8;
    padding-top: 50px;
    padding-right: 10px;
    padding-bottom: 50px;
    padding-left: 170px;
    background-color: #e8f0f7;
}

</style>
<body>
	<!-- We need to render one version of the page, if the user is logged out
		 therefore we need to render a different version   -->
	<c:choose>
		<c:when test="${user != null}">
			<p> Welcome ${user.email}<br/>
			Sign Out <a href="${logout_url}}">here</a><br/>
			
			<p><b>${response}</b></p>
			
			<p>
			<c:forEach var="word" items="${list}">
				<c:out value="${word}"/><br/>
			</c:forEach>
			</p>
			
			<br/>
			<form action="/AnagramServlet" method="get">
				Search for Anagram: <input type="text" name="search_input">
				<input type="submit">
			</form>
			<p>
			
			</p>
			
			<form action="/AnagramServlet" method="post">
				Create New Anagram: <input type="text" name="add_input">
				<input type="submit">
			</form>
			
			</c:when>
			<c:otherwise>
				<p> Welcome to the Anagram Application <a href="${login_url}">
				<br>Sign In or Register</a>
				</p>
			</c:otherwise>
			</c:choose>
			
		
</body>
</html>