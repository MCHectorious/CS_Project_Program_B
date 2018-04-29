package DataImport;

import DataImport.CourseListScrapers.AQACourseListScraper;
import DataImport.RevisionWebsiteScrapers.MainRevisionWebsiteScraper;

import java.util.ArrayList;


public class GetDataMain {

    public static void main(String[] args) {
        System.out.println("Getting Courses");
        ArrayList<String> courses = new AQACourseListScraper().getWebsitesForCourses();//Gets courses from AQA
        System.out.println("Getting Flashcards");
        MainRevisionWebsiteScraper.scrapeRevisionWebsites(courses);//Saves the flashcards from relevant courses for the AQA courses

    }


}

