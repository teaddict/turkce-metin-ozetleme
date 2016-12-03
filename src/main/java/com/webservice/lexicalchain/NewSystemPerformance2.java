package com.webservice.lexicalchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * Oluşturduğumuz sistemin algoritmasını şu şekilde açıklayabiliriz;
. Her metindeki cümle sayısını al
. Katılımcıların çıkardığı özetleri inceleyerek, burdaki cümlelerin frekansını bul. 
	Örneğin; 3. cümle 5 katılımcı tarafından özet olarak seçilmiş.
. Çıkartılan bu frekansı puan olarak kullan. 
	Örneğin; Birinci metindeki ikinci cümle oniki katılımcı tarafından özet olarak seçilmiş bu yüzden bu cümlenin puanı onikidir. 
	Birinci metindeki beşinci cümle ise hiç bir katılımcı tarafından seçilmediği için puanı sıfırdır. 
	Maksimum puan onsekizdir çünkü toplamda onsekiz katılımcı vardır. 
	Minimum puan is sıfırdır.
. Her metin için katılımcıların çıkardıkları özetleri bu puanlama sistemine göre hesaplayarak, 
	toplayıp ve ortalamasını al. 
. Uygulamamızın üç farklı uygulamayla çıkardığı özetleri de bu puanlama sistemine göre karşılaştır.
 * 
 * 
 * 
 */
public class NewSystemPerformance2 {
	public List<List<List<Integer>>> readUserPerformanceStatistics(String filename) throws IOException {
		List<List<List<Integer>>> results = new ArrayList<List<List<Integer>>>();
		BufferedReader input = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
		String readLine = input.readLine();
		while ((readLine != null)) // metin dosyası satır satır okundu.
		{
			sb.append(readLine);
			sb.append(System.lineSeparator());
			readLine = input.readLine();
		}
		input.close();
		String text = sb.toString();
		String[] lines = text.split("\n");
		for (String line : lines) {
			String[] userSelections = line.split(":");
			List<List<Integer>> listOfSelected = new ArrayList<>();
			for (String selections : userSelections) {
				String numbers[] = selections.split(",");
				List<Integer> listOfNumbers = new ArrayList<Integer>();
				for (String number : numbers) {
					listOfNumbers.add(Integer.parseInt(number));
				}
				listOfSelected.add(listOfNumbers);
			}
			results.add(listOfSelected);
		}
		return results;

	}

	public List<Integer> readTextLengths(String filename) throws IOException {
		List<Integer> results = new ArrayList<Integer>();
		BufferedReader input = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
		String readLine = input.readLine();
		while ((readLine != null)) // metin dosyası satır satır okundu.
		{
			results.add(Integer.parseInt(readLine));
			sb.append(readLine);
			sb.append(System.lineSeparator());
			readLine = input.readLine();
		}
		input.close();
		return results;
	}

	public List<List<Integer>> readSystemPerformanceStatistics(String filename) throws IOException {
		List<List<Integer>> results = new ArrayList<List<Integer>>();
		BufferedReader input = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
		String readLine = input.readLine();
		while ((readLine != null)) // metin dosyası satır satır okundu.
		{
			sb.append(readLine);
			sb.append(System.lineSeparator());
			readLine = input.readLine();
		}
		input.close();
		String text = sb.toString();

		String[] lines = text.split("\n");
		for (String line : lines) {
			String numbers[] = line.split(",");
			List<Integer> listOfNumbers = new ArrayList<Integer>();
			for (String number : numbers) {
				listOfNumbers.add(Integer.parseInt(number));
			}
			results.add(listOfNumbers);

		}
		return results;
	}

	public Map<Integer, Integer> calculateFrequencyOfSentences(int textLenght, List<List<Integer>> selectedFromUsers) {
		Map<Integer, Integer> sentenceFrequency = new HashMap<Integer, Integer>();
		for (int i = 0; i < textLenght; i++) {
			for (int j = 0; j < selectedFromUsers.size(); j++) {
				List<Integer> selectedSentences = new ArrayList<Integer>();
				selectedSentences = selectedFromUsers.get(j);
				for (int m = 0; m < selectedSentences.size(); m++) {
					if (selectedSentences.get(m) == (i + 1)) {
						if (sentenceFrequency.containsKey(i + 1)) {
							int temp = sentenceFrequency.get(i + 1);
							temp++;
							sentenceFrequency.put(i + 1, temp);
						} else {
							sentenceFrequency.put(i + 1, 1);
						}
					}
				}
			}
		}
		return sentenceFrequency;
	}

	public double calculateScoreOfUser(Map<Integer, Integer> sentenceFrequency, List<List<Integer>> selectedFromUsers) {
		int sum = 0;
		for (int j = 0; j < selectedFromUsers.size(); j++) {
			List<Integer> selectedSentences = new ArrayList<Integer>();
			selectedSentences = selectedFromUsers.get(j);
			for (int m = 0; m < selectedSentences.size(); m++) {
				if (sentenceFrequency.get(selectedSentences.get(m)) != null) {
					int score = sentenceFrequency.get(selectedSentences.get(m));
					sum = sum + score;
				}

			}
		}
		double average = (double) sum / (double) selectedFromUsers.size();
		return average;
	}

