package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
	
	public static void main(String[] args) {
		String input_directory = "/Users/luisa/Desktop/Files/";
		String output_directory = "/Users/luisa/Desktop/Output/";
		long start = System.nanoTime();
		File dir = new File(input_directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			consolidatedFile(directoryListing, output_directory);
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime) + "s");
	}
	
	//Actividad 6
	private static void consolidatedFile(File[] directoryListing, String output_directory){
		List<String> words = new ArrayList<String>();
		List<Integer> repetitions = new ArrayList<Integer>();
		List<Integer> files = new ArrayList<Integer>();
		for (File child : directoryListing) {
			for (Entry <String, Integer> entry : occurrences(child).entrySet()) {
				if(words.contains(entry.getKey())){
					int pivot = words.indexOf(entry.getKey());
					repetitions.set(pivot, repetitions.get(pivot) + entry.getValue());
					files.set(pivot, files.get(pivot) + 1);
				}else{
					words.add(entry.getKey());
					repetitions.add(entry.getValue());
					files.add(1);
				}
			}
		}
		String FILENAME = output_directory + "tokenized.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < repetitions.size(); i++ ) {
				bw.write(words.get(i) + ";" + repetitions.get(i) + ";" + files.get(i));
			    bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Map<String, Integer> occurrences(File child){
		long start = System.nanoTime();
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
		long elapsedTime = System.nanoTime() - start;
		System.out.println(child.getName() + "   " + convertNano(elapsedTime) + "s");
		return occurrences;
	}
		
	
	private static double convertNano(long time) {
		return (double)time / 1_000_000_000.0;
	}

}
