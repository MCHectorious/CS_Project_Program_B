package dataImport.RevisionWebsiteScrapers;


import dataStructures.Flashcard;
import fileManipulation.DataExport;
import generalUtilities.Utilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

class CramScraper implements RevisionWebsiteScraper {

    @Override
    public ArrayList<String> getRelatedCourses(ArrayList<String> strings) {
        ArrayList<String> relatedWebsites = new ArrayList<>();
        for ( String string: strings) {

            String url = "http://www.cram.com/search?query=" + Utilities.convertToURLFormat(string)
                    + "&search_in%5B%5D=title&search_in%5B%5D=body&search_in%5B%5D=subject&search_in%5B%5D=username&image_filter=exclude_imgs&period=any";
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
            } catch (IOException exception) {
                System.out.println("Unable to access Cram courses");
                System.out.println(exception.getMessage());
            }
        }
        return relatedWebsites;
    }

    @Override
    public void getFlashcards(ArrayList<String> strings) {
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

