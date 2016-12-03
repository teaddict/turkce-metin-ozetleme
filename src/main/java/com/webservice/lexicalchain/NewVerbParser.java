package com.webservice.lexicalchain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import zemberek.morphology.apps.TurkishMorphParser;

public class NewVerbParser {
	NewVerbFinder findVerb = new NewVerbFinder();

	public String readTextFile(String fileName) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		StringBuilder sb = new StringBuilder();
		String line = input.readLine();
		while ((line != null)) // metin dosyası satır satır okundu.
		{
			sb.append(line.toLowerCase());
			sb.append(System.lineSeparator());
			line = input.readLine();
		}
		input.close();
		String text = sb.toString();
		return text;
	}
	
	public Set<String> sentenceListNew(String file) {
		String lines[] = file.split("\\r?\\n");
		
		Set<String> sentences = new HashSet<String>();
		try {
			this.findVerb.parser = TurkishMorphParser.createWithDefaults();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String line : lines) {
			String tempLineWords[] = line.split(":");
			String tempWord = tempLineWords[0];
			String root = this.findVerb.parse(tempWord.toString());
			if (root == "notVerb") {
				sentences.add(line);
			}
		
		}
		return sentences;
	}
	
	public void writeNewList(Set<String> sentences,String filename) throws IOException{
		StringBuilder sb = new StringBuilder();
		
		for(String sentence: sentences){
			sb.append(sentence.toLowerCase());
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
		NewVerbParser verbParser = new NewVerbParser();
		String textFilename = "src/main/resources/datasets/lexical/relation_collection.csv";
		String outputFile = "src/main/resources/datasets/lexical/relations_no_verbs.csv";
		String file = verbParser.readTextFile(textFilename);
		Set<String> sentences = verbParser.sentenceListNew(file);
		verbParser.writeNewList(sentences, outputFile);
	}
	
}
