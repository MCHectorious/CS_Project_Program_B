package dataImport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GeneralWebScraping {
	public static Document connect(String url) {
		Document doc = null;
		try{
			doc = Jsoup.connect(url).get();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}
