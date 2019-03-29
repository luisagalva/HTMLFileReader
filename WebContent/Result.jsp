<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 
<%@page import="java.util.HashMap"%> 
<%@page import="java.util.Map"%> 
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<title>Búsqueda</title>
</head>
<body>

<main role="main" class="container">
  <div class="jumbotron">
    <h1>Motor de búsqueda</h1>
    <form class="form-inline mt-2 mt-md-0" action="search" method="POST" >
      <input class="form-control mr-sm-2" type="text" placeholder="Buscar" name="keyWord" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Buscar</button>
    </form>
  </div>
</main>

<table class="table"> 
        <%-- Fetching the attributes of the request object 
             which was previously set by the servlet
        --%>  
	<%Map<String, String> std =  (HashMap<String, String>)request.getAttribute("data");
	  List<String> files = (ArrayList<String>)request.getAttribute("files");
	  String absPath = (String)request.getAttribute("path");
      for(String file : files){%> 
        <%-- Arranging data in tabular form 
        --%> 
        <tr> 
            <td ><a href="Files/<%= file %>"><%= file %></a></td>
            <td ><%= std.get(file) %></td>   
        </tr> 
     <%}%> 
</table>  

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>