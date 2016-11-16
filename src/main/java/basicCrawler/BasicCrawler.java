package basicCrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Yasser Ganjisaffar
 */
public class BasicCrawler extends WebCrawler {

    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(
    		".*(\\.(php|css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("http://www.med66.com/");
//        return true;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
//        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
//        String domain = page.getWebURL().getDomain();
//        String path = page.getWebURL().getPath();
//        String subDomain = page.getWebURL().getSubDomain();
//        String parentUrl = page.getWebURL().getParentUrl();
//        String anchor = page.getWebURL().getAnchor();

//        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
//        logger.debug("Domain: '{}'", domain);
//        logger.debug("Sub-domain: '{}'", subDomain);
//        logger.debug("Path: '{}'", path);
//        logger.debug("Parent page: {}", parentUrl);
//        logger.debug("Anchor text: {}", anchor);
        
        String storagePath = "C:\\Users\\Administrator\\Desktop\\crawler\\single\\storage\\";

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String htmlText = htmlParseData.getHtml();            
            Document doc = Jsoup.parse(htmlText);
            Elements contents = doc.select("div[id=fontzoom]");            
            
            String text = null;
            for(Element elm:contents){
            	text = elm.text();
            }
            String title = htmlParseData.getTitle();
            
//            contents = doc.select("h2.articTit");
//            String title = null;
//            for(Element elm:contents){
//            	title = elm.text();
//            }
            title = title.replace("|", " ");
            title = title.replace("\\", " ");
            title = title.replace("/", " ");
            title = title.replace("?", " ");
//            String html = htmlParseData.getHtml();
//            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            
            String filePath = storagePath + title + ".txt";
            File textFile = new File(filePath);
            if(text!=null && title!=null){
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
            
            
            
//            logger.info(title);
//            logger.info(text);
//            logger.debug("Text length: {}", text.length());
//            logger.debug("Html length: {}", html.length());
//            logger.debug("Number of outgoing links: {}", links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            logger.debug("Response headers:");
            for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }

        logger.debug("=============");
    }
}