package dataImport;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetAllFlashcardsMain {

	public static void main(String[] args) {
		System.out.println("ילד");
		System.out.println("Getting Courses");
		ArrayList<String> courses = getCourses();
		System.out.println("Getting MemRise Courses");
		ArrayList<String> MemRiseCourses = MemRiseScraper.getRelatedMemRiseCourses(courses);
		System.out.println("Getting MemRise Flashcards");
		MemRiseScraper.getFlaschards(MemRiseCourses);
		System.out.println("Getting Cram Courses");
		ArrayList<String> CramCourses = CramScraper.getRelatedCramCourses(courses);
		System.out.println("Getting Cram Flashcards");
		CramScraper.getFlashcards(CramCourses);
	}
	
	public static ArrayList<String> getCourses(){
		ArrayList<String> output = new ArrayList<>();
		try{
            Document document = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the aqa website
            Elements links = document.select("div[class=panelInner gcse-header]").select("a[href]");// Gets all the hyperlinks of the relevant courses
            for(Element element: links){ //Runs through each course
            	output.add("AQA GCSE "+convertOfficialCourseNameToColloquialCourseName(element.text())  );// Adds the course name
            }
        }catch (IOException exception){//TODO: improve
            System.out.println("Error: A IOException occurred");// Displays that an error has occurred
        }
		try{
            Document document = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the aqa website
            Elements links = document.select("div[class=panelInner as_and_a-level-header]").select("a[href]");// Gets all the hyperlinks of the relevant courses
            for(Element element: links){ //Runs through each course
            	output.add("AQA AS A-Level "+convertOfficialCourseNameToColloquialCourseName(element.text()));// Adds the course name
            }
        }catch (IOException exception){//TODO: improve
            System.out.println("Error: A IOException occurred");// Displays that an error has occurred
        }
		
		
		return output;
	}
	
    public static String convertOfficialCourseNameToColloquialCourseName(String text){
        String[] prefixesToRemove = {"AS and A-level","A-level ", "GCSE", "GCSE ","AS","AS "};
        String[] phrasesToRemove = {"New ", "New"};
        for (String s:prefixesToRemove){
            if(text.startsWith(s)){
                text = text.substring(s.length());
            }
        }
        for (String s:phrasesToRemove){
            if(text.contains(s)){
                text = text.substring(0, text.indexOf(s)) + text.substring(text.indexOf(s)+s.length());
            }
        }

        if(text.contains("(")){
            if(text.substring(text.lastIndexOf('(')+1,text.lastIndexOf(')')-1).matches("(Draft )*([0123456789])+")){
                try{
                    text = text.substring(0,text.lastIndexOf('(')-1).concat(text.substring(text.lastIndexOf(')')+1));
                }catch (Exception e){
                    text = text.substring(0,text.lastIndexOf('(')-1);
                }

            }
        }
        return text;
    }

}

