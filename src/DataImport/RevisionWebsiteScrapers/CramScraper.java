package DataImport.RevisionWebsiteScrapers;


import DataStructures.Flashcard;
import FileManipulation.DataExport;
import GeneralUtilities.Utilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

class CramScraper implements RevisionWebsiteScraper {

    @Override
    public ArrayList<String> getRelatedCourses(ArrayList<String> courseSearchStrings) {
        ArrayList<String> relatedCourses = new ArrayList<>();//Initialises the output

        for ( String courseSearchString: courseSearchStrings) {

            String URL = "http://www.cram.com/search?query=" + Utilities.convertToURLFormat(courseSearchString)
                    + "&search_in%5B%5D=title&search_in%5B%5D=body&search_in%5B%5D=subject&search_in%5B%5D=username&image_filter=exclude_imgs&period=any";
            //The URL used to search Cram for the course

            try {
                Document searchResultsWebsite = Jsoup.connect(URL).timeout(1000000).get();//Tries to connect to the website for the search
                Elements searchResultsSection = searchResultsWebsite.select("div[id=searchResults]").select("a[href]");//Gets the part of the page with the search results

                for (Element searchResult:searchResultsSection ) {
                    String courseWebsite = searchResult.attr("href");

                    if (courseWebsite.length()>12){
                        if(courseWebsite.substring(0,12).equals("/flashcards/")){//Checks whether it is link to a Cram course

                            relatedCourses.add(courseWebsite);//Adds to output
                            if (relatedCourses.size()==5){//Stops after 5 courses
                                break;
                            }

                        }
                    }

                }

            } catch (IOException exception) {//For example caused by a malformed URL
                System.out.println("Unable to access Cram courses");
                System.out.println(exception.getMessage());//To help find the source of the error
            }

        }
        return relatedCourses;
    }

    @Override
    public void getFlashcards(ArrayList<String> relatedCoursesURLs) {
        for (String relatedCourseURL: relatedCoursesURLs) {
            try {
                Document courseWebsite = Jsoup.connect("http://www.cram.com"+relatedCourseURL).timeout(1000000).get();//Connects to the websites for the Cram courses
                Elements flashcardSection = courseWebsite.select("table[class=flashCardsListingTable]").select("tr");//Gets the section of the page with the flashcards

                for (Element flashcard: flashcardSection){
                    String flashcardFront = flashcard.select("div[class=front_text card_text]").text();//Gets the front of the flashcard
                    String flashcardBack = flashcard.select("div[class=back_text card_text]").text();//Gets the back of the flashcard
                    DataExport.appendToTextFile(Flashcard.withSeparator(flashcardFront, flashcardBack), "DataSets/RawFlashcards.txt");//Appends the flashcard to the appropriate text file
                }

            } catch (IOException e) {//Can be caused by a malformed URL
                System.out.println(relatedCourseURL);
                System.out.println("Error"+e.getMessage());//To help the cause of the error
            }

        }
    }

}

