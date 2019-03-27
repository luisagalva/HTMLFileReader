package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
	
	//Actividad 11
	private static void dictionary(File[] directoryListing, String output_directory){
		List<String> words = new ArrayList<String>();
		List<String> postWords = new ArrayList<String>();
		List<Integer> postFiles = new ArrayList<Integer>();
		List<Double> postRepetitions = new ArrayList<Double>();
		int collissions = 0;
		
		Hashtable<String, Integer> hashtable = new Hashtable<>();
		for(int i=0; i<directoryListing.length; i++) {
			File child = directoryListing[i];
			long start = System.nanoTime();
			Map<String, Integer> occurrences1 = occurrences(child);
			int occurrencesSize = occurrences1.entrySet().size();
			for (Entry <String, Integer> entry : occurrences1.entrySet()) {
				String key = entry.getKey();
				if(words.contains(key)){
					//dictionary
					int pivot = hashtable.get(key)+1;
					hashtable.put(key, pivot);
					//post
					int postPivot = postWords.lastIndexOf(key)+1;
					postWords.add(postPivot, key);
					postFiles.add(postPivot, i);
					postRepetitions.add(postPivot, (Double.valueOf(entry.getValue()) * 100) / occurrencesSize);
					collissions++;
				}else{
					//dictionary
					words.add(key);
					hashtable.put(key, 1);
					//post
					postWords.add(key);
					postFiles.add(i);
					postRepetitions.add( (Double.valueOf(entry.getValue()) * 100 ) / occurrencesSize);
				}
			}
			long elapsedTime = System.nanoTime() - start;
			System.out.println(child.getName() + "   " + convertNano(elapsedTime) + "s");
		}
		System.out.println(collissions + " colisiones");
		Set<String> keys = hashtable.keySet();
		for(String key: keys){
            if(hashtable.get(key) == 1){
            	int pivot = postWords.indexOf(key);
            	postWords.remove(pivot);
            	postFiles.remove(pivot);
            	postRepetitions.remove(pivot);
            }
        }
		hashtable.values().removeIf(value -> value < 2);

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {		    
			fw = new FileWriter(output_directory + "posting.txt");
			bw = new BufferedWriter(fw);
			for (int i = 0; i < postWords.size(); i++ ) {
				bw.write(postFiles.get(i) + "  " + String.format("%.2f", postRepetitions.get(i)));
			    bw.newLine();
			}
			bw.close();
			
			fw = new FileWriter(output_directory + "dictionary.txt");
			bw = new BufferedWriter(fw);
	        for(String key: keys){
	            bw.write(key + "  " + hashtable.get(key) + "  " + postWords.indexOf(key));
			    bw.newLine();
	        }
			bw.close();
			
			fw = new FileWriter(output_directory + "documentIndex.txt");
			bw = new BufferedWriter(fw);
			int i = 0;
	        for(File child : directoryListing){
	            bw.write( i + "  " + child.getName());
			    bw.newLine();
			    i++;
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
				if(!StopList.list.contains(word) && word.length() > 1) {
					Integer oldCount = occurrences.get(word);
					if ( oldCount == null ) {
						oldCount = 0;
					}
					occurrences.put(word, oldCount + 1);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return occurrences;
	}
		
	
	private static String convertNano(long time) {
		return String.format("%.2f",(double)time / 1_000_000_000.0);
	}

}
