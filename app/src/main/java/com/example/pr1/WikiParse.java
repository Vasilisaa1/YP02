package com.example.pr1;

import java.util.List;

public class WikiParse {
    public int Id;
    public String Title;
    public String Url;
    public String SiteType;
    public String ContentSummary;
    public List<String> KeyFeatures;
    public List<String> VersionHistory;
    public String ParsedAt;
    public String Age;

    // Конструктор по умолчанию (обязателен для многих парсеров, например Gson)
    public WikiParse() {}

    // Полный конструктор (опционально)
    public WikiParse(int id, String title, String url, String siteType,
                     String contentSummary, List<String> keyFeatures,
                     List<String> versionHistory, String parsedAt, String age) {
        this.Id = id;
        this.Title = title;
        this.Url = url;
        this.SiteType = siteType;
        this.ContentSummary = contentSummary;
        this.KeyFeatures = keyFeatures;
        this.VersionHistory = versionHistory;
        this.ParsedAt = parsedAt;
        this.Age = age;
    }

    // Геттеры
    public int getId() { return Id; }
    public String getTitle() { return Title; }
    public String getUrl() { return Url; }
    public String getSiteType() { return SiteType; }
    public String getContentSummary() { return ContentSummary; }
    public List<String> getKeyFeatures() { return KeyFeatures; }
    public List<String> getVersionHistory() { return VersionHistory; }
    public String getParsedAt() { return ParsedAt; }
    public String getAge() { return Age; }

    // Сеттеры
    public void setId(int id) { Id = id; }
    public void setTitle(String title) { Title = title; }
    public void setUrl(String url) { Url = url; }
    public void setSiteType(String siteType) { SiteType = siteType; }
    public void setContentSummary(String contentSummary) { ContentSummary = contentSummary; }
    public void setKeyFeatures(List<String> keyFeatures) { KeyFeatures = keyFeatures; }
    public void setVersionHistory(List<String> versionHistory) { VersionHistory = versionHistory; }
    public void setParsedAt(String parsedAt) { ParsedAt = parsedAt; }
    public void setAge(String age) { Age = age; }
}