package com.nextepisode.tmdb_service.service;

import org.springframework.stereotype.Service;

@Service
public class TMDBImageService {
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

    private static TMDBImageService instance;


    public TMDBImageService() {
        size = ORIGINAL;
    }

    public static TMDBImageService getTMDBImageService() {
        return instance = new TMDBImageService();
    }

    public TMDBImageService setSize(String size) {
        if (size == null || size.isEmpty())
            TMDBImageService.size = ORIGINAL;
        else
            TMDBImageService.size = size;
        return instance;
    }

    public TMDBImageService setPath(String path) {
        if (path == null || path.isEmpty()) {
            path = "";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        TMDBImageService.path = path;
        return instance;
    }

    public String get() {
        return basePath + '/' + size + '/' + path;
    }

}
