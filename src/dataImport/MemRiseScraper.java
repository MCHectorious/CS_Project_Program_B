package dataImport;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fileIO.DataExport;
import util.Util;
import dataStructures.Flashcard;

public class MemRiseScraper extends FlashcardScraper{

	public final static String WEBSITE_HEADER = "https://www.memrise.com";

	public void scrape() {scrape(10);}
	
	public void scrape(int numberOfWebsites) {
		String[] WebsiteList = getWebsiteList(numberOfWebsites);
		for(String website:WebsiteList) {
			scrapeIndividualWebsite(WEBSITE_HEADER + website);
		}
	}	 
	
	public String[] getWebsiteList(int numberOfWebsites) {
		ArrayList<String> output =  new ArrayList<String>();
		
		String[] CourseWebsites = new String[numberOfWebsites];
		Document GCSEsearchResults = GeneralWebScraping.connect("https://www.memrise.com/courses/english/?q=GCSE");
		Elements ResultsSection = GCSEsearchResults.select("div[class=row]").select("h3");
		
		for(int i=0;i<numberOfWebsites && i<ResultsSection.size();i++){
			CourseWebsites[i] = ResultsSection.select("a[class=inner]").get(i).attr("href") ;
		}
		
		for(String website:CourseWebsites){
			Document websiteForCourse = GeneralWebScraping.connect(WEBSITE_HEADER+website);
			Elements chapters = websiteForCourse.select("div[class=levels clearfix]").select("a[class=level clearfix]");
			for(Element e: chapters){ output.add(e.attr("href"));}
		}
		return output.toArray(new String[0]);
	}

	public void scrapeIndividualWebsite(String url) {
		Document doc = GeneralWebScraping.connect(url);
		Elements FlashCardSection = doc.select("div[class=thing text-text]");
		for (Element e: FlashCardSection){
			String front = e.select("div[class=col_a col text]").select("div[class=text]").first().text();
			String back = e.select("div[class=col_b col text]").select("div[class=text]").first().text();
			DataExport.appendToTextFile(Flashcard.withSep(front, back), "DataSets/RawFlashcards.txt");
			//System.out.println(Flashcard.withSep(front, back));
		}	
	}
	
	
	public static ArrayList<String> getRelatedMemRiseCourses(ArrayList<String> strings){
		ArrayList<String> relatedWebsites = new ArrayList<>();
		for ( String string: strings) {
            StringBuilder builder = new StringBuilder();
            builder.append("https://www.memrise.com/courses/english/?q=");
            builder.append(Util.convertToURLFormat(string));
            String courseColloquialName = "";
            if(string.contains("AQA GCSE ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA GCSE ")+9);
            }else if(string.contains("AQA AS A-Level ")) {
            	courseColloquialName = string.substring(string.indexOf("AQA AS A-Level ")+15);
            }
            courseColloquialName = courseColloquialName.toLowerCase();
            String url = builder.toString();
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
            } catch (Exception e) {
                System.out.print("Error"+e.getMessage());
            }
        }
		return relatedWebsites;
		
	}

	public static void getFlaschards(ArrayList<String> strings) {
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
                System.out.println("Error"+e.getMessage());        
            }

        }
	}


}
