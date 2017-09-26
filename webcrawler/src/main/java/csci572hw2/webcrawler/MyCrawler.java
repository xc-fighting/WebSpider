package csci572hw2.webcrawler;

import java.io.File;

import java.util.UUID;
import java.util.regex.Pattern;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;

import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{

	
	
	 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"+
        "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	 private final static Pattern imgPatterns=Pattern.compile( ".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
     
	 private static File storageFolder;
	 private static String[] crawlDomains;
	 
	 public static void configure(String[] domain,String folderName){
		 crawlDomains=domain;
		 storageFolder=new File(folderName);
		 if(storageFolder.exists()==false){
			 storageFolder.mkdirs();
		 }
	 }

		@Override
		public boolean shouldVisit(Page referringPage, WebURL url) {
			String href = url.getURL().toLowerCase();
		    if(FILTERS.matcher(href).matches()){
		    	return false;
		    }
		    if(imgPatterns.matcher(href).matches()){
		    	return true;
		    }
		    for(String domain:crawlDomains){
		    	if(href.startsWith(domain)){
		    		return true;
		    	}
		    }
		    return false;
		}
		
		/**
		* This function is called when a page is fetched and ready
		* to be processed by your program.
		*/
		@Override
		public void visit(Page page)  {
			String url = page.getWebURL().getURL();
			System.out.println("URL: " + url);
			
			if(imgPatterns.matcher(url).matches()==false||
					!(page.getParseData() instanceof BinaryParseData)||
					page.getContentData().length<(10*1024)){
				return;
			}
			
			String extension=url.substring(url.lastIndexOf("."));
			String hashedName=UUID.randomUUID()+extension;
			
			String filename=storageFolder.getAbsolutePath()+"/"+hashedName;
			try{
				Files.write(page.getContentData(), new File(filename));
				System.out.println(hashedName+":"+"stored");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

}
