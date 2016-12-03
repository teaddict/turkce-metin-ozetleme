package com.webservice.lexicalchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 * Dr. Banu Diri'nin kullandığı sistemi kullanarak kendi başarı oranımızı çıkartıp, karşılaştırmak istedik.
 * Bu ölçüm şekli; "Sistemin performansı, her bir metin dosyasının özetleme başarısı ayrı ayrı bulunup, 
 * ortalaması alınarak hesaplanmıştır. Özeti çıkarılacak olan bir metin dosyasının başarısı,
 * ilgili metin dosyasının özetini çıkaran her test kullanıcının özet olarak aldığı cümleler ile, 
 * sistemin özet olarak kabul ettiği cümlelerle aynı olan cümle sayısının,
 * kullanıcının çıkardığı özet cümle sayısına oranlarının toplamının ortalaması alınarak hesaplanır." 
 * 
 * 
 */
public class NewSystemPerformance {

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

	public String calculatePerformanceStatistics(String systemReport, String userReport,int k)
			throws IOException {
		List<Double> heuristicStatistics = new ArrayList<Double>();
		//kullanıcıların çıkardığı özetleri al
		//metin[i] - kullanıcı[j] - seçilen cümleler[k]
		List<List<List<Integer>>> userResults = new ArrayList<List<List<Integer>>>();
		//sistemin çıakrdığı özetleri al
		//metin[i] - seçilen cümleler[k]
		List<List<Integer>> systemResults = new ArrayList<List<Integer>>();

		userResults = this.readUserPerformanceStatistics(userReport);
		systemResults = this.readSystemPerformanceStatistics(systemReport);
		heuristicStatistics = this.getPerformanceStatistics(userResults, systemResults);
		StringBuilder sb = new StringBuilder();
		sb.append("############HEURISTIC " + k + "RESULTS###############\n\n");
		for(int i=0; i<heuristicStatistics.size(); i++){
			String tempLine = "METİN " + (i+1) + " : " + heuristicStatistics.get(i).toString();
			sb.append(tempLine);
			sb.append(System.lineSeparator());
		}
		String text = sb.toString();
		System.out.println(text);
		return text;
	}

	public List<Double> getPerformanceStatistics(List<List<List<Integer>>> userResults, List<List<Integer>> systemResults) {
		List<Double> heuristicStatistics = new ArrayList<Double>();
		// metin metin incelemeye alalım
		for(int i=0; i<systemResults.size(); i++){
			List<Integer> selectedFromSystem = new ArrayList<Integer>();
			List<List<Integer>> selectedFromUsers = new ArrayList<List<Integer>>();
			selectedFromSystem = systemResults.get(i);
			selectedFromUsers = userResults.get(i);
			double sum = 0.0;
			double average = 0.0;
			for(int j=0; j<selectedFromUsers.size(); j++){
				List<Integer> selectedSentences = new ArrayList<Integer>();
				selectedSentences = selectedFromUsers.get(j);
				int same = 0;
				for(int m=0; m<selectedSentences.size(); m++){
					for(int k=0; k<selectedFromSystem.size(); k++){
						if(selectedFromSystem.get(k).equals(selectedSentences.get(m))){
							same = same + 1;
							break;
						}
					}
				}
				double tempSum = (double)same / (double)selectedSentences.size();
				sum = tempSum + sum;
			}
			average = sum / 18.0;
			heuristicStatistics.add(average);
		}
		return heuristicStatistics;
	}

	public void writePerformanceStatistics(String filename, String text) throws IOException {
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(filename));
		writer.write(text);
		writer.flush();
		writer.close();

	}

	public static void main(String[] args) throws IOException {
		NewSystemPerformance systemPerformance = new NewSystemPerformance();
		String resultFilename = "src/main/resources/datasets/test/sonuclar/report1/results.txt";
		String heuristic1 = "src/main/resources/datasets/test/sonuclar/report1/heuristic1.txt";
		String heuristic2 = "src/main/resources/datasets/test/sonuclar/report1/heuristic2.txt";
		String heuristic3 = "src/main/resources/datasets/test/sonuclar/report1/heuristic3.txt";
		String userReport = "src/main/resources/datasets/test/sonuclar/report1/userOrdered.txt";

		String heuristic1Result = systemPerformance.calculatePerformanceStatistics(heuristic1, userReport,1);
		String heuristic2Result = systemPerformance.calculatePerformanceStatistics(heuristic2, userReport,2);
		String heuristic3Result = systemPerformance.calculatePerformanceStatistics(heuristic3, userReport,3);
		String sumOfResults = heuristic1Result + "\n\n" + heuristic2Result + "\n\n"	+ heuristic3Result;
		systemPerformance.writePerformanceStatistics(resultFilename, sumOfResults);


	}

}
