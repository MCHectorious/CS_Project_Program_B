package dataImport;


import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fileIO.DataExport;
import util.Util;
import dataStructures.Flashcard;

public class CramScraper extends FlashcardScraper{

    private final static String WEBSITE_HEADER = "http://www.cram.com";

    public void scrape() {scrape(10);}

    public void scrape(int numberOfWebsites) {
        String[] WebsiteList = getWebsiteList(numberOfWebsites);
        for(String website:WebsiteList) {
            scrapeIndividualWebsite(WEBSITE_HEADER + website);
        }
    }

    private String[] getWebsiteList(int numberOfWebsites) {
        ArrayList<String> output = new ArrayList<String>();

        Document GCSEsearchResults = GeneralWebScraping.connect("http://www.cram.com/search?query=GCSE+AQA&search_in%5B%5D=title&search_in%5B%5D=body&search_in%5B%5D=subject&search_in%5B%5D=username&image_filter=exclude_imgs&period=any");
        Document GCSEsearchResultsPage2 = GeneralWebScraping.connect("http://www.cram.com/search?query=GCSE+AQA&search_in%5B0%5D=title&search_in%5B1%5D=body&search_in%5B2%5D=subject&search_in%5B3%5D=username&period=any&image_filter=exclude_imgs&page=2");
        Document GCSEsearchResultsPage3 = GeneralWebScraping.connect("http://www.cram.com/search?query=GCSE+AQA&search_in%5B0%5D=title&search_in%5B1%5D=body&search_in%5B2%5D=subject&search_in%5B3%5D=username&period=any&image_filter=exclude_imgs&page=3");
        Document GCSEsearchResultsPage4 = GeneralWebScraping.connect("http://www.cram.com/search?query=GCSE+AQA&search_in%5B0%5D=title&search_in%5B1%5D=body&search_in%5B2%5D=subject&search_in%5B3%5D=username&period=any&image_filter=exclude_imgs&page=4");


        Elements ResultsSection = GCSEsearchResults.select("div[id=searchResults]").select("li");
        ResultsSection.addAll(GCSEsearchResultsPage2.select("div[id=searchResults]").select("li"));
        ResultsSection.addAll(GCSEsearchResultsPage3.select("div[id=searchResults]").select("li"));
        ResultsSection.addAll(GCSEsearchResultsPage4.select("div[id=searchResults]").select("li"));



        for(Element element: ResultsSection){
            String link = element.select("a[itemprop=significantLinks]").attr("href");
            output.add(link);
        }

        return output.toArray(new String[0]);
    }

    private void scrapeIndividualWebsite(String url) {
        Document doc = GeneralWebScraping.connect(WEBSITE_HEADER+url);
        Elements FlashCardSection = doc.select("table[class=flashCardsListingTable]").select("tr");
        for (Element e: FlashCardSection){
            String front = e.select("div[class=front_text card_text]").text();
            String back = e.select("div[class=back_text card_text]").text();
            DataExport.appendToTextFile(Flashcard.withSep(front, back), "DataSets/RawFlashcards.txt");
            //System.out.println(Flashcard.withSep(front, back));
        }
    }

    public static ArrayList<String> getRelatedCramCourses(ArrayList<String> strings){
        ArrayList<String> relatedWebsites = new ArrayList<>();
        for ( String string: strings) {

            StringBuilder builder = new StringBuilder();
            builder.append("http://www.cram.com/search?query=");
            builder.append(Util.convertToURLFormat(string));
            builder.append("&search_in%5B%5D=title&search_in%5B%5D=body&search_in%5B%5D=subject&search_in%5B%5D=username&image_filter=exclude_imgs&period=any");

            String url = builder.toString();
            //System.out.println(url);
            try {
                Document document = Jsoup.connect(url).timeout(1000000).get();
                Elements section = document.select("div[id=searchResults]").select("a[href]");

                for (Element element:section ) {
                    String website = element.attr("href");
                    if (website.length()>12){
                        if(website.substring(0,12).equals("/flashcards/")){


                            relatedWebsites.add(website);
                            if (relatedWebsites.size()==5){
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error"+e.getMessage());
                //System.exit(0);
            }
        }
        return relatedWebsites;
    }

    public static void getFlashcards(ArrayList<String> strings) {
        for (String url: strings) {
            try {
                //System.out.println("http://www.cram.com"+url);
                Document courseWebsite = Jsoup.connect("http://www.cram.com"+url).timeout(1000000).get();
                Elements FlashCardSection = courseWebsite.select("table[class=flashCardsListingTable]").select("tr");
                for (Element e: FlashCardSection){
                    String front = e.select("div[class=front_text card_text]").text();
                    String back = e.select("div[class=back_text card_text]").text();
                    DataExport.appendToTextFile(Flashcard.withSep(front, back), "DataSets/RawFlashcards.txt");
                    //System.out.println(Flashcard.withSep(front, back));

                }
            } catch (IOException e) {
                System.out.println(url);
                System.out.println("Error"+e.getMessage());

            }

        }
    }




}

