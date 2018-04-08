package dataImport.CourseListScrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AQACourseListScraper implements CourseListScraper {

    private static String convertOfficialCourseNameToColloquialCourseName(String officialName) {
        String[] prefixesToRemove = {"AS and A-level", "A-level ", "GCSE", "GCSE ", "AS", "AS "};
        String[] phrasesToRemove = {"New ", "New"};
        for (String prefix : prefixesToRemove) {
            if (officialName.startsWith(prefix)) {
                officialName = officialName.substring(prefix.length());
            }
        }
        for (String phrase : phrasesToRemove) {
            if (officialName.contains(phrase)) {
                officialName = officialName.substring(0, officialName.indexOf(phrase)) + officialName.substring(officialName.indexOf(phrase) + phrase.length());
            }
        }

        if (officialName.contains("(")) {
            if (officialName.substring(officialName.lastIndexOf('(') + 1, officialName.lastIndexOf(')') - 1).matches("(Draft )*([0123456789])+")) {
                try {
                    officialName = officialName.substring(0, officialName.lastIndexOf('(') - 1).concat(officialName.substring(officialName.lastIndexOf(')') + 1));
                } catch (IndexOutOfBoundsException e) {
                    officialName = officialName.substring(0, officialName.lastIndexOf('(') - 1);
                }

            }
        }
        return officialName;
    }

    public ArrayList<String> getWebsitesForCourses() {
        ArrayList<String> courses = new ArrayList<>();
        Document AQAQualificationWebsite = null;
        try {
            AQAQualificationWebsite = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the aqa website
            Elements GCSELinks = AQAQualificationWebsite.select("div[class=panelInner gcse-header]").select("a[href]");// Gets all the hyperlinks of the relevant courses
            for (Element link : GCSELinks) { //Runs through each course
                courses.add("AQA GCSE " + convertOfficialCourseNameToColloquialCourseName(link.text()));// Adds the course name
            }
        } catch (IOException exception) {
            System.out.println("Unable to access AQA GCSE courses");
            System.out.print(exception.getMessage());
        }
        try {
            if (AQAQualificationWebsite == null){
                AQAQualificationWebsite = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the aqa website
            }
            Elements ALevelLinks = AQAQualificationWebsite.select("div[class=panelInner as_and_a-level-header]").select("a[href]");// Gets all the hyperlinks of the relevant courses
            for (Element link : ALevelLinks) { //Runs through each course
                courses.add("AQA AS A-Level " + convertOfficialCourseNameToColloquialCourseName(link.text()));// Adds the course name
            }
        } catch (IOException exception) {
            System.out.println("Unable to access AQA A-Level courses");
            System.out.print(exception.getMessage());
        }

        return courses;
    }

}
