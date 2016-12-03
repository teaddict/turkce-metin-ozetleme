package com.webservice.lexicalchain;

//import java.io.IOException;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import junit.framework.Assert;
//
//public class NewTest {
//
//	@Before
//	public void setUp() throws Exception {
//		System.out.println("Beginning of Tests");
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		System.out.println("End of Tests");
//	}
//
//	@Test
//	public void testEmptyFile() throws IOException {
//		NewPreprocess preprocess = new NewPreprocess();
//		String textFilename = "src/test/res/datasets/test/emptyText.txt";
//		Assert.assertEquals("Özetlenemeyecek kadar kısa bir metin..", preprocess.readTextFile(textFilename));
//	}
//	
//	// it is for library
//	@Test
//	public void testFileExtension() throws IOException {
//		NewPreprocess preprocess = new NewPreprocess();
//		String textFilename = "src/test/res/datasets/test/emptyText";
//		Assert.assertEquals("Wrong extension of text file..", preprocess.readTextFile(textFilename));
//	}
//
//}
