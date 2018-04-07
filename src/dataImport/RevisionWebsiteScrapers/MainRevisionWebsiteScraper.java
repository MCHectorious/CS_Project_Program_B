package dataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

public class MainRevisionWebsiteScraper {

    public static void scrapeRevisionWebsites(ArrayList<String> courses) {
        CramScraper cramScraper = new CramScraper();
        ArrayList<String> cramCourses = cramScraper.getRelatedCourses(courses);
        cramScraper.getFlashcards(cramCourses);

        MemRiseScraper memRiseScraper = new MemRiseScraper();
        ArrayList<String> memriseCourses = memRiseScraper.getRelatedCourses(courses);
        memRiseScraper.getFlashcards(memriseCourses);

    }

}
