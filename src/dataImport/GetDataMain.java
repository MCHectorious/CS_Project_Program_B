package dataImport;

import dataImport.CourseListScrapers.AQACourseListScraper;
import dataImport.RevisionWebsiteScrapers.MainRevisionWebsiteScraper;

import java.util.ArrayList;


public class GetDataMain {

    public static void main(String[] args) {
        System.out.println("Getting Courses");
        ArrayList<String> courses = new AQACourseListScraper().getWebsitesForCourses();
        System.out.println("Getting Flashcards");
        MainRevisionWebsiteScraper.scrapeRevisionWebsites(courses);

    }


}

