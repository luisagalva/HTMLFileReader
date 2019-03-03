package me.luisa.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.io.FilenameUtils;

public class Main {
	
	private static long openingTime;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		File dir = new File("/Users/luisa/Desktop/Files");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		  consolidatedFile(directoryListing);
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime));

	}
	
	//Actividad 4
	@SuppressWarnings("resource")
	private static void consolidatedFile(File[] directoryListing){
		long start = System.nanoTime();   
		 ArrayList<String> consolidated = new ArrayList<String>();
		
		for (File child : directoryListing) {
			String[] singleHtml = getArray(child);
			for (String word : singleHtml) {
				consolidated.add(word.toLowerCase());
			}
		}
		 
		try {
			String FILENAME = "/Users/luisa/Desktop/ConsolidatedFile.txt";
			BufferedWriter bw = null;
			FileWriter fw = null;

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			
			Collections.sort(consolidated);
			for (int i = 0; i < consolidated.size(); i++) {
				bw.write(consolidated.get(i));
				bw.newLine();
			}
            bw.close();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long elapsedTime = System.nanoTime() - start;
	    openingTime += elapsedTime;
	    
	    System.out.println("Tiempo total en crear archivo consolidado " + convertNano(elapsedTime));
	}
	
	private static String[] getArray(File file){
		long start = System.nanoTime();
		String array[];
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
			array = doc.text().split("\\s+");
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			array = new String[] {};
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println(file.getName() + "   " + convertNano(elapsedTime));
		return array;
	}

	
	private static double convertNano(long time) {
		return (double)time / 1_000_000_000.0;
	}

}
