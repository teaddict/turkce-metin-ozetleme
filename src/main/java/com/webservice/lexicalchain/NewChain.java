package com.webservice.lexicalchain;

import java.util.ArrayList;
import java.util.List;

public class NewChain {

	private String subjectOfChain;
	private int score = 0;
	private double strength = 0;
	private List<NewLexical> lexicals = new ArrayList<NewLexical>();
	private List<String> relationType = new ArrayList<String>();
	private List<String> relatedWord = new ArrayList<String>();


	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getSubjectOfChain() {
		return subjectOfChain;
	}

	public void setSubjectOfChain(String subjectOfChain) {
		this.subjectOfChain = subjectOfChain;
	}

	public void addToChain(NewLexical lexical, String relationType,String relatedWord) {
		this.lexicals.add(lexical);
		this.relationType.add(relationType);
		this.relatedWord.add(relatedWord);
	}

	public List<NewLexical> getLexicals() {
		return lexicals;
	}

	public void setLexicals(List<NewLexical> lexicals) {
		this.lexicals = lexicals;
	}

	public List<String> getRelationType() {
		return relationType;
	}

	public void setRelationType(List<String> relationType) {
		this.relationType = relationType;
	}

	public List<String> getRelatedWord() {
		return relatedWord;
	}

	public void setRelatedWord(List<String> relatedWord) {
		this.relatedWord = relatedWord;
	}
	
	public Integer getSizeOfChain(){
		return this.lexicals.size();
	}
	
	public List<String> getWordsOfChain(){
		List<String> words = new ArrayList<String>();
		for(NewLexical lexical : this.lexicals){
			words.add(lexical.getWord());
		}
		return words;
	}
	
	public List<Integer> getParagraphsOfChain(){
		List<Integer> paragraphs = new ArrayList<Integer>();
		for(NewLexical lexical : this.lexicals){
			paragraphs.add(lexical.getParagraphNo());
		}
		return paragraphs;
	}
	
	public List<Integer> getSentencesOfChain(int paragraphNo){
		List<Integer> sentences = new ArrayList<Integer>();
		for(NewLexical lexical : this.lexicals){
			if(lexical.getParagraphNo() == paragraphNo){
			sentences.add(lexical.getSentenceNo());
			}
		}
		return sentences;
	}
	
	
	public void printChain(){
		for(int i=0; i<this.lexicals.size(); i++){
			System.out.println(this.lexicals.get(i).getWord());
			System.out.println(this.relationType.get(i));
			System.out.println(this.relatedWord.get(i));
		}
		System.out.println(this.lexicals.size());
		System.err.println(this.getScore());
		System.err.println(this.getStrength());
		System.out.println("\n");

	}
	public String getChainInformation(){
		String chain = "";
		for(int i=0; i<this.lexicals.size(); i++){
			chain += "(" + this.lexicals.get(i).getWord() +" ";
			chain += this.relationType.get(i) +" ";
			chain += this.relatedWord.get(i) +") ";
			chain += "P" +this.lexicals.get(i).getParagraphNo() + "-S" + this.lexicals.get(i).getSentenceNo() +",";
		}
		chain += ":" + this.lexicals.size();
		chain += ":" + this.getScore();
		chain += ":" + this.getStrength() +"\n";



		return chain;
	}
}
