package com.webservice.lexicalchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.io.Resources;

public class NewSemanticListParser {

	public String readTextFile(String fileName) throws IOException {
		String text ="";
	    try (BufferedReader input = Files.newBufferedReader(Paths.get(Resources.getResource(fileName).toURI()))) {
		StringBuilder sb = new StringBuilder();
		String line = input.readLine();
		while ((line != null)) // metin dosyası satır satır okundu.
		{
			sb.append(line.toLowerCase());
			sb.append(System.lineSeparator());
			line = input.readLine();
		}
		input.close();
		text = sb.toString();
		
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return text;
	}

	public void writeNewList(String text, String filename) throws IOException {
		Set<String> uniqueSentences = new HashSet<String>();
		String sentences[] = text.split("\\r?\\n");

		StringBuilder sb = new StringBuilder();

		for (String sentence : sentences) {
			if (!uniqueSentences.contains(sentence.toLowerCase())) {
				uniqueSentences.add(sentence.toLowerCase());
				sb.append(sentence.toLowerCase());
				sb.append(System.lineSeparator());
			}
		}

		String resultText = sb.toString();
		try (BufferedWriter bufferedWriter = Files
				.newBufferedWriter(Paths.get(Resources.getResource(filename).toURI()))) {
			System.out.println(Paths.get(Resources.getResource(filename).toURI()));
			bufferedWriter.write(resultText);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public enum Relations {
		SYNONYMY(0), ANTONYM(1), HYPONYMY(2), HYPERNYMY(3), WORD_ASSOC(4), SIMILAR_TO(5), YAN_KAVRAM(6), SCIENTIFIC(7);
		private int value;

		private Relations(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

	}

	public Set<String> wordListNew(String file) {
		Set<String> words = new HashSet<String>();
		String lines[] = file.split("\\r?\\n");
		for (int i = 0; i < lines.length; i++) {
			String[] tokens = lines[i].split(":");
			words.add(tokens[0]);
		}
		return words;
	}

	public HashMap<String, Set<String>> sentenceListNew(Set<String> words, String file) {
		String lines[] = file.split("\\r?\\n");
		HashMap<String, Set<String>> sentenceMap = new HashMap<String, Set<String>>();

		Set<String> sentences = new HashSet<String>();
		String tempLineWords[] = lines[0].split(":");
		String tempWord = tempLineWords[0];
		sentences.add(lines[0]);

		for (int i = 1; i < lines.length; i++) {
			tempLineWords = lines[i].split(":");
			if (tempLineWords[0].equals(tempWord)) {
				sentences.add(lines[i]);
			} else {
				sentenceMap.put(tempWord, sentences);
				tempWord = tempLineWords[0];
				sentences = new HashSet<String>();
				sentences.add(lines[i]);
			}

		}
		return sentenceMap;
	}

	public static void main(String[] args) throws IOException {
		// time
		long startTime = System.nanoTime();

		// NewSemanticListParser wordnetparser = new NewSemanticListParser();
		// String textFilename =
		// "src/test/res/datasets/lexical/all_relations.csv";
		// String text = wordnetparser.readTextFile(textFilename);
		// Set<String> words = wordnetparser.wordListNew(text);
		// System.out.println(words.size());
		// Set<String> result = wordnetparser.sentenceListNew(words,
		// text).get("haber");
		// if (result != null) {
		// System.out.println(result);
		// } else {
		// System.out.println("listede böyle bir kelime yok!");
		// }
		NewSemanticListParser wordnetparser = new NewSemanticListParser();
		String readTextFilename = "src/main/resources/datasets/lexical/all_relations_test.csv";
		String writeTextFilename = "src/main/resources/datasets/lexical/all_relations_new.csv";
		String text = wordnetparser.readTextFile(readTextFilename);
		wordnetparser.writeNewList(text, writeTextFilename);
		
		// time
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; // divide by 1000000 to
															// get milliseconds.
		System.out.println("Execute time(miliseconds): " + duration);

	}

}