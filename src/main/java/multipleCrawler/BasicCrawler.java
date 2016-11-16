package multipleCrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */
public class BasicCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(BasicCrawler.class);

    private static final Pattern FILTERS = Pattern.compile(
        ".*(\\.(php|css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
        "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private String[] myCrawlDomains;

    @Override
    public void onStart() {
        myCrawlDomains = (String[]) myController.getCustomData();
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        for (String crawlDomain : myCrawlDomains) {
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        String storagePath = "C:\\Users\\Administrator\\Desktop\\crawler\\multiple\\storage2\\";

        logger.info("URL: {}", url);

        if (page.getParseData() instanceof HtmlParseData) {
        	HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String htmlText = htmlParseData.getHtml();            
            Document doc = Jsoup.parse(htmlText);
            
            Elements contents = doc.select("div.article-content");            
            
            String text = null;
            for(Element elm:contents){
            	text = elm.text();
            }
            String title = htmlParseData.getTitle();
            title = title.replace("|", " ");
            title = title.replace("\\", " ");
            title = title.replace("/", " ");
            title = title.replace("?", " ");
            String filePath = storagePath + title + ".txt";
            File textFile = new File(filePath);
            if(text!=null){
            	try {
                	
                	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(textFile), "UTF-8");
//    				FileWriter out = new FileWriter(textFile);
    				out.write(text);
    				out.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            }
        }

        logger.debug("=============");
    }
}