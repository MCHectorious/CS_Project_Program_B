package DataImport.CourseListScrapers;

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
                officialName = officialName.substring(prefix.length());//Removes the prefix
            }
        }

        for (String phrase : phrasesToRemove) {
            if (officialName.contains(phrase)) {
                officialName = officialName.substring(0, officialName.indexOf(phrase)) + officialName.substring(officialName.indexOf(phrase) + phrase.length());//Removes the phrase
            }
        }

        if (officialName.contains("(")) {
            if (officialName.substring(officialName.lastIndexOf('(') + 1, officialName.lastIndexOf(')') - 1).matches("(Draft )?([0123456789])+")) {//Checks whether the between the brackets is a number, possibly directly after the word Draft
                try {
                    officialName = officialName.substring(0, officialName.lastIndexOf('(') - 1).concat(officialName.substring(officialName.lastIndexOf(')') + 1));
                } catch (IndexOutOfBoundsException e) {//If the last character is ')"
                    officialName = officialName.substring(0, officialName.lastIndexOf('(') - 1);
                }

            }
        }
        return officialName;
    }

    public ArrayList<String> getWebsitesForCourses() {
        ArrayList<String> relatedCourses = new ArrayList<>();//Initialises the output
        Document AQAQualificationsWebsite = null;

        try {
            AQAQualificationsWebsite = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the AQA website which contains the courses by qualification
            Elements linksForGCSECourses = AQAQualificationsWebsite.select("div[class=panelInner gcse-header]").select("a[href]");// Gets all the hyperlinks of the GCSE courses

            for (Element link : linksForGCSECourses) { //Runs through each GCSE course
                relatedCourses.add("AQA GCSE " + convertOfficialCourseNameToColloquialCourseName(link.text()));// Adds the course name
            }

        } catch (IOException exception) {//Will occur if the URl is malformed, etc
            System.out.println("Unable to access AQA GCSE courses");
            System.out.println(exception.getMessage());//Prints the error to find the cause
        }

        try {

            if (AQAQualificationsWebsite == null){//If there was an before
                AQAQualificationsWebsite = Jsoup.connect("http://www.aqa.org.uk/qualifications").timeout(1000000).get();//Loads the aqa website
            }

            Elements linksForALevelCourses = AQAQualificationsWebsite.select("div[class=panelInner as_and_a-level-header]").select("a[href]");// Gets all the hyperlinks of the AS and A-Level courses

            for (Element link : linksForALevelCourses) { //Runs through each course
                relatedCourses.add("AQA AS A-Level " + convertOfficialCourseNameToColloquialCourseName(link.text()));// Adds the course name
            }

        } catch (IOException exception) {//Will occur if the URl is malformed, etc
            System.out.println("Unable to access AQA A-Level courses");
            System.out.print(exception.getMessage());//Prints the error to find the cause
        }

        return relatedCourses;
    }

}
