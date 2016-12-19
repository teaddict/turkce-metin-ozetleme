package com.webservice.lexicalchain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zemberek.morphology.parser.MorphParse;
import zemberek.tokenizer.ZemberekLexer;

public class NewNounFinder {
	
	TurkishParser parser;
	public NewNounFinder(TurkishParser p) throws IOException {
		super();
		this.parser = p;
	}

	public List<String> simpleTokenization(String input) {
		ZemberekLexer lexer = new ZemberekLexer();
		return lexer.tokenStrings(input);
	}

	public String parse(String word) {
		List<MorphParse> parses = this.parser.parser.parse(word);
		List<String> nouns = new ArrayList<String>();
		for (MorphParse parse : parses) {
			if (parse.formatOnlyIgs().toString().contains("Noun")
					&& !parse.formatOnlyIgs().toString().contains("Verb")) {
				String nounRoot = parse.getLemma();
				nouns.add(nounRoot);

			}
		}

		if (nouns.size() < 1) {
			return "notNoun";
		} else {
			for (int i = 0; i < nouns.size(); i++) {
				for (int j = 0; j < nouns.size(); j++) {
					if (word.length() - nouns.get(j).length() == (i+1)) {
						return nouns.get(j);
					}
				}
			}
		}
		return nouns.get(0);
	}

	public List<String> getNounRoots(String textFile) {
		List<String> tokened = new ArrayList<String>();
		List<String> nouns = new ArrayList<String>();
		// boşluğa göre kelimeleri ayırdı
		tokened = simpleTokenization(textFile);
		for (int i = 0; i < tokened.size(); i++) {
			String root = parse(tokened.get(i).toString());
			if (root != "notNoun") {
				nouns.add(root);
			}
		}
		return nouns;
	}

}
