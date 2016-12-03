package com.webservice.lexicalchain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.webservice.controller.WebController;

public class NewExtractSentences {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	/*
	 * 
	 * We investigated three alternatives for this step: For each chain in the
	 * summary representation choose the sentence that contains the first
	 * appearance of a chain member in the text.
	 * 
	 * her zincirdeki ilk kelimenin geçtiği cümleyi al
	 */
	public List<String> heuristic1(List<NewChain> chains, List<String> paragraphs, List<String> sentences) {
		List<String> extractedSentences = new ArrayList<String>();
		List<Integer> sentencesIndexNo = new ArrayList<Integer>();
		for (NewChain chain : chains) {
			int sentenceNo = chain.getLexicals().get(0).getSentenceNo();
			if (!sentencesIndexNo.contains(sentenceNo)) {
				sentencesIndexNo.add(sentenceNo);
			}
			else{
				for(int i=0; i<chain.getLexicals().size(); i++)
				{
					sentenceNo = chain.getLexicals().get(i).getSentenceNo();
					if (!sentencesIndexNo.contains(sentenceNo)) {
						sentencesIndexNo.add(sentenceNo);
						break;
					}
				}
			}
		}
		// cümleleri yerlerine göre sıralayalım
		Collections.sort(sentencesIndexNo);
		for (int i = 0; i < sentencesIndexNo.size(); i++) {
			extractedSentences.add(sentences.get(sentencesIndexNo.get(i)));


		}

		return extractedSentences;
	}

	/*
	 * We therefore defined a criterion to evaluate the appropriateness of a
	 * chain member to represent its chain based on its frequency of occurrence
	 * in the chain. We found experimentally that such words, call them
	 * representative words, have a frequency in the chain noless than the
	 * average word frequency in the chain.
	 * 
	 * her zincirde kelimelerin frekansını belirle ortalama üstündeki kelimeleri
	 * al -> temsilci kelimeler bu kelimeleri içeren ilk cümleyi al
	 * 
	 */
	public List<String> heuristic2(List<NewChain> chains, List<String> paragraphs, List<String> sentences) {
		Map<String, Integer> uniqueLexicals = new HashMap<String, Integer>();
		List<String> representativeWords = new ArrayList<String>();
		List<String> extractedSentences = new ArrayList<String>();
		for (NewChain chain : chains) {
			int sumOfOccurences = 0;
			double averageOfOccurences = 0.0;
			List<NewLexical> lexicals = chain.getLexicals();
			List<String> words = chain.getWordsOfChain();
			List<String> uniqueWords = new ArrayList<String>();
			for (NewLexical lexical : lexicals) {
				if (!uniqueLexicals.containsKey(lexical.getWord())) {
					int occurrences = Collections.frequency(words, lexical.getWord());
					uniqueLexicals.put(lexical.getWord(), occurrences);
					uniqueWords.add(lexical.getWord());
				}
			}

			// zincirde farklı kelimeler varsa
			if (uniqueWords.size() > 1) {

				uniqueLexicals = this.sortByComparator(uniqueLexicals);
				for (Map.Entry<String, Integer> entry : uniqueLexicals.entrySet()) {
					sumOfOccurences += entry.getValue().intValue();
				}
				averageOfOccurences = (double) sumOfOccurences / (double) uniqueLexicals.size();
				for (Map.Entry<String, Integer> entry : uniqueLexicals.entrySet()) {
					double occurence = (double) entry.getValue().intValue();
					if (occurence >= averageOfOccurences) {
						if (!representativeWords.contains(entry.getKey())) {
							representativeWords.add(entry.getKey());
						}
					}
				}
				int maxOccurence = 0;
				int currentOccurence = 0;
				String selectedSentence = "";
				String secondSelectedSentence = "";
				for (String sentence : sentences) {
					String lowerSentence = sentence.toLowerCase();
					if (lowerSentence.contains(representativeWords.get(0))) {
						for (int i = 1; i < representativeWords.size(); i++) {
							if (lowerSentence.contains(representativeWords.get(i))) {
								currentOccurence += 1;
							}
						}
					}
					if (currentOccurence > maxOccurence) {
						maxOccurence = currentOccurence;
						secondSelectedSentence = selectedSentence;
						selectedSentence = sentence;
					}
					currentOccurence = 0;
				}
				if (!selectedSentence.equals("")) {
					if (!extractedSentences.contains(selectedSentence)) {
						extractedSentences.add(selectedSentence);
					}
					else{
						if (!secondSelectedSentence.equals("")) {
							if (!extractedSentences.contains(secondSelectedSentence)) {
								extractedSentences.add(secondSelectedSentence);
							}
						}
					}
				}
			}
			// zincirde hiç farklı kelime yoksa
			else {

				String word = "";
				for (Map.Entry<String, Integer> entry : uniqueLexicals.entrySet()) {
					word = entry.getKey();
				}
				for (String sentence : sentences) {
					String lowerSentence = sentence.toLowerCase();
					if (lowerSentence.contains(word)) {
						if (!extractedSentences.contains(sentence)) {
							extractedSentences.add(sentence);
							break;
						}
					}
				}
			}
		}

		List<String> orderedSentences = new ArrayList<String>();
		for(String sentence:sentences){
			if(extractedSentences.contains(sentence)){
				orderedSentences.add(sentence);
			}
		}
		LOGGER.info("representative: " + representativeWords);
		return orderedSentences;
	}

