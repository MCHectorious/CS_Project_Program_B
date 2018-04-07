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

class MemRiseScraper implements RevisionWebsiteScraper {

    @Override
    public ArrayList<String> getRelatedCourses(ArrayList<String> strings) {
		ArrayList<String> relatedWebsites = new ArrayList<>();
		for ( String string: strings) {

            String courseColloquialName = "";
            if(string.contains("AQA GCSE ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA GCSE ")+9);
            }else if(string.contains("AQA AS A-Level ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA AS A-Level ")+15);
            }
            courseColloquialName = courseColloquialName.toLowerCase();

            String url = "https://www.memrise.com/courses/english/?q=" + Utilities.convertToURLFormat(string);
            try {
                Document document = Jsoup.connect(url).timeout(1000000).get();
                Elements section = document.select("div[class=row]").select("div[class=course-box-wrapper col-xs-12 col-sm-6 col-md-4]");
                //Elements section  = document.select("div[class=col-sm-12 col-md-9]").select("div[class=course-box ]");
                for (Element element:section ) {
                    Element courseNameElement = element.select("a[class=inner]").first();
                    String courseName = courseNameElement.text().toLowerCase();
                    String categoryName = element.select("a[class=category]").first().text().toLowerCase();

                    if (courseName.contains(courseColloquialName) ||
                            categoryName.contains(courseColloquialName)){

                        String website = courseNameElement.attr("href");
                        if(website.length()>8){
                            if(website.substring(0,8).equals("/course/")){
                                
								relatedWebsites.add(website);
                                if (relatedWebsites.size()==5){
                                    break;
                                }
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
            	//System.out.println(url);
            	Document courseWebsite = Jsoup.connect("https://www.memrise.com"+url).timeout(1000000).get();
                Elements section = courseWebsite.select("div[class=levels clearfix]").select("a[href]");
                for (Element element: section) {
                    Document levelWebsite = Jsoup.connect("https://www.memrise.com"+element.attr("href")).get();
                    Elements informationSection = levelWebsite.select("div[class=things clearfix]").select("div[class=thing text-text]");
                    for(Element div:informationSection){
                        String front = div.select("div[class=col_a col text]").select("div[class=text]").first().text();
                        String back = div.select("div[class=col_b col text]").select("div[class=text]").first().text();
            			DataExport.appendToTextFile(Flashcard.withSep(front, back), "DataSets/RawFlashcards.txt");
            			//System.out.println(Flashcard.withSep(front, back));

            			
                    }

                }
            } catch (Exception e) {
                System.out.println(url);
                System.out.println("Error" + e.getMessage());
            }

        }
	}


}
