package dataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

public class MainRevisionWebsiteScraper {

    public static void scrapeRevisionWebsites(ArrayList<String> courses) {
        CramScraper cramScraper = new CramScraper();
        ArrayList<String> cramCourses = cramScraper.getRelatedCourses(courses);//Gets related Cram courses
        cramScraper.getFlashcards(cramCourses);//Gets the flashcards from relevant Cram courses

        MemriseScraper memriseScraper = new MemriseScraper();
        ArrayList<String> memriseCourses = memriseScraper.getRelatedCourses(courses);//Gets related Memrise courses
        memriseScraper.getFlashcards(memriseCourses);//Gets the flashcards from relevant Memrise courses

    }

}
