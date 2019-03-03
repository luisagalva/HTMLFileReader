package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.io.FilenameUtils;

public class Main {
	
	private static long openingTime;
	
	public static void main(String[] args) {
		String input_directory = args[0];
		String output_directory = args[1];
		long start = System.nanoTime();
		File dir = new File(input_directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				tokenize(child, output_directory);
			}
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime));

	}
	
	//Actividad 5
	@SuppressWarnings("resource")
	private static void tokenize(File file, String output_directory){
		long start = System.nanoTime();
		String words[];
		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

			words = doc.text().split("\\s+");
			for ( String word : words ){
			   Integer oldCount = occurrences.get(word);
			   if ( oldCount == null ) {
			      oldCount = 0;
			   }
			   occurrences.put(word, oldCount + 1);
			}
			
			String FILENAME = output_directory + FilenameUtils.removeExtension(file.getName());
			BufferedWriter bw = null;
			FileWriter fw = null;

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			
			Map<String, Integer> treeMap = new TreeMap<String, Integer>(occurrences);
			for (Entry<String, Integer> entry : treeMap.entrySet()) {
			    bw.write(entry.getKey() + "   " + entry.getValue());
			    bw.newLine();
			}
			bw.close();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println(file.getName() + "   " + convertNano(elapsedTime));
	}
	
	private static double convertNano(long time) {
		return (double)time / 1_000_000_000.0;
	}

}
