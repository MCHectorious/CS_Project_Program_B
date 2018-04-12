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

class MemriseScraper implements RevisionWebsiteScraper {

    @Override
    public ArrayList<String> getRelatedCourses(ArrayList<String> courseSearchStrings) {
		ArrayList<String> relatedWebsites = new ArrayList<>();//Initialises the output
		for ( String string: courseSearchStrings) {

            String courseColloquialName = "";
            if(string.contains("AQA GCSE ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA GCSE ")+9);
            }else if(string.contains("AQA AS A-Level ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA AS A-Level ")+15);
            }
            courseColloquialName = courseColloquialName.toLowerCase();

            String url = "https://www.memrise.com/courses/english/?q=" + Utilities.convertToURLFormat(string);//Generatess the url of the search website
            try {
                Document searchResultWebsite = Jsoup.connect(url).timeout(1000000).get();//Gets the website wuth search result
                Elements section = searchResultWebsite.select("div[class=row]").select("div[class=course-box-wrapper col-xs-12 col-sm-6 col-md-4]");//gets the section of the website with the  search results
                for (Element element:section ) {
                    Element courseNameElement = element.select("a[class=inner]").first();
                    String courseName = courseNameElement.text().toLowerCase();//gets the name of the course
                    String categoryName = element.select("a[class=category]").first().text().toLowerCase();//gets the name of the category the course is in

                    if (courseName.contains(courseColloquialName) ||
                            categoryName.contains(courseColloquialName)){ //The course only is relevant if its name or category name includes the colloquial anme

                        String website = courseNameElement.attr("href");
                        if(website.length()>8){
                            if(website.substring(0,8).equals("/course/")){//checks if the link is to a course
                                
								relatedWebsites.add(website);//Adds the course website to the output
                                if (relatedWebsites.size()==5){//Stops when 5 relevant courses have been found
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException exception) {//Can be caused by a malformed URl
                System.out.println("Unable to access Cram courses");
                System.out.println(exception.getMessage());//To help find the cause of the error
            }
        }
		return relatedWebsites;
		
	}

    @Override
    public void getFlashcards(ArrayList<String> relatedCoursesURLs) {
		for (String url: relatedCoursesURLs) {
            try {
            	Document courseWebsite = Jsoup.connect("https://www.memrise.com"+url).timeout(1000000).get();//Gets website for the related Memrise course
                Elements section = courseWebsite.select("div[class=levels clearfix]").select("a[href]");//Gets the part of the page with the levels
                for (Element element: section) {
                    Document levelWebsite = Jsoup.connect("https://www.memrise.com"+element.attr("href")).get();
                    Elements informationSection = levelWebsite.select("div[class=things clearfix]").select("div[class=thing text-text]");//Gets the part of page with flashcards
                    for(Element div:informationSection){
                        String front = div.select("div[class=col_a col text]").select("div[class=text]").first().text();//Gets the front of the flashcard
                        String back = div.select("div[class=col_b col text]").select("div[class=text]").first().text();//Gets the back of the flashcard
            			DataExport.appendToTextFile(Flashcard.withSeparator(front, back), "DataSets/RawFlashcards.txt");//Saves the flashcard to the text file
                    }
                }
            } catch (IOException e) {
                System.out.println(url);
                System.out.println("Error" + e.getMessage());//To help find the cause of the error
            }

        }
	}


}
