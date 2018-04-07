package dataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

interface RevisionWebsiteScraper {

    ArrayList<String> getRelatedCourses(ArrayList<String> courses);

    void getFlashcards(ArrayList<String> urls);

}
