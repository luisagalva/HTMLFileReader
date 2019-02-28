package me.luisa.reader;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
	
	private static long openingTime;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		File dir = new File("/Users/luisa/Desktop/Files");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		  for (File child : directoryListing) {
			  open(child);
		   }
		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Tiempo total de abrir los archivos " + convertNano(openingTime));
		System.out.println("Tiempo total de ejecución " + convertNano(elapsedTime));

	}
	
	private static void open(File file){
		long start = System.nanoTime();    
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long elapsedTime = System.nanoTime() - start;
	    openingTime += elapsedTime;
		System.out.println(file.getName() + "   " + convertNano(elapsedTime));
	}
	
	private static double convertNano(long time) {
		return (double)time / 1_000_000_000.0;
	}

}
