package com.webservice.lexicalchain;
/**
 * A Java class that implements a simple text classifier, based on WEKA.
 * To be used with MyFilteredLearner.java.
 * WEKA is available at: http://www.cs.waikato.ac.nz/ml/weka/
 * Copyright (C) 2013 Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 *
 * This program is free software: you can redistribute it and/or modify
 * it for any purpose.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

import com.webservice.controller.WebController;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class implements a simple text classifier in Java using WEKA. It loads a
 * file with the text to classify, and the model that has been learnt with
 * MyFilteredLearner.java.
 * 
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see NewLearner
 */
public class NewClassifier {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	/**
	 * String that stores the text to classify
	 */
	String text;
	/**
	 * Object that stores the instance.
	 */
	Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	FilteredClassifier classifier;

	/**
	 * This method loads the text to be classified.
	 * 
	 * @param fileName
	 *            The name of the file that stores the text.
	 */
	NewClassifier textClassifier;

	public void load(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			text = "";
			while ((line = reader.readLine()) != null) {
				text = text + " " + line;
			}
			LOGGER.info("===== Loaded text data: " + fileName + " =====");
			reader.close();
		} catch (IOException e) {
			LOGGER.info("Problem found when reading: " + fileName);

		}
	}

	public void loadFile(String fileName) {
		text = fileName;
	}

	/**
	 * This method loads the model to be used as classifier.
	 * 
	 * @param fileName
	 *            The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
			in.close();
			LOGGER.info("===== Loaded model: " + fileName + " =====");
		} catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along
			// with the IOException
			LOGGER.info("Problem found when reading: " + fileName);

		}
	}

	/**
	 * This method creates the instance to be classified, from the text that has
	 * been read.
	 */
	public void makeInstance() {
		// Create the attributes, class and text
		// ekonomi,magazin,saglik,siyasi,spor
		FastVector fvNominalVal = new FastVector(2);
		fvNominalVal.addElement("teknoloji");
		fvNominalVal.addElement("saglik");
		fvNominalVal.addElement("ekonomi");
		fvNominalVal.addElement("spor");
		fvNominalVal.addElement("siyasi");
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("text", (FastVector) null);
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		instances = new Instances("Test relation", fvWekaAttributes, 1);
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance

		Instance instance = new Instance(2);

		// DenseInstance instance = new DenseInstance(2);

		instance.setValue(attribute2, text);

		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
		LOGGER.info("===== Instance created with reference dataset =====");
	}

	/**
	 * This method performs the classification of the instance. Output is done
	 * at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: "
					+ instances.classAttribute().value((int) pred));
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("Problem found when classifying the text");
		}
	}

	public String getClassOfText() {
		String classOfText = "";
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			LOGGER.info("===== Classified instance =====");
			LOGGER.info("Class predicted: "
					+ instances.classAttribute().value((int) pred));
			classOfText = instances.classAttribute().value((int) pred)
					.toString();
		} catch (Exception e) {
			LOGGER.info(e.toString());
			LOGGER.info("Problem found when classifying the text");
		}
		return classOfText;
	}

	/**
	 * Main method. It is an example of the usage of this class.
	 * 
	 * @param args
	 *            Command-line arguments: fileData and fileModel.
	 */
	public static void main(String[] args) {

		NewClassifier classifier;

		String testFile = "src/main/resources/datasets/test/banu_hoca/8.txt";
		String dataModel = "src/main/resources/datasets/1371haber/raw_texts/myClassifier.dat";
		classifier = new NewClassifier();
		classifier.load(testFile);
		classifier.loadModel(dataModel);
		classifier.makeInstance();
		classifier.classify();
	}

	public String getClassOfText(String textFile) {
		if (textClassifier == null) {
			String dataModel = "datasets/1371haber/raw_texts/myClassifier.dat";
			textClassifier = new NewClassifier();
			textClassifier.loadFile(textFile);
			textClassifier.loadModel(dataModel);
			textClassifier.makeInstance();
			return textClassifier.getClassOfText();
		} else {
			textClassifier.loadFile(textFile);
			return textClassifier.getClassOfText();

		}
	}
}
