package com.webservice.lexicalchain;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NewParagraph {

	public List<String> getParagraphs(String text) {
		String lineBreakCharacters = "\n\n";
		StringTokenizer st = new StringTokenizer(text, lineBreakCharacters);
		List<String> paragraphs = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			//if (st.nextToken().matches(".*\\w.*") ) { //check if contains any character not only whitespace
				paragraphs.add(st.nextToken());
			//}
		}
		//bazen boş paragraf aldığı için düzenleme yaptık
		List<String> correctedParagraphs = new ArrayList<String>();
		for(String parag : paragraphs){
			if(parag.length()>1){
				correctedParagraphs.add(parag);
			}
		}
		return correctedParagraphs;
	}

}