	public List<String> heuristic3(List<NewChain> chains, List<String> paragraphs, List<String> sentences) {
		List<String> extractedSentences = new ArrayList<String>();
		List<Integer> keys = new ArrayList<Integer>();
		for (NewChain chain : chains) {
			Map<Integer, Integer> paragraphDensity = new HashMap<Integer, Integer>();
			Map<String, Integer> sentenceDensity = new HashMap<String, Integer>();
			List<NewLexical> lexicals = chain.getLexicals();
			List<Integer> paragraphList = chain.getParagraphsOfChain();
			// paragrafların yoğunluğunu belirle
			for (NewLexical lexical : lexicals) {
				if (!paragraphDensity.containsKey(lexical.getParagraphNo())) {
					int occurrences = Collections.frequency(paragraphList, lexical.getParagraphNo());
					paragraphDensity.put(lexical.getParagraphNo(), occurrences);
				}
			}

			if (paragraphDensity.size() > 1) {
				int keyOfParagraph = -1;
				int maxFrequencyOfParagraph = -1;
				// en yüksek frekanslı paragrafı bul
				for (Map.Entry<Integer, Integer> entry : paragraphDensity.entrySet()) {
					if (maxFrequencyOfParagraph < entry.getValue().intValue()) {
						maxFrequencyOfParagraph = entry.getValue().intValue();
						keyOfParagraph = entry.getKey().intValue();
					}
				}
				// bu paragraftaki cümlelerin frekansını zincirdeki kelimelere
				// göre çıkart
				if (keyOfParagraph != -1) {
					List<Integer> sentencesList = chain.getSentencesOfChain(keyOfParagraph);
					for (NewLexical lexical : lexicals) {
						if (!sentenceDensity.containsKey(Integer.toString(lexical.getSentenceNo()))) {
							int occurrences = Collections.frequency(sentencesList, lexical.getSentenceNo());
							sentenceDensity.put(Integer.toString(lexical.getSentenceNo()), occurrences);
						}
					}
				}

				// en yüksek frekanslı cümleyi alalım
				sentenceDensity = this.sortByComparator(sentenceDensity);
				for (Map.Entry<String, Integer> entry : sentenceDensity.entrySet()) {
					if (!keys.contains(Integer.parseInt(entry.getKey()))) {
						keys.add(Integer.parseInt(entry.getKey()));
						break;
					}
				}

			}
			// zincirdeki bütün kelimeler aynı paragraftaysa
			else {
				int paragraphNo = -1;
				for (Map.Entry<Integer, Integer> entry : paragraphDensity.entrySet()) {
					paragraphNo = entry.getKey();
				}
				if (paragraphNo != -1) {
					List<Integer> sentencesList = chain.getSentencesOfChain(paragraphNo);
					for (NewLexical lexical : lexicals) {
						if (!sentenceDensity.containsKey(Integer.toString(lexical.getSentenceNo()))) {
							int occurrences = Collections.frequency(sentencesList, lexical.getSentenceNo());
							sentenceDensity.put(Integer.toString(lexical.getSentenceNo()), occurrences);
						}
					}
				}
				// en yüksek frekanslı cümleyi alalım
				sentenceDensity = this.sortByComparator(sentenceDensity);
				for (Map.Entry<String, Integer> entry : sentenceDensity.entrySet()) {
					if (!keys.contains(Integer.parseInt(entry.getKey()))) {
						keys.add(Integer.parseInt(entry.getKey()));
						break;
					}
				}
			}
		}
		// cümleleri yerlerine göre sıralayalım
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			extractedSentences.add(sentences.get(keys.get(i)));
		}
		return extractedSentences;

	}

	public Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