	public double calculateScoreOfSystem(Map<Integer, Integer> sentenceFrequency, List<Integer> selectedFromSystem) {
		int sum = 0;
		for (int i = 0; i < selectedFromSystem.size(); i++) {
			if (sentenceFrequency.get(selectedFromSystem.get(i)) != null) {
				int score = sentenceFrequency.get(selectedFromSystem.get(i));
				sum = sum + score;
			}
		}
		return (double) sum;
	}

	public String calculatePerformanceStatistics(String textLenghtsFile, String systemReport, String userReport, int k)
			throws IOException {
		List<Double> heuristicStatistics = new ArrayList<Double>();
		List<Double> userStatistics = new ArrayList<Double>();

		// kullanıcıların çıkardığı özetleri al
		// metin[i] - kullanıcı[j] - seçilen cümleler[k]
		List<List<List<Integer>>> userResults = new ArrayList<List<List<Integer>>>();
		// sistemin çıakrdığı özetleri al
		// metin[i] - seçilen cümleler[k]
		List<List<Integer>> systemResults = new ArrayList<List<Integer>>();
		// metin [i] = count of sentences
		List<Integer> textLenghts = new ArrayList<Integer>();

		userResults = this.readUserPerformanceStatistics(userReport);
		systemResults = this.readSystemPerformanceStatistics(systemReport);
		textLenghts = this.readTextLengths(textLenghtsFile);
		// her metni teker teker yollayıp cümle frekansı ve kullanıcı
		// puanlamasını bul
		for (int i = 0; i < textLenghts.size(); i++) {
			Map<Integer, Integer> sentenceFrequency = new HashMap<Integer, Integer>();
			List<List<Integer>> selectedFromUsers = new ArrayList<List<Integer>>();
			selectedFromUsers = userResults.get(i);
			sentenceFrequency = this.calculateFrequencyOfSentences(textLenghts.get(i), selectedFromUsers);
			userStatistics.add(this.calculateScoreOfUser(sentenceFrequency, selectedFromUsers));
		}
		for (int i = 0; i < textLenghts.size(); i++) {
			Map<Integer, Integer> sentenceFrequency = new HashMap<Integer, Integer>();
			List<List<Integer>> selectedFromUsers = new ArrayList<List<Integer>>();
			selectedFromUsers = userResults.get(i);
			sentenceFrequency = this.calculateFrequencyOfSentences(textLenghts.get(i), selectedFromUsers);
			List<Integer> selectedFromSystem = systemResults.get(i);
			heuristicStatistics.add(this.calculateScoreOfSystem(sentenceFrequency, selectedFromSystem));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("############HEURISTIC " + k + " RESULTS###############\n\n");
		for (int i = 0; i < heuristicStatistics.size(); i++) {
			String tempLine = "METİN " + (i + 1) + "," + "heuristic:" + heuristicStatistics.get(i).toString() + ","
					+ "user: " + userStatistics.get(i).toString();
			sb.append(tempLine);
			sb.append(System.lineSeparator());
		}
		String text = sb.toString();
		System.out.println(text);
		return text;
	}

	public void writePerformanceStatistics(String filename, String text) throws IOException {
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(filename));
		writer.write(text);
		writer.flush();
		writer.close();

	}

	public static void main(String[] args) throws IOException {
		NewSystemPerformance2 systemPerformance2 = new NewSystemPerformance2();
		String resultFilename = "src/main/resources/datasets/test/sonuclar/report2/results.txt";
		String textLenghts = "src/main/resources/datasets/test/sonuclar/report2/cumle_sayilari.txt";
		String heuristic1 = "src/main/resources/datasets/test/sonuclar/report2/heuristic1.txt";
		String heuristic2 = "src/main/resources/datasets/test/sonuclar/report2/heuristic2.txt";
		String heuristic3 = "src/main/resources/datasets/test/sonuclar/report2/heuristic3.txt";
		String userReport = "src/main/resources/datasets/test/sonuclar/report2/userOrdered.txt";

		String heuristic1Result = systemPerformance2.calculatePerformanceStatistics(textLenghts, heuristic1, userReport,
				1);
		String heuristic2Result = systemPerformance2.calculatePerformanceStatistics(textLenghts, heuristic2, userReport,
				2);
		String heuristic3Result = systemPerformance2.calculatePerformanceStatistics(textLenghts, heuristic3, userReport,
				3);
		String sumOfResults = heuristic1Result + "\n\n" + heuristic2Result + "\n\n" + heuristic3Result;
		systemPerformance2.writePerformanceStatistics(resultFilename, sumOfResults);

	}

}
