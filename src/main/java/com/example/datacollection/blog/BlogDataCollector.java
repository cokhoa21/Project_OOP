package com.example.datacollection.blog;

import com.example.datacollection.DataCollector;
import com.example.model.Blog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlogDataCollector implements DataCollector<Blog> {

    private final String blogUrl;

    public BlogDataCollector(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    @Override
    public List<Blog> collectData() {
        List<Blog> listBlogs = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(blogUrl).get();
            Elements articles = doc.select("article");

            for (Element article : articles) {
                String title = article.select("h2").text();
                String content = extractContent(article);

                Blog blog = new Blog(title, content);
                listBlogs.add(blog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listBlogs;
    }

    public List<Blog> getPostsByKeyword(String keyword) {
        List<Blog> allPosts = collectData();

        return allPosts.stream()
                .filter(post -> post.getContent().contains(keyword))
                .toList();
    }

    private String extractContent(Element article) {
        Element entryContent = article.selectFirst(".entry-content");
        if (entryContent != null) {
            return entryContent.html();
        }
        return "";
    }
}
