package com.webservice.lexicalchain;

import java.io.IOException;

import org.springframework.stereotype.Service;

import zemberek.morphology.apps.TurkishMorphParser;
//tek bir tane olusturmamiz gerek cunku cok zaman aliyor yaklasik 1,5 sn 
//server icin iyi degil , bu yuzden bu sekilde hizlandirildi
@Service
public class TurkishParser {
	TurkishMorphParser parser;
	public TurkishParser() throws IOException {
		super();
		//parser ı burda oluşturmayıp fonksiyon içinde oluşturursak çok zaman alıyor!
		this.parser = TurkishMorphParser.createWithDefaults();
	}
	
	public TurkishMorphParser getParser(){
		return this.parser;
	}
}
