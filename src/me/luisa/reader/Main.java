package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
	
	public static void main(String[] args) {
		String input_directory = args[0];
		String output_directory = args[1];
		long start = System.nanoTime();
		File dir = new File(input_directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			dictionary(directoryListing, output_directory);
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime) + "s");
         
	}
	
	//Actividad 8
	private static void dictionary(File[] directoryListing, String output_directory){
		List<String> words = new ArrayList<String>();
		List<String> postWords = new ArrayList<String>();
		List<String>postFiles = new ArrayList<String>();
		List<Integer> postRepetitions = new ArrayList<Integer>();
		int collissions = 0;
		
		Hashtable<String, String> hashtable = new Hashtable<>();
		for (File child : directoryListing) {
			long start = System.nanoTime();
			for (Entry <String, Integer> entry : occurrences(child).entrySet()) {
				String key = entry.getKey();
				if(words.contains(key)){
					//dictionary
					int pivot = Integer.parseInt(hashtable.get(key))+1;
					hashtable.put(key, Integer.toString(pivot));
					//post
					int postPivot = postWords.lastIndexOf(key)+1;
					postWords.add(postPivot, key);
					postFiles.add(postPivot, child.getName());
					postRepetitions.add(postPivot, entry.getValue());
					collissions++;
				}else{
					//dictionary
					words.add(key);
					hashtable.put(key, "1");
					//post
					postWords.add(key);
					postFiles.add(child.getName());
					postRepetitions.add(entry.getValue());
				}
			}
			long elapsedTime = System.nanoTime() - start;
			System.out.println(child.getName() + "   " + convertNano(elapsedTime) + "s");
		}
		
		System.out.println(collissions);
		
		String FILENAME = output_directory + "dictionary.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			Set<String> keys = hashtable.keySet();
	        for(String key: keys){
	            bw.write(key + " " + hashtable.get(key) + " " + postWords.indexOf(key));
			    bw.newLine();
	        }
			fw = new FileWriter(output_directory + "posting.txt");
			bw = new BufferedWriter(fw);
			for (int i = 0; i < postWords.size(); i++ ) {
				bw.write( postFiles.get(i) + ";" + postRepetitions.get(i));
			    bw.newLine();
			}
			        
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static Map<String, Integer> occurrences(File child){
		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		String words[];
		try {
			Document doc = Jsoup.parse(child, "UTF-8", "http://example.com/");
			words = doc.text().split("\\s+");
			for ( String word : words ){
				Integer oldCount = occurrences.get(word);
				if ( oldCount == null ) {
					oldCount = 0;
				}
				occurrences.put(word, oldCount + 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurrences;
	}
		
	
	private static double convertNano(long time) {
		return (double)time / 1_000_000_000.0;
	}

}
