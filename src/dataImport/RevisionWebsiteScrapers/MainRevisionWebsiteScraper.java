package dataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

public class MainRevisionWebsiteScraper {

    public static void scrapeRevisionWebsites(ArrayList<String> courses) {
        CramScraper cramScraper = new CramScraper();
        ArrayList<String> cramCourses = cramScraper.getRelatedCourses(courses);
        cramScraper.getFlashcards(cramCourses);

        MemriseScraper memriseScraper = new MemriseScraper();
        ArrayList<String> memriseCourses = memriseScraper.getRelatedCourses(courses);
        memriseScraper.getFlashcards(memriseCourses);

    }

}
