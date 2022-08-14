package map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class BMIChallenge {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		
		if(args.length<2) {
			System.out.println("Please provide 2 parameters: Output File Path & Input File Path");
			System.out.print("Press any key to exit . . . ");
		    s.nextLine();
		    System.exit(1);
		}
		String outputFilePath = args[0].replaceAll("\"", "");
		String inputFilePath = args[1].replaceAll("\"", "");		
			    
	    int countOverweight = calculateBMI(outputFilePath, inputFilePath);
	    System.out.println("# of Overweight people: "+countOverweight);
	    	    
	    System.out.print("Press any key to exit . . . ");
	    s.nextLine();
	}
	
	public static int calculateBMI(String outputFilePath, String inputFilePath) throws IOException {
		BufferedWriter out = 
		    	new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath)));
		
		int countOverweight = 0;
		    
		File file = new File(inputFilePath);
		if(!file.exists()) {
			System.out.println("Input File doesn't exist");
			throw new FileNotFoundException(inputFilePath+" doesn't exist");
		}
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(file),"UTF8"));
		
		out.write("[\n");
		
		String line = "";
		while((line=(reader.readLine()))!=null) {
			String gender = extractData(line, "Gender\": \"", "\"");
			String height = extractData(line, "HeightCm\":", ",");
			String weight = extractData(line, "WeightKg\":", "}");
			
			double dHeight = 0,dWeight=0;
			try {
				dHeight = Double.parseDouble(height)/100;
			} catch(Exception ex) {
				
			}
			
			try {
				dWeight = Double.parseDouble(weight);
			} catch(Exception ex) {
				
			}
			
			if(dHeight==0) {
				System.out.println("Error in height value: "+height);
			}
			if(dWeight==0) {
				System.out.println("Error in weight value: "+weight);
			}
			
			double bmiValue = 0;
			String bmiCategory="",healthRisk="";
			if(dHeight==0||dWeight==0) {
				bmiCategory = "N/A";
				healthRisk = "N/A";
			} else {
				bmiValue = (dWeight)/(dHeight*dHeight);
				bmiValue = round2Digits(bmiValue);
				
				if(bmiValue <= 18.4) {
					bmiCategory = "Underweight";
					healthRisk = "Malnutrition risk";
				} else if(bmiValue>18.4 && bmiValue<25) {
					bmiCategory = "Normal weight";
					healthRisk = "Low risk";
				} else if(bmiValue>=25 && bmiValue<30) {
					bmiCategory = "Overweight";
					healthRisk = "Enhanced risk";
					countOverweight++;
				} else if(bmiValue>=30 && bmiValue<35) {
					bmiCategory = "Moderately obese";
					healthRisk = "Medium risk";
				} else if(bmiValue>=35 && bmiValue<40) {
					bmiCategory = "Severely obese";
					healthRisk = "High risk";
				} else if(bmiValue>=40) {
					bmiCategory = "Very severely obese";
					healthRisk = "Very high risk";
				}
			}
			
			out.write("{\"Gender\": \""+gender+"\", \"HeightCm\": "+height+", \"WeightKg\": "+weight+", \"BMI\": "+bmiValue+", \"BMICategory\": \""+bmiCategory+"\", \"HealthRisk\":\""+healthRisk+"\" },\r\n");			
		}
		out.write("]");
		
		out.flush();
		out.close();
		
		reader.close();
		
		return countOverweight;
	}
	
	public static double round2Digits(double num) {
		double result = num * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}
	
	public static String extractData(String data, String start, String end) {
		int i = data.indexOf(start);
		String result = "";
		if(i >= 0) {
			data = data.substring(i+start.length());
			int j = data.indexOf(end);
			if(j>=0) {
				result = data.substring(0, j);
			} else {
				return "";
			}
		}
				
		return result;
	}
	

}
