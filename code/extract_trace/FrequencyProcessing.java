// package com.dance.csc.ncsu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.Calendar;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

public class FrequencyProcessing {
	
	//static String pathOfFile = "./phpmoadmin-1.txt";
	//static String outputFile = "./phpmoadmin-1_freqvector_test.csv";
  
	static HashMap<String, Integer> syscallMap = new HashMap<String, Integer>();
	static Long interval = (long) 100; // 1000 = 1 second
	static int numberOfSyscall;
	static int numberOfSample;

	public static void main(String[] args) throws IOException {
    String pathOfFile = args[0];
    String outputFile = args[1];
		
		File file = new File(pathOfFile);
		FileReader fr = new FileReader(file);
		FileReader fr2 = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		BufferedReader br2 = new BufferedReader(fr2);
		StringBuffer sb = new StringBuffer();
		
		String readLine;
		String systemCall = null;
		int defaultValue = 0;
		
		while ((readLine = br.readLine()) != null) {
			
			if (readLine.split("\\s+")[5].equals(">")) {
				systemCall = readLine.split("\\s+")[6];
			} else {
				continue;
			}
			
			syscallMap.put(systemCall, defaultValue);
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		
		String fileHeader = "timestamp,";
		
		for (Entry<String, Integer> entry : syscallMap.entrySet()) {
			fileHeader = fileHeader + entry.getKey() + ",";
		}
		
		fileHeader = fileHeader.substring(0, fileHeader.length() - 1);
		bw.write(fileHeader);
		bw.newLine();
		
		bw.close();
		
		String readLine2 = null;
		
		
		long sampleBeginTimestamp = 0;
		long currentTimestamp = 0;
		String systemCall2 = null;
		
        // String currentDate = "2018-01-21 ";
        
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) ;
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) ;
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String currentDate = year+"-"+month+"-"+day+" ";
        
        /*
		LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
		String currentDate = date.format(formatter);
        */
		while ((readLine2 = br2.readLine()) != null) {
			
			if (readLine2.split("\\s+")[5].equals(">")) {
				systemCall2 = readLine2.split("\\s+")[6];
			} else {
				continue;
			}
			
			String timeStampString = readLine2.split("\\s+")[1];
			timeStampString = currentDate + timeStampString;
//			System.out.println("Time:" + timeStampString);
			
			String timeDouble = Long.toString(getMillisFromGMT(timeStampString.substring(0, timeStampString.length() - 6))) 
					+ "." + (timeStampString.substring(timeStampString.length() - 6));
			double timeStamp = Double.parseDouble(timeDouble);
			long timeStampLong = (long) (timeStamp);
			
//			System.out.println(timeStampLong);
			if (sampleBeginTimestamp == 0) {
				sampleBeginTimestamp = timeStampLong; // initialize the first samplingtimestamp with the first line timestamp
			}
			
//			System.out.println("Timestamp:" + timeStampLong);
			
			System.out.println("SampleTimestamp:" + sampleBeginTimestamp);
			
			currentTimestamp = timeStampLong;
			
			if (currentTimestamp <= (sampleBeginTimestamp + interval)) {
				//System.out.println("In the same sample, current timestamp: " + currentTimestamp);
				if (syscallMap.containsKey(systemCall2)) {
//					System.out.println(systemCall2);
					syscallMap.put(systemCall2, syscallMap.get(systemCall2) + 1);
					numberOfSyscall++;
				}
			} 
			
			while (currentTimestamp > (sampleBeginTimestamp + interval)) {
				
//				System.out.println("Number of system call:" + numberOfSyscall);
//				System.out.println("Number of sample:" + numberOfSample);
				
				
				for (Entry<String, Integer> entry : syscallMap.entrySet()) {
					System.out.println(entry.getKey() + " : " + entry.getValue());
				}
				
				sampleBeginTimestamp = sampleBeginTimestamp + interval;
				
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(outputFile, true));
				
				String eachLine = Long.toString(sampleBeginTimestamp) + ",";
//				String eachLine = timeStampString + ",";
				
				for (Entry<String, Integer> entry : syscallMap.entrySet()) {
					eachLine = eachLine + entry.getValue() + ",";
				}
				
				eachLine = eachLine.substring(0, eachLine.length() - 1);
				
				bw2.write(eachLine);
				
				bw2.newLine();
				
				bw2.close();
				
				for (Entry<String, Integer> entry: syscallMap.entrySet()) {
					syscallMap.put(entry.getKey(), 0);
				}
				
				//System.out.println("Enter a new sample...");
				//System.out.println("*******************************");
				
				numberOfSample++;
				
//				System.out.println("SampleTimestamp:" + sampleBeginTimestamp);
			}
			
		}
		
		
		fr.close();
		fr2.close();
						
	}
	
	public static long getMillisFromGMT(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
		
	}

}
