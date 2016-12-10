package com.webservice.lexicalchain;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.webservice.controller.WebController;

public class NewLexicalChain {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	// metin içindeki tüm sözcükleri lexicals olarak alıyoruz
	public List<NewChain> chainBuilder(List<NewLexical> lexicals) throws IOException {
		// time
		long startTime = System.nanoTime();
		List<NewChain> chains = new ArrayList<NewChain>();
		NewSemanticListParser wordnetparser = new NewSemanticListParser();
		String textFilename = "datasets/lexical/all_relations.csv";
		String text = wordnetparser.readTextFile(textFilename);
		Set<String> words = wordnetparser.wordListNew(text);
		HashMap<String, Set<String>> wordMap = new HashMap<String, Set<String>>();
		wordMap = wordnetparser.sentenceListNew(words, text);
		// her kelimeyi teker teker işleme sokuyoruz
		for (NewLexical lexical : lexicals) {
			String word = lexical.getWord();
			Set<String> result = wordMap.get(word);
			if (result != null) {
				// her kelimeyi semantik listemizde aratıp sonuçları alıyoruz
				for (String tempWord : result) {
					String relation[] = tempWord.split(":");
					String searchWord = relation[0];
					String searchRelation = relation[1];
					String searchRelatedWord = relation[2];
					// eğer hiç zincirimiz yoksa yenisini oluşturuyoruz
					if (chains.size() < 1) {
						NewChain chain = new NewChain();
						chain.addToChain(lexical, searchRelation, searchRelatedWord);
						chains.add(chain);
					}
					// eğer varsa zincireri çekip içindeki lexicallere bakıp
					// karşılaştırma yapacağız
					else {
						boolean added = false;
						for (int i = 0; i < chains.size(); i++) {
							NewChain tempChain = chains.get(i);
							List<NewLexical> tempLexicals = tempChain.getLexicals();
							List<String> tempRelations = tempChain.getRelationType();
							List<String> tempRelatedWords = tempChain.getRelatedWord();
							for (int j = 0; j < tempLexicals.size(); j++) {
								if (searchWord.equals(tempLexicals.get(j).getWord())
										&& searchRelatedWord.equals(tempRelatedWords.get(j))) {
									added = true;
									chains.get(i).addToChain(lexical, searchRelation, searchRelatedWord);
									break;
								}
								/*
								 * zaman:syn:çağ çağ:syn:devir bu şekilde alıyor
								 */
								// else
								// if(searchRelatedWord.equals(tempLexicals.get(j).getWord())){
								// added = true;
								// chains.get(i).addToChain(searchWord, lexical,
								// searchRelation,searchRelatedWord);
								// break;
								// }
								/* 
								 
								/*
								 * araba:syn:taşıt
								 * bisiklet:syn:taşıt 
								 * yani ilişki kurulan kelime aynı
								 * 
								 */
								else if (searchRelatedWord.equals(tempRelatedWords.get(j))) {
									added = true;
									chains.get(i).addToChain(lexical, searchRelation, searchRelatedWord);
									break;
								}
							}
						}
						if (!added) {
							NewChain newChain = new NewChain();
							newChain.addToChain(lexical, searchRelation, searchRelatedWord);
							chains.add(newChain);
						}
					}
				}
			}
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; // divide by 1000000 to
															// get milliseconds.

		LOGGER.info("Execute time(miliseconds): " + duration);
		LOGGER.info("All chain size: " + chains.size());

		// for (NewChain chain : chains) {
		// System.out.println("\n##############Chain##############\n");
		// chain.printChain();
		// }
		// String filename = "src/test/res/datasets/test/chains2.csv";
		// writeChainsToTextfile(filename,chains);
		
		return chains;
	}

	/*
	 * burda chain analiz edilcek ve içindekilere bakılcak mesela aynı kelimeye
	 * ait ve hep aynı kelimeden oluşan birden fazla zincir olmaması gerek çünkü
	 * farklı synonymler olduğu için aynı kelimenin birden fazla zinciri
	 * olabiliyor hepsinin de boyutu aynı oluyor
	 */
	public List<NewChain> chainAnalyse(List<NewChain> chains) {
		Map<String,NewChain> uniqueChains = new HashMap<String,NewChain>();
		List<NewChain> listOfChains = new ArrayList<NewChain>();
		for(int i=0; i<chains.size(); i++){
			String key="";
			List<NewLexical> lexicals = chains.get(i).getLexicals();
			for(int j=0; j<lexicals.size(); j++){
				key +=lexicals.get(j).getWord();
			}
			if(!uniqueChains.containsKey(key)){
				uniqueChains.put(key, chains.get(i));
				key ="";
			}
		}
		for (Map.Entry<String,NewChain> entry : uniqueChains.entrySet()) {
			NewChain chain = entry.getValue();
			listOfChains.add(chain);
		}
		return listOfChains;
	}

	public void writeChainsToTextfile(String filename, List<NewChain> allChains) throws IOException {
		String text = "";
		for (NewChain chain : allChains) {
			text += chain.getChainInformation();
		}
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
	    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
	    	bufferedWriter.write(text);
	    	bufferedWriter.flush();
	    	bufferedWriter.close();
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
