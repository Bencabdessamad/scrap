package com.example.scrap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnnouncementScraper {

    private static Set<String> seenTitles = new HashSet<>();

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            try {
                scrapeAnnouncements();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private static void scrapeAnnouncements() throws IOException {
        Document doc = Jsoup.connect("https://www.est.um5.ac.ma/").get();
        Elements announcements = doc.select(".news-post");
        for (Element announcement : announcements) {
            String title = announcement.select("h3 a").text();
            String content = announcement.select(".block-meta time").text();
            String description = announcement.select(".news-content p").text();
            if (isNewAnnouncement(title)) {
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);
                System.out.println("Description: " + description);
                System.out.println();
                seenTitles.add(title);
            }
        }
        System.out.println("Announcements scraped.");
    }
    private static boolean isNewAnnouncement(String title) {
        return !seenTitles.contains(title) && (title.toLowerCase().contains("concour") || title.toLowerCase().contains("concours"));
    }
}
