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
    public ArrayList<String> getRelatedCourses(ArrayList<String> courseSearchStrings) {
        ArrayList<String> relatedCourses = new ArrayList<>();
        for ( String courseSearchString: courseSearchStrings) {

            String URL = "http://www.cram.com/search?query=" + Utilities.convertToURLFormat(courseSearchString)
                    + "&search_in%5B%5D=title&search_in%5B%5D=body&search_in%5B%5D=subject&search_in%5B%5D=username&image_filter=exclude_imgs&period=any";
            try {
                Document searchResultsWebsite = Jsoup.connect(URL).timeout(1000000).get();
                Elements searchResultsSection = searchResultsWebsite.select("div[id=searchResults]").select("a[href]");

                for (Element searchResult:searchResultsSection ) {
                    String courseWebsite = searchResult.attr("href");
                    if (courseWebsite.length()>12){
                        if(courseWebsite.substring(0,12).equals("/flashcards/")){


                            relatedCourses.add(courseWebsite);
                            if (relatedCourses.size()==5){
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
        return relatedCourses;
    }

    @Override
    public void getFlashcards(ArrayList<String> relatedCoursesURLs) {
        for (String relatedCourseURL: relatedCoursesURLs) {
            try {
                //System.out.println("http://www.cram.com"+relatedCourseURL);
                Document courseWebsite = Jsoup.connect("http://www.cram.com"+relatedCourseURL).timeout(1000000).get();
                Elements flashcardSection = courseWebsite.select("table[class=flashCardsListingTable]").select("tr");
                for (Element flashcard: flashcardSection){
                    String flashcardFront = flashcard.select("div[class=front_text card_text]").text();
                    String flashcardBack = flashcard.select("div[class=back_text card_text]").text();
                    DataExport.appendToTextFile(Flashcard.withSeparator(flashcardFront, flashcardBack), "DataSets/RawFlashcards.txt");
                    //System.out.println(Flashcard.withSeparator(flashcardFront, flashcardBack));

                }
            } catch (IOException e) {
                System.out.println(relatedCourseURL);
                System.out.println("Error"+e.getMessage());

            }

        }
    }




}

