package com.webservice.lexicalchain;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import com.webservice.controller.WebController;
import com.webservice.model.Summary;


public class NewPreprocess {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );
	public NewPreprocess(TurkishParser tp) throws IOException {
		super();
		this.findNoun = new NewNounFinder(tp);
		this.findVerb = new NewVerbFinder();
		this.setSentenceOperation(new NewSentence());
		this.setParagraphs(new NewParagraph());
		this.lexicalChain = new NewLexicalChain();
		this.chainScores = new NewChainScores();
		this.cleanedParagraphsOfText = new ArrayList<String>();
		this.cleanedSentencesOfText = new ArrayList<String>();
		this.originalParagraphsOfText = new ArrayList<String>();
		this.originalSentencesOfText = new ArrayList<String>();
		this.extractSentence = new NewExtractSentences();
		this.titleOfText = "";
		this.classifier = new NewClassifier();
		this.systemPerformance = new NewSystemPerformance();
	}
	
	NewNounFinder findNoun;
	NewVerbFinder findVerb;
	private NewSentence sentenceOperation;
	private NewParagraph paragraphs;
	NewLexicalChain lexicalChain;
	NewChainScores chainScores;
	List<String> cleanedParagraphsOfText;
	List<String> cleanedSentencesOfText;
	List<String> originalParagraphsOfText;
	List<String> originalSentencesOfText;
	String titleOfText;
	String classOfText;
	NewExtractSentences extractSentence;
	NewClassifier classifier;
	NewSystemPerformance systemPerformance;
	
	public void cleaner(){
		this.setSentenceOperation(new NewSentence());
		this.setParagraphs(new NewParagraph());
		this.lexicalChain = new NewLexicalChain();
		this.chainScores = new NewChainScores();
		this.cleanedParagraphsOfText = new ArrayList<String>();
		this.cleanedSentencesOfText = new ArrayList<String>();
		this.originalParagraphsOfText = new ArrayList<String>();
		this.originalSentencesOfText = new ArrayList<String>();
		this.extractSentence = new NewExtractSentences();
		this.titleOfText = "";
		this.classifier = new NewClassifier();
	}
	
	public List<String> getAllNouns(String textFile) throws IOException {
		List<String> nouns = new ArrayList<String>();
		// noktalamaları kaldır
		// "’" işareti türkçede
		String strippedInput = textFile.replaceAll("\\p{Punct}+", "").replaceAll("’", "");
		strippedInput = strippedInput.replaceAll("\\d", "");
		nouns = this.findNoun.getNounRoots(strippedInput);
		return nouns;
	}

	public List<String> getAllVerbs(String textFile) throws IOException {
		List<String> verbs = new ArrayList<String>();
		// noktalamaları kaldır
		// "’" işareti türkçede
		String strippedInput = textFile.replaceAll("\\p{Punct}+", "").replaceAll("’", "");
		strippedInput = strippedInput.replaceAll("\\d", "");
		verbs = this.findVerb.getVerbs(strippedInput);
		return verbs;
	}

	// burda stopwords listesindeki tüm kelimeler çıkartılacak text içinden
	public String cleanStopWords(String textFile) throws IOException {
		String stopWordsFilename = "datasets/stopwordsTurkish.txt";
		String getStopWords = getFile(stopWordsFilename);
		String stopWordsText = readTextFile(getStopWords);
		stopWordsText = stopWordsText.toLowerCase();
		String stopWords[] = stopWordsText.split("\\r?\\n");
		List<String> stopWordSet = new ArrayList<String>(Arrays.asList(stopWords));
		textFile = textFile.toLowerCase();
		StringBuilder result = new StringBuilder(textFile.length());
		for (String s : textFile.split(" ")) {
			if (!stopWordSet.contains(s))
				result.append(s + " ");
		}

		return result.toString();
	}

	private String getFile(String fileName) {

		StringBuilder result = new StringBuilder("");

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}
	public String readTextFile(String fileName) throws IOException {
		String[] split = fileName.split("\\.");
		String ext = split[split.length - 1];
		if (ext.equals("txt")) {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = input.readLine();
			while ((line != null)) // metin dosyası satır satır okundu.
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = input.readLine();
			}
			input.close();
			String text = sb.toString();
			if (text.length() < 10) {
				return "Özetlenemeyecek kadar kısa bir metin..";
			}

			return text;
		} else {
			return "Wrong extension of text file..";
		}
	}
	
	public String readText(String text) throws IOException {
		StringBuilder sb = new StringBuilder();
		String lines[] = text.split("\\r?\\n");
		for(String line : lines) // metin dosyası satır satır okundu.
		{
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		String textBuilded = sb.toString();
		if (text.length() < 10) {
			return "Özetlenemeyecek kadar kısa bir metin..";
		}
		return textBuilded;
	}

	public List<NewLexical> getAllLexicals(String cleanedText,String originalText) throws IOException {
		int paragraphNo = 0;
		int sentenceNo = 0;
		List<NewLexical> lexicals = new ArrayList<NewLexical>();
		List<String> tempParagraphsOfText = this.getParagraphs().getParagraphs(cleanedText);
		List<String> tempOriginalParagraphsOfText = this.getParagraphs().getParagraphs(originalText);
		for (int i = 0; i < tempParagraphsOfText.size(); i++) { // tüm paragrafları yaz.
			List<String> sentences = this.getSentenceOperation().getSentences(tempParagraphsOfText.get(i));
			List<String> originalSentences = this.getSentenceOperation().getSentences(tempOriginalParagraphsOfText.get(i));
			if (i == 0 && sentences.size() == 1) {
				this.titleOfText = sentences.get(i);
			} else {
				for (int j=0; j<sentences.size()-1; j++) { // paragrafın içindeki tüm
													// cümleleri yaz.
					List<String> nounsOfText = getAllNouns(sentences.get(j));
					for (String noun : nounsOfText) {
						NewLexical lexical = new NewLexical(noun, sentenceNo, paragraphNo);
						lexicals.add(lexical);
					}
					sentenceNo = sentenceNo + 1;
					this.cleanedSentencesOfText.add(sentences.get(j));
					this.originalSentencesOfText.add(originalSentences.get(j));
				}
				paragraphNo = paragraphNo + 1;
				this.cleanedParagraphsOfText.add(tempParagraphsOfText.get(i));
				this.originalParagraphsOfText.add(tempOriginalParagraphsOfText.get(i));
			}
		}
		return lexicals;
	}

	public String createChains(List<NewLexical> lexicals) throws IOException {
		List<NewChain> chains = lexicalChain.chainBuilder(lexicals);
		LOGGER.info("#############All Chains: " + chains.size());
		List<NewChain> uniqueChains = lexicalChain.chainAnalyse(chains);
		LOGGER.info("#############Unique Chains: " + uniqueChains.size());
		uniqueChains = chainScores.calculateChainScores(uniqueChains);
		uniqueChains = chainScores.calculateChainStrengths(uniqueChains);
		String uniqueFilename = "datasets/test/uniqueChains.csv";
		lexicalChain.writeChainsToTextfile(uniqueFilename, uniqueChains);
		List<NewChain> strongChains = chainScores.getStrongChains(uniqueChains);
		LOGGER.info("#############Strong Chains: " + strongChains.size());
//		for(NewChain c : strongChains){
//			LOGGER.info(c.getChainInformation());
//		}
		String strongFilename = "datasets/test/strongChains.csv";
		lexicalChain.writeChainsToTextfile(strongFilename, strongChains);
		//basarisiz algoritma
//		List<String> extractedSentences1 = this.extractSentence.heuristic1(strongChains, this.originalParagraphsOfText,
//				this.originalSentencesOfText);
//		System.out.println("#############Heuristic1 OUTPUT############");
//		for (String sentence : extractedSentences1) {
//			System.out.println(sentence);
//		}
		List<String> extractedSentences2 = this.extractSentence.heuristic2(strongChains, this.originalParagraphsOfText, this.originalSentencesOfText);
		LOGGER.info("#############Heuristic2 OUTPUT: " + extractedSentences2);

		//basarisiz algoritma
//		List<String> extractedSentences3 = this.extractSentence.heuristic3(strongChains, this.originalParagraphsOfText, this.originalSentencesOfText);
//		System.out.println("#############Heuristic3 OUTPUT############");
//		for (String sentence : extractedSentences3) {
//			System.out.println(sentence);
//		}

		return extractedSentences2.toString();
	}

	public String getClassOfText(String textFile) {
		NewClassifier classifier;
		String dataModel = "datasets/1371haber/raw_texts/myClassifier.dat";
		classifier = new NewClassifier();
		classifier.loadFile(textFile);
		classifier.loadModel(dataModel);
		classifier.makeInstance();
		return classifier.getClassOfText();
	}
	
	public String getVerbs(String contextOfText) throws IOException {

		String readText = readText(contextOfText);
		LOGGER.info("######Read Text File:######" + readText);

		String cleanText = cleanStopWords(readText);
		LOGGER.info("######Clean text file:######" + cleanText);

		String verbsOfText = getAllVerbs(cleanText).toString();
		LOGGER.info("######Verbs of text: " + verbsOfText);

		return verbsOfText;
	}
	
	public String getNouns(String contextOfText) throws IOException {
		String readText = readText(contextOfText);
		LOGGER.info("######Read Text File:######" + readText);

		String cleanText = cleanStopWords(readText);
		LOGGER.info("######Clean text file:######" + cleanText);

		String nounsOfText = getAllNouns(cleanText).toString();
		LOGGER.info("######Nouns of text: " + nounsOfText);

		return nounsOfText;
	}

	public String getClass(String contextOfText) throws IOException {
		String readText = readText(contextOfText);
		LOGGER.info("######Read Text File:######" + readText);

		String classOfText = classifier.getClassOfText(readText);
		LOGGER.info("####################CLASS OF ORIGINAL TEXT: " + classOfText);

		return classOfText;
	}

	

	public static void main(String[] args) throws IOException {

		String textFilename = "src/main/resources/datasets/2.txt";
		if(args.length>0){
			textFilename = args[0];
		}
		TurkishParser parser = new TurkishParser();
		NewPreprocess preprocess = new NewPreprocess(parser);
		String readText = preprocess.readTextFile(textFilename);
		LOGGER.info("######Read Text File:######" + readText);

		String cleanText = preprocess.cleanStopWords(readText);
		LOGGER.info("######Clean text file:######" + cleanText);

		List<String> paragraphsOfText = preprocess.getParagraphs().getParagraphs(cleanText);
		LOGGER.info("######Paragrapfs text file: " + paragraphsOfText.size());

		List<String> sentences = preprocess.getSentenceOperation().getSentences(cleanText);
		LOGGER.info("######Sentences text file: " + sentences.size());


		String title = preprocess.getSentenceOperation().getTitleSentence(sentences);
		LOGGER.info("######Title######: " + title);

		String nounsOfText = preprocess.getAllNouns(cleanText).toString();
		LOGGER.info("######Nouns of text: " + nounsOfText);

		List<NewLexical> lexicals = preprocess.getAllLexicals(cleanText,readText);
		String summary = preprocess.createChains(lexicals);
		if(summary.isEmpty()){
			summary = "Girmiş olduğunuz metin özetlenemeyecek kadar kısa!";
		}
		LOGGER.info("######Summary: " + summary);

		String classOfText = preprocess.classifier.getClassOfText(readText);
		LOGGER.info("####################CLASS OF ORIGINAL TEXT: " + classOfText);

		String classOfSummary = preprocess.classifier.getClassOfText(summary);
		LOGGER.info("###################CLASS OF SUMMARY: " + classOfSummary);
		
		System.out.println(preprocess.getNouns(cleanText));
		
	}
	
	
	public Summary getSummary(Summary summary) throws IOException {

		String readText = readText(summary.getContextOfText());
		LOGGER.info("######Read Text File:######" + readText);

		String cleanText = cleanStopWords(summary.getContextOfText());
		LOGGER.info("######Clean text file:######" + cleanText);

		List<String> paragraphsOfText = getParagraphs().getParagraphs(cleanText);
		LOGGER.info("######Paragrapfs text file: " + paragraphsOfText.size());

		List<String> sentences = getSentenceOperation().getSentences(cleanText);
		LOGGER.info("######Sentences text file: " + sentences.size());

		String title = getSentenceOperation().getTitleSentence(sentences);
		LOGGER.info("######Title######: " + title);

		String nounsOfText = getAllNouns(cleanText).toString();
		LOGGER.info("######Nouns of text: " + nounsOfText);
		
		List<NewLexical> lexicals = getAllLexicals(cleanText,readText);
		String summaryOfText = createChains(lexicals);
		summary.setSummaryOfText(summaryOfText);
		String classOfText = getClassOfText(readText);
		LOGGER.info("####################CLASS OF ORIGINAL TEXT: " + classOfText);
		summary.setClassOfText(classOfText);
		String classOfSummary = getClassOfText(summaryOfText);
		summary.setClassOfSummary(classOfSummary);
		LOGGER.info("###################CLASS OF SUMMARY: " + summaryOfText);

//		System.out.println("####################CLASS OF ORIGINAL TEXT################");
//		System.out.println(preprocess.classifier.getClassOfText(readText));
//		System.out.println("###################CLASS OF SUMMARY#######################");
//		System.out.println(preprocess.classifier.getClassOfText(summaryOfText));
		
		return summary;
		
	}

	public NewParagraph getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(NewParagraph paragraphs) {
		this.paragraphs = paragraphs;
	}

	public NewSentence getSentenceOperation() {
		return sentenceOperation;
	}

	public void setSentenceOperation(NewSentence sentenceOperation) {
		this.sentenceOperation = sentenceOperation;
	}

}
