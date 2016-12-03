package com.webservice.lexicalchain;
import java.util.List;

import zemberek.tokenizer.SentenceBoundaryDetector;
import zemberek.tokenizer.SimpleSentenceBoundaryDetector;

public class NewSentence {
	
	public List<String> getSentences(String text){
        SentenceBoundaryDetector detector = new SimpleSentenceBoundaryDetector();
        List<String> sentences = detector.getSentences(text);
        return sentences;
	}
	//ilk cümleyi alır ve başlık sonunda bir noktalama işareti olmadığı için, satırlara böler, ilk satırı başlık olarak geri dönderir
	public String getTitleSentence(List<String> sentences){
		String[] title = sentences.get(0).split("\\r?\\n");
		return title[0];
	}
	
}
