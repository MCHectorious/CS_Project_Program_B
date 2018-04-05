package dataImport;

public class MainScraping {

	public static void main(String[] args) {

		(new MemRiseScraper()).scrape(15);
		(new CramScraper()).scrape(30);
		
	}
}
