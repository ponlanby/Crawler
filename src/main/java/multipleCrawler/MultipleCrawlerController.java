package multipleCrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar
 */

public class MultipleCrawlerController {
    private static final Logger logger = LoggerFactory.getLogger(MultipleCrawlerController.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            logger.info("Needed parameter: ");
            logger.info("\t rootFolder (it will contain intermediate crawl data)");
            return;
        }

    /*
     * crawlStorageFolder is a folder where intermediate crawl data is
     * stored.
     */
        String crawlStorageFolder = args[0];

        CrawlConfig config1 = new CrawlConfig();
        CrawlConfig config2 = new CrawlConfig();
        CrawlConfig config3 = new CrawlConfig();
        CrawlConfig config4 = new CrawlConfig();

    /*
     * The two crawlers should have different storage folders for their
     * intermediate data
     */
        config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
        config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");
        config3.setCrawlStorageFolder(crawlStorageFolder + "/crawler3");
        config4.setCrawlStorageFolder(crawlStorageFolder + "/crawler4");

        config1.setPolitenessDelay(1000);
        config2.setPolitenessDelay(2000);
        config3.setPolitenessDelay(1000);
        config4.setPolitenessDelay(2000);

        config1.setMaxPagesToFetch(50);
        config2.setMaxPagesToFetch(100);
        config3.setMaxPagesToFetch(50);
        config4.setMaxPagesToFetch(100);

    /*
     * We will use different PageFetchers for the two crawlers.
     */
        PageFetcher pageFetcher1 = new PageFetcher(config1);
        PageFetcher pageFetcher2 = new PageFetcher(config2);
        PageFetcher pageFetcher3 = new PageFetcher(config3);
        PageFetcher pageFetcher4 = new PageFetcher(config4);

    /*
     * We will use the same RobotstxtServer for both of the crawlers.
     */
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

        CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
        CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);
        CrawlController controller3 = new CrawlController(config3, pageFetcher3, robotstxtServer);
        CrawlController controller4 = new CrawlController(config4, pageFetcher4, robotstxtServer);

        String[] crawler1Domains = {"http://www.autohome.com.cn/news/"};
        String[] crawler2Domains = {"http://www.autohome.com.cn/advice/"};
        String[] crawler3Domains = {"http://www.autohome.com.cn/drive/"};
        String[] crawler4Domains = {"http://www.autohome.com.cn/tech/"};

        controller1.setCustomData(crawler1Domains);
        controller2.setCustomData(crawler2Domains);
        controller3.setCustomData(crawler3Domains);
        controller4.setCustomData(crawler4Domains);

        controller1.addSeed("http://www.autohome.com.cn/use/136-1/#liststart");
        controller2.addSeed("http://diandong.autohome.com.cn/#pvareaid=103686");
        controller1.addSeed("http://diandong.autohome.com.cn/#pvareaid=103686");
        controller2.addSeed("http://diandong.autohome.com.cn/#pvareaid=103686");

    /*
     * The first crawler will have 5 concurrent threads and the second
     * crawler will have 7 threads.
     */
        controller1.startNonBlocking(BasicCrawler.class, 10);
        controller2.startNonBlocking(BasicCrawler.class, 10);
        controller3.startNonBlocking(BasicCrawler.class, 10);
        controller4.startNonBlocking(BasicCrawler.class, 10);

        controller1.waitUntilFinish();
        logger.info("Crawler 1 is finished.");

        controller2.waitUntilFinish();
        logger.info("Crawler 2 is finished.");
        
        controller3.waitUntilFinish();
        logger.info("Crawler 3 is finished.");

        controller4.waitUntilFinish();
        logger.info("Crawler 4 is finished.");
    }
}
