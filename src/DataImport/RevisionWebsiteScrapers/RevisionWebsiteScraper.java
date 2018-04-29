package DataImport.RevisionWebsiteScrapers;

import java.util.ArrayList;

interface RevisionWebsiteScraper {

    ArrayList<String> getRelatedCourses(ArrayList<String> courseSearchStrings);//Returns the URL of related courses

    void getFlashcards(ArrayList<String> relatedCoursesURLs);//Inputs the flashcards from the related course to a text file

}
