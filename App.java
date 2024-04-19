import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;




public class App {
   public static void main(String[] args) throws
           InterruptedException {
    //    long updateTimeMillis = TimeUnit.HOURS.toMillis(1);
       String[] motherUrls = {
               "https://www.springeropen.com/search?query=blockchain&searchType=publisherSearch"


       };


       try (FileWriter writer = new FileWriter("blockchain.csv", true)) {
           writer.write("\"Link\",\"Website\",\"Title\",\"Description\",\"Author\",\"Date\",\"Type\",\"Keywords\"\n");


           for (String motherUrl : motherUrls) {
               Set<String> childUrls = new HashSet<>(); // Initialize for each mother page


               boolean hasNextPage = true;
               while (hasNextPage) {
                   Document motherDoc = Jsoup.connect(motherUrl)
                           .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                           .get();


                   Elements articleLinks = motherDoc.select("a[itemprop=url][data-test=title-link]");
                   for (Element link : articleLinks) {
                       String childUrl = link.attr("abs:href");
                       childUrls.add(childUrl);
                   }


                   Elements nextElements = motherDoc.select("a.c-pagination__link[rel=next]");
                   if (!nextElements.isEmpty()) {
                       Element nextElement = nextElements.first();
                       motherUrl = nextElement.attr("abs:href");
                   } else {
                       hasNextPage = false;
                   }
               }


               for (String childUrl : childUrls) {
                   Document childDoc = Jsoup.connect(childUrl).get();
                   String site = escapeCsv(childDoc.select("meta[name=citation_publisher]").attr("content"));
                   String title = escapeCsv(childDoc.select("meta[name=citation_title]").attr("content"));
                   String description = escapeCsv(childDoc.select("meta[property=og:description]").attr("content"));
                   String publicationDate = escapeCsv(childDoc.select("meta[name=citation_publication_date]").attr("content"));
                   String author = escapeCsv(childDoc.select("meta[name=citation_author]").attr("content"));
                   String type = escapeCsv(childDoc.select("meta[name=citation_article_type]").attr("content"));
                   final String tag="";


                   writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", childUrl, site, title, description, author,publicationDate,  type,tag));


               }
           }
           System.out.println("Scraping done. Check scidirect.csv for the data.");
       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   private static String escapeCsv(String text) {
       return text.replace("\"", "\"\"");
   }
}