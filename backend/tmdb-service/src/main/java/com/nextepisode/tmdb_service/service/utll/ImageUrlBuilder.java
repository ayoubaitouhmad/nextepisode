package com.nextepisode.tmdb_service.service.utll;

import org.springframework.stereotype.Service;

@Service
public class ImageUrlBuilder {
    public static String W_92 = "w92";
    public static String W_154 = "w154";
    public static String W_185 = "w185";
    public static String W_342 = "w342";
    public static String W_500 = "w500";
    public static String W_780 = "w780";
    public static String ORIGINAL = "original";

    private static String basePath = "https://image.tmdb.org/t/p";
    private static String path;
    private static String size;

    private static ImageUrlBuilder instance;


    public ImageUrlBuilder() {
        size = ORIGINAL;
    }

    public static ImageUrlBuilder getTMDBImageService() {
        return instance = new ImageUrlBuilder();
    }

    public ImageUrlBuilder setSize(String size) {
        if (size == null || size.isEmpty())
            ImageUrlBuilder.size = ORIGINAL;
        else
            ImageUrlBuilder.size = size;
        return instance;
    }

    public ImageUrlBuilder setPath(String path) {
        if (path == null || path.isEmpty()) {
            path = "";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        ImageUrlBuilder.path = path;
        return instance;
    }

    public String get() {
        return basePath + '/' + size + '/' + path;
    }

}
