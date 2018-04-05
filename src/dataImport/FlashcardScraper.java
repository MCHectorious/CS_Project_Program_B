package dataImport;


public class FlashcardScraper {

    static private String WEBSITE_HEADER;
    public void scrape() {scrape(10);}
    public void scrape(int numberOfWebsites) {
        String[] WebsiteList = getWebsiteList(numberOfWebsites);
        for(String website:WebsiteList) {
            scrapeIndividualWebsite(WEBSITE_HEADER + website);
        }
    }
    private String[] getWebsiteList(int numberOfWebsites) {return null;};
    private void scrapeIndividualWebsite(String url) {};

}

