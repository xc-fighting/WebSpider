package csci572hw2.webcrawler;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;



import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;


import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{

	public static int NumberOfFetchAttempted=0;
	public static int NumberOfFetchSuccessed=0;
	
	public static int NumberOfAllURL=0;
	public static Map<Integer,Integer> statusMap=new HashMap<Integer,Integer>(); 
	public static Map<String,Integer> typeMap=new HashMap<String,Integer>();
	public static int size1=0;
	public static int size2=0;
	public static int size3=0;
	public static int size4=0;
	public static int size5=0;
	
	public static Set<Integer> domainSets=new HashSet<Integer>();
	public static Set<Integer> externalSets=new HashSet<Integer>();
	public static int NumOfDomainUnique=0;
	public static int NumOfExternalUnique=0;
	
	 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"+
        "|rm|smil|wmv|swf|wma|zip|rar|gz|txt))$");
//	 private final static Pattern visitPatterns=Pattern.compile( ".*(\\.(doc|pdf|bmp|gif|jpe?g|png|tiff?))$");
     

	 private static String[] crawlDomains;
	
	 
	 public static void configure(String[] domain,String folderName){
		 crawlDomains=domain;
		 typeMap.put("text/html",0);
		 typeMap.put("image/gif",0);
		 typeMap.put("image/jpeg",0);
		 typeMap.put("image/png",0);
		 typeMap.put("application/pdf",0);
		
		
	 }
	 @Override
	  protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		   System.out.println("handling all of the url it want to fetch");
		   if(statusMap.containsKey(statusCode)==false){
			   statusMap.put(statusCode, 0);
		   }
		   statusMap.put(statusCode,statusMap.get(statusCode)+1);
		   NumberOfFetchAttempted++;
		   String input=webUrl.getURL()+","+statusCode;
		   System.out.println(input);
		   FileUtil.WriteToFetchFile(input);
	   }

		@Override
		public boolean shouldVisit(Page referringPage, WebURL url) {
			System.out.println("judging for the validation of current page's children");
			NumberOfAllURL++;
			String href = url.getURL();
			int code=href.hashCode();
			String input="";
			for(String domain:crawlDomains){
				//judge whether the url inside the domain
				//if yes
		    	if(href.startsWith(domain)){
		    		//if the url inside the domain,then judege if it is valid for visit
		    		if(domainSets.contains(code)==false){
		    			NumOfDomainUnique++;
		    			domainSets.add(code);
		    		}
		    		input=url+","+"OK";
		    		FileUtil.WriteToURLsFile(input);
		    		if(FILTERS.matcher(href).matches()){
				    	return false;
				    }
		    		else{
		    			return true;
		    		}
		    		
		    	}
		    }
		    //for the condistion when the url is not inside the domain
			input=url+","+"N_OK";
			FileUtil.WriteToURLsFile(input);
			if(externalSets.contains(code)==false){
				NumOfExternalUnique++;
				externalSets.add(code);
			}
		    return false;
		}
		
		/**
		* This function is called when a page is fetched and ready
		* to be processed by your program.
		*/
		@Override
		public void visit(Page page)  {
			System.out.println("visiting page in queue");
			NumberOfFetchSuccessed++;
			String url = page.getWebURL().getURL();
			int NumberOfLinks=page.getParseData().getOutgoingUrls().size();
			
			String type=page.getContentType();
			if(type.startsWith("text/html")){
				typeMap.put("text/html",typeMap.get("text/html")+1);
			}
			else{
				typeMap.put(type,typeMap.get(type)+1);
			}
			int contentLength=page.getContentData().length;
			if(contentLength<1024){
				size1++;
			}
			else if(contentLength>=1024 && contentLength<10240){
				size2++;
			}
			else if(contentLength>=10240 && contentLength<102400){
				size3++;
			}
			else if(contentLength>=102400 && contentLength<1048576){
				size4++;
			}
			else{
				size5++;
			}
			String input=url+","+contentLength+","+NumberOfLinks+","+type;
			FileUtil.WriteToVistitFile(input);
		
		}

}
