package com.webservice.lexicalchain;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NewExtractKeywords {
	TurkishParser parser;

	public NewExtractKeywords() throws IOException {
		super();
		this.parser = new TurkishParser();
		this.preprocess = new NewPreprocess(this.parser);
		
	}

	NewPreprocess preprocess;

	public String readAllFiles(String path) throws IOException {
		File f = new File(path);
		File[] files = f.listFiles();
		String text = "";
		for (File file : files) {
			text += this.preprocess.readTextFile(file.getAbsolutePath());
		}
		return text;
	}

	public void writeNewList(List<String> nouns, String filename) throws IOException {
		StringBuilder sb = new StringBuilder();

		for (String noun : nouns) {
			sb.append(noun.toLowerCase());
			sb.append(System.lineSeparator());
		}

		String text = sb.toString();
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(filename));
		writer.write(text);
		writer.flush();
		writer.close();

	}

	public static void main(String[] args) throws IOException {
		NewExtractKeywords extractKeywords = new NewExtractKeywords();
		String path = "src/main/resources/datasets/haberler/dunya";
		String filename = "src/main/resources/datasets/haberler/liste.txt";
		String text = extractKeywords.readAllFiles(path);
		String cleanedText = extractKeywords.preprocess.cleanStopWords(text);
		List<String> nounsOfText = extractKeywords.preprocess.getAllNouns(cleanedText);
		extractKeywords.writeNewList(nounsOfText, filename);

	}
}
