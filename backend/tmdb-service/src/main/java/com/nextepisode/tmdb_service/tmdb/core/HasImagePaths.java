package com.nextepisode.tmdb_service.tmdb.core;

import com.nextepisode.tmdb_service.service.utll.ImageUrlBuilder;

public interface HasImagePaths {
    default String fullImageUrl(String path) {
        if(path == null){
            return "";
        }
        if(path.equals("")){
            return path;
        }
        return ImageUrlBuilder.buildUrl(path);
    }
}