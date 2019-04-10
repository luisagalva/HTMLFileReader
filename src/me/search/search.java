package me.search;

import me.luisa.reader.Engine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class search
 */
@WebServlet("/search")
public class search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// List to hold Student objects 

		String keyWord = request.getParameter("keyWord");
		ServletContext context = request.getServletContext();
		String path = context.getRealPath("/");
		
		Map<String, Double> treeMap = Engine.getMapResults(keyWord, path);
		Map<String, String> result = new HashMap<String,String>();
		List<String> files = new ArrayList<String>();
		if(treeMap.entrySet().size() <= 10) {
			for (Entry<String, Double> entry : treeMap.entrySet()) {
				System.out.println(entry.getKey() + " " + String.format("%.2f",entry.getValue()));
				result.put(entry.getKey(), String.format("%.2f",entry.getValue()));
				files.add(entry.getKey());
			}
		}else {
			List<String> keys = treeMap.entrySet().stream()
					  .map(Map.Entry::getKey)
					  .limit(10)
					  .collect(Collectors.toList());
			for (String entry : keys) {
				System.out.println(entry+ " " + String.format("%.2f",treeMap.get(entry)));
				result.put(entry, String.format("%.2f",treeMap.get(entry)));
				files.add(entry);
			}
		}
		if(treeMap.entrySet().size() == 0) {
			result.put("No hay resultados", "0.00");
		}
		
	    request.setAttribute("data", result); 
	    request.setAttribute("files", files); 
	    request.setAttribute("path", path); 
		RequestDispatcher rd = request.getRequestDispatcher("Result.jsp");
	    rd.forward(request, response);
	}

}
