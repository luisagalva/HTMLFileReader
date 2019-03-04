package me.luisa.reader;

public class Token {
	
	private static String name;
	private static int repetitions;
	private static int files;
	
	public Token(){
		Token.name = "";
		repetitions = 0;
		files = 0;
	}
	
	public Token(String name, int repetitions){
		Token.name = name;
		Token.repetitions = repetitions;
		files = 1;
	}
	
	public void addRepetitions(int repetitions){
		Token.repetitions += repetitions;
		Token.files++;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Token.name = name;
	}

	public static int getRepetitions() {
		return repetitions;
	}

	public static void setRepetitions(int repetitions) {
		Token.repetitions = repetitions;
	}

	public static int getFiles() {
		return files;
	}

	public static void setFiles(int files) {
		Token.files = files;
	}

	@Override
	public String toString() {
		return Token.name + ";" + Token.repetitions + ";" + Token.files;
	}
	
}
