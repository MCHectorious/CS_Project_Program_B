package dataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

interface RevisionWebsiteScraper {

    ArrayList<String> getRelatedCourses(ArrayList<String> courseSearchStrings);

    void getFlashcards(ArrayList<String> relatedCoursesURLs);

}
