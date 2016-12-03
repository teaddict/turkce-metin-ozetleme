package com.webservice.lexicalchain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.parser.MorphParse;
import zemberek.tokenizer.ZemberekLexer;

public class NewVerbFinder {

	TurkishMorphParser parser;

	public List<String> simpleTokenization(String input) {
		ZemberekLexer lexer = new ZemberekLexer();
		return lexer.tokenStrings(input);
	}

	public String parse(String word) {
    	boolean temp = true;
		List<MorphParse> parses = this.parser.parse(word);
		for (MorphParse parse : parses) {
        	if(parse.formatOnlyIgs().toString().contains("Verb") 
        			&& !parse.formatOnlyIgs().toString().contains("Noun") 
        			&& temp)
        	{
        		temp = false;
        		String verbRoot = parse.getLemma();
        		return verbRoot;
        	}
		}
		return "notVerb";
	}

	public List<String> getVerbs(String textFile) {
		List<String> tokened = new ArrayList<String>();
		List<String> verbs = new ArrayList<String>();
		// boşluğa göre kelimeleri ayırdı
		tokened = simpleTokenization(textFile);
		try {
			this.parser = TurkishMorphParser.createWithDefaults();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tokened.size(); i++) {
			String root = parse(tokened.get(i).toString());
			if (root != "notVerb") {
				verbs.add(root);
			}
		}
		return verbs;
	}

}
