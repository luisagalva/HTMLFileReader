package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
	
	//Actividad 12
	public static void main(String[] args) throws IOException {
		String thisLine = null;
		Scanner scan = new Scanner(System.in);
		String[] columns;
		List<String> words = new ArrayList<String>();
		List<Integer> repetitions = new ArrayList<Integer>();
		List<Integer> postIndex = new ArrayList<Integer>();
		List<Integer> docIndex = new ArrayList<Integer>();
		List<Double> weighted = new ArrayList<Double>();
		List<String> docName = new ArrayList<String>();
		System.out.println("loading words");
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("Output/dictionary.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 words.add(columns[0].toLowerCase());
				 repetitions.add(Integer.parseInt(columns[1]));
				 postIndex.add(Integer.parseInt(columns[2]));
		     }
			 System.out.println("loading tokens");
			 br = new BufferedReader(new FileReader("Output/posting.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 docIndex.add(Integer.parseInt(columns[0]));
				 weighted.add(Double.parseDouble(columns[1]));
		     }
			 System.out.println("loading docs");
			 br = new BufferedReader(new FileReader("Output/documentIndex.txt"));
			 while ((thisLine = br.readLine()) != null) {
				 columns = thisLine.split("  ");
				 docName.add(columns[1]);
		     }  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ingresa la palabra que deseas buscar");
		String toSearch = scan.nextLine().toLowerCase();
		long start = System.nanoTime();
		if(words.contains(toSearch)) {
			int index = postIndex.get(words.indexOf(toSearch));
			int rep = repetitions.get(words.indexOf(toSearch)) + postIndex.get(words.indexOf(toSearch));
			while(index < rep ) {
				System.out.println(docName.get(docIndex.get(index)) );
				index++;
			}
		}else {
			System.out.println("no fue encontrada");
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de búsqueda " + convertNano(elapsedTime) + "s");
		System.out.println("end");
		scan.close();
	}
	
	public static void generateFiles(String input_directory, String output_directory ){
		long start = System.nanoTime();
		File dir = new File(input_directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			dictionary(directoryListing, output_directory);
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime) + "s");
	}
	
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
		return String.format("%.5f",(double)time / 1_000_000_000.0);
	}

}
