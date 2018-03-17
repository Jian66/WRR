package gaussian;

import java.io.IOException;
import java.util.Random;

import dynamic.ReadData;

/**
 * 
 * @author JianWang,DLUT
 *
 */

public class Gaussian {
	
	static ReadData reader = new ReadData();
	static String filePath = "E:/Users/JianW/workspace/CLASSICMETHOD/src/gaussian/data/";
	static String fileName1 = "2015.txt"; // 2015 historical
	static String fileName2 = "2016.txt"; // 2016 historical
	
	static String fileName3 = "forecast.txt"; 
	static String fileName4 = "epsilon.txt"; 


	static double[][] historical;
	static double[] sigma;
	
	static double[][] forecast;
	static double[][] epsilon;

	
	public static void main(String[] args) throws IOException {
		
		historical = reader.readMatrix(filePath + fileName1);
		
		Random r = new Random();
		
		int month = 1;// month=1,2..,12
		int plantNum = historical.length;
		
		forecast = new double[plantNum][month*10];
		epsilon = new double[plantNum][month*10];
		
		sigma = new double[month];
		for (int i = 0; i < month; i++) {
			sigma[i] = 0.05 + i * 0.01;
		}
		
		for (int j = 0; j < plantNum; j++) {
			for (int i = 0; i < 10; i++) {
				for (int t = 0; t < month; t++) {
					double temp = r.nextGaussian();
					
					forecast[j][t+i*month] = historical[j][t+12-month] + historical[j][t+12-month] * sigma[t] * temp;
					epsilon[j][t+i*month] = temp;
					
//					System.out.print(forecast + "\t");
				}
			}
			System.out.println();
		}
		
		reader.writeMatrix(filePath + fileName3, forecast);
		reader.writeMatrix(filePath + fileName4, epsilon);

	}
	
	
}
