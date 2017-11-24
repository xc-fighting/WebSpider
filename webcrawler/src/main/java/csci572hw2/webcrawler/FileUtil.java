package csci572hw2.webcrawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtil {

	private static File fetchFile;
	private static File visitFile;
	private static File urlFile;
	private static File reportFile;
	private static String dirPath="";
	public static void init() throws Exception{
		fetchFile=new File("output");
		if(fetchFile.exists()==false){
			fetchFile.mkdirs();
		}
		dirPath=fetchFile.getAbsolutePath();
		fetchFile=new File(dirPath+"/fetch_USA_TODAY.csv");
		if(fetchFile.exists()==false){
			fetchFile.createNewFile();
		}
		visitFile=new File(dirPath+"/visit_USA_TODAY.csv");
		if(visitFile.exists()==false){
			visitFile.createNewFile();
		}
		urlFile=new File(dirPath+"/url_USA_TODAY.csv");
		if(urlFile.exists()==false){
			urlFile.createNewFile();
		}
		reportFile=new File(dirPath+"/report.txt");
		if(reportFile.exists()==false){
			reportFile.createNewFile();
		}
		System.out.println("finish output file initializing");
		
	}
	
	public static void print(){
		System.out.println(fetchFile.getAbsolutePath());
		System.out.println(visitFile.getAbsolutePath());
		System.out.println(urlFile.getAbsolutePath());
	}
	
	public synchronized static void WriteToFetchFile(String input){
		try {
			RandomAccessFile rf=new RandomAccessFile(fetchFile,"rw");
			long fileLength=rf.length();
			rf.seek(fileLength);
			rf.writeChars(input+"\n");
			rf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized static void WriteToVistitFile(String input){
		try{
			RandomAccessFile rf=new RandomAccessFile(visitFile,"rw");
			long fileLength=rf.length();
			rf.seek(fileLength);
			rf.writeChars(input+"\n");
			rf.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public synchronized static void WriteToURLsFile(String input){
		try{
			RandomAccessFile rf=new RandomAccessFile(urlFile,"rw");
			long fileLength=rf.length();
			rf.seek(fileLength);
			rf.writeChars(input+"\n");
			rf.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void WriteToReportFile(){
		try{
			FileWriter rf=new FileWriter(reportFile);
			rf.write("Name: CHEN XU\n");
			rf.write("USC ID: 9012039827\n");
			rf.write("News site crawled: USA_TODAY\n\n");
			rf.write("Fetch Statistics\n");
			rf.write("=================\n");
			rf.write("#fetch attempt:"+MyCrawler.NumberOfFetchAttempted+"\n");
			rf.write("#fetch success:"+MyCrawler.NumberOfFetchSuccessed+"\n");
			rf.write("#fetch fail or abort:"+(MyCrawler.NumberOfFetchAttempted-MyCrawler.NumberOfFetchSuccessed)+"\n\n");
			
			rf.write("Outgoing URLS\n");
			rf.write("==============\n");
			rf.write("Total URLs extracted:"+MyCrawler.NumberOfAllURL+"\n");
			rf.write("#unique URLs extracted: "+(MyCrawler.NumOfDomainUnique+MyCrawler.NumOfExternalUnique)+"\n");
			rf.write("#unique URLs within News Site: "+MyCrawler.NumOfDomainUnique+"\n");
			rf.write("#unique URLs outside News Site: "+MyCrawler.NumOfExternalUnique+"\n\n");
			
			rf.write("Status Codes\n");
			rf.write("============\n");
			for(int key:MyCrawler.statusMap.keySet()){
				rf.write(key+":"+MyCrawler.statusMap.get(key)+"\n");
			}
			rf.write("File Sizes:\n");
			rf.write("==============\n");
			rf.write("< 1KB:"+MyCrawler.size1+"\n");
			rf.write("1KB ~ < 10KB:"+MyCrawler.size2+"\n");
			rf.write("10KB ~ < 100KB:"+MyCrawler.size3+"\n");
			rf.write("100KB ~ < 1MB:"+MyCrawler.size4+"\n");
			rf.write(">= 1MB:"+MyCrawler.size5+"\n\n");
			
			rf.write("Content Types:\n");
			rf.write("================\n");
			for(String key:MyCrawler.typeMap.keySet()){
				rf.write(key+":"+MyCrawler.typeMap.get(key)+"\n");
			}
			
			rf.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
