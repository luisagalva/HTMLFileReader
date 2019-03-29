package me.luisa.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class Engine {
	private static List<String> words = new ArrayList<String>();
	private static List<Integer> repetitions = new ArrayList<Integer>();
	private static List<Integer> postIndex = new ArrayList<Integer>();
	private static List<Integer> docIndex = new ArrayList<Integer>();
	private static List<Double> weighted = new ArrayList<Double>();
	private static List<String> docName = new ArrayList<String>();
	
	//Actividad 13
	public static Map<String, Double> getMapResults(String toSearch, String path) throws IOException {
		readDictionary(path);
		System.out.println("Ingresa las palabras que deseas buscar");
		toSearch = toSearch.toLowerCase();
		String[] toSearchArray = toSearch.split(" ");
		Map<String, Double> results = new HashMap<String, Double>();
		int index = 0;
		int rep = 0;
		
		long start = System.nanoTime();
		for(String word : toSearchArray){
			if(words.contains(word)) {
				index = postIndex.get(words.indexOf(word));
				rep = repetitions.get(words.indexOf(word)) + postIndex.get(words.indexOf(word));
				while(index < rep ) {
					String document = docName.get(docIndex.get(index));
					index++;
					Double oldCount = results.get(document);
					if ( oldCount == null ) {
						oldCount = 0.0;
					}
					results.put(document, oldCount + weighted.get(index));
				}
			}
		}
		Map<String, Double> treeMap = sortByValue(results);
		
		
		
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de búsqueda " + convertNano(elapsedTime) + "s");
		System.out.println("end");

		return treeMap;
	
	}
	
	private static void readDictionary(String path) throws NumberFormatException, IOException{
		String thisLine = null;
		String[] columns;
		System.out.println("loading words");
		try {
			@SuppressWarnings("resource")
			
			BufferedReader br = new BufferedReader(new FileReader(path + "/Output/dictionary.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 words.add(columns[0].toLowerCase());
				 repetitions.add(Integer.parseInt(columns[1]));
				 postIndex.add(Integer.parseInt(columns[2]));
		     }
			 System.out.println("loading tokens");
			 br = new BufferedReader(new FileReader(path + "/Output/posting.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 docIndex.add(Integer.parseInt(columns[0]));
				 weighted.add(Double.parseDouble(columns[1]));
		     }
			 System.out.println("loading docs");
			 br = new BufferedReader(new FileReader(path + "/Output/documentIndex.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 docName.add(columns[1]);
		     }  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

	        // 1. Convert Map to List of Map
	        List<Map.Entry<String, Double>> list =
	                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

	        // 2. Sort list with Collections.sort(), provide a custom Comparator
	        //    Try switch the o1 o2 position for a different order
	        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	            public int compare(Map.Entry<String, Double> o1,
	                               Map.Entry<String, Double> o2) {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });

	        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
	        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	        for (Entry<String, Double> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        /*
	        //classic iterator example
	        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
	            Map.Entry<String, Integer> entry = it.next();
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }*/
	        return sortedMap;
	    }

	
	private static String convertNano(long time) {
		return String.format("%.5f",(double)time / 1_000_000_000.0);
	}


}
