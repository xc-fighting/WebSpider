package csci572hw2.webcrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static void main(String[] args) throws Exception{
    	 String crawlStorageFolder = "C:\\Users\\xuchen\\Desktop\\WebSpider\\CrawlerData";
         int numberOfCrawlers = 7;

         FileUtil.init();
         FileUtil.print();
         CrawlConfig config = new CrawlConfig();
         config.setCrawlStorageFolder(crawlStorageFolder);
        
         config.setIncludeBinaryContentInCrawling(true);
         config.setMaxDepthOfCrawling(16);
         config.setMaxPagesToFetch(20000);

         /*
          * Instantiate the controller for this crawl.
          */
         PageFetcher pageFetcher = new PageFetcher(config);
         RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
         RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
         CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
         String[] domains={"https://www.usatoday.com/","http://www.usatoday.com/"};
         MyCrawler.configure(domains, "images");
         
         for(String domain:domains){
        	 controller.addSeed(domain);
         }
         
         controller.start(MyCrawler.class, numberOfCrawlers);
         
         FileUtil.WriteToReportFile();

    }
}
