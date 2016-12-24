package com.webservice.lexicalchain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.webservice.controller.WebController;

public class NewChainScores {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	/*
	 * We define the score of an interpretation as the sum of its chain scores.
	 * A chain score is determined by the number and weight of the relations
	 * between chain members. Experimentally, we fixed the weight of reiteration
	 * = 10 synonymy = 10 antonymy = 7 hypernymy = 4 hyponymy = 4 related_with =
	 * 4 holo_part = 4 holo_member = 4 yan_kavram = 4
	 * 
	 */
	public List<NewChain> calculateChainScores(List<NewChain> chains) {
		int sumScore = 0;
		for (int i = 0; i < chains.size(); i++) {
			List<String> relations = new ArrayList<String>();
			relations = chains.get(i).getRelationType();
			for (int j = 0; j < relations.size(); j++) {
				String relation = relations.get(j);
				if (relation.equals("synonymy")) {
					sumScore += 10;
				} else if (relation.equals("antonymy")) {
					sumScore += 7;
				} else if (relation.equals("hypernymy") || relation.equals("hyponymy")
						|| relation.equals("related_with") || relation.equals("holo_part")
						|| relation.equals("holo_member") || relation.equals("yan_kavram")) {
					sumScore += 4;
				}
			}
			chains.get(i).setScore(sumScore);
			sumScore = 0;
		}

		return chains;
	}

	/*
	 * 
	 * 1. Compute the aggregate score of each chain by summing the scores of
	 * each individual element in the chain. 2. Pick up the chains whose score
	 * is more than the mean of the scores for every chain computed in the
	 * document. 3. For each of the strong chains, identify representative
	 * words, whose contribution to the chain is maximum 4. Choose the sentence
	 * that contains the first appearance of a representative chain member in
	 * the text.
	 */
	public List<NewChain> calculateChainStrengths(List<NewChain> chains) {
		for (int i = 0; i < chains.size(); i++) {
			List<NewLexical> lexicals = new ArrayList<NewLexical>();
			Set<String> uniqueLexicals = new HashSet<String>();
			lexicals = chains.get(i).getLexicals();
			for (int j = 0; j < lexicals.size(); j++) {
				if (!uniqueLexicals.contains(lexicals.get(j).getWord())) {
					uniqueLexicals.add(lexicals.get(j).getWord());
				}
			}
			double homogenity = 1.0 - ((double) uniqueLexicals.size() / (double) lexicals.size());
			double strength = (double) lexicals.size() * (double) homogenity;
			chains.get(i).setStrength(strength);
		}
		return chains;
	}

	/*
	 * ortalama puanın üstündekileri al ortalama strength üstündekileri al
	 * “Strength Criterion”: Score(Chain) > Average(Scores) + 2 ∗
	 * StandardDeviation(Scores)
	 */
	public List<NewChain> getStrongChains(List<NewChain> chains) {
		double averageScoreOfChain = 0.0;
		double averageStrengthOfChain = 0.0;
		double sumStrengthOfChain = 0.0;
		double sumScoreOfChain = 0.0;
		List<NewChain> strongChains = new ArrayList<NewChain>();
		for (int i = 0; i < chains.size(); i++) {
			sumScoreOfChain += chains.get(i).getScore();
			sumStrengthOfChain += chains.get(i).getStrength();
		}
		averageScoreOfChain = sumScoreOfChain / chains.size();
		LOGGER.info("Kelime zinciri ortalama puan değeri:" + averageScoreOfChain);

		averageStrengthOfChain = sumStrengthOfChain / (double) chains.size();
		LOGGER.info("Kelime zinciri ortalama güç değeri:" + averageStrengthOfChain);

		double temp = 0.0;
		for (int i = 0; i < chains.size(); i++) {
			temp += (averageStrengthOfChain - chains.get(i).getStrength())
					* (averageStrengthOfChain - chains.get(i).getStrength());
		}
		double variance = temp / (double) chains.size();
		double stddev = Math.sqrt(variance);
		double criterion = averageStrengthOfChain + 2.0 * stddev;
		LOGGER.info("Kelime zinciri kriter değeri:" + criterion);

		for (int i = 0; i < chains.size(); i++) {
			if (chains.get(i).getStrength() >= criterion) {
				strongChains.add(chains.get(i));
			}
		}
		
		if(strongChains.isEmpty()){
			LOGGER.info("Kriter degerinin ustunde zincir bulunamadi, en guclu zincir alincak!");
			double tempStrength = chains.get(0).getStrength();
			int tempIndexNumber = 0;
			for (int i = 1; i < chains.size(); i++) {
				if (chains.get(i).getStrength() > tempStrength) {
					tempIndexNumber = i;
				}
			}
			strongChains.add(chains.get(tempIndexNumber));
		}

		return strongChains;
	}


	/*
	 * zincirdeki kelimeleri sınıflandırma işlemine gönder dönen sonucun metnin
	 * sınıfyla karşılaştır aynı ise true, değilse false
	 */
	public boolean checkClassOfChain(NewChain chain) {

		return true;
	}
}
