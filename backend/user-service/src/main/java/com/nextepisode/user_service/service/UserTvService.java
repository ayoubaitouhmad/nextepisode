package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.TvListResponse;
import com.nextepisode.user_service.dto.TvResponse;
import com.nextepisode.user_service.dto.UserMovieTvStats;
import com.nextepisode.user_service.repo.UserTvRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserTvService {
    private final UserTvRepository userTvRepository;

    public UserTvService(UserTvRepository userTvRepository) {
        this.userTvRepository = userTvRepository;
    }


    @Transactional(readOnly = true)
    public TvListResponse getUserFavoriteShows(String username, Pageable pageable) {
        log.debug("Finding user:{} favorite shows, page:{}", username, pageable.getPageNumber());
        Page<TvResponse> page = userTvRepository.findUserFavoriteShows(username, pageable);
        return buildMovieListResponse(page);
    }


    @Transactional(readOnly = true)
    public TvListResponse getUserWatchedShows(String username, Pageable pageable) {
        log.debug("Finding user:{} watched shows, page:{}", username, pageable.getPageNumber());
        Page<TvResponse> page = userTvRepository.findUserWatchedShows(username, pageable);
        return buildMovieListResponse(page);
    }

    @Transactional(readOnly = true)
    public TvListResponse getUserWatchlistShows(String username, Pageable pageable) {
        log.debug("Finding user:{} watchlist shows, page:{}", username, pageable.getPageNumber());
        Page<TvResponse> page = userTvRepository.findUserWatchlistShows(username, pageable);
        return buildMovieListResponse(page);
    }


    private TvListResponse buildMovieListResponse(Page<TvResponse> page) {
        return TvListResponse.builder()
                .page(page.getNumber())
                .results(page.getContent())
                .totalPages((long) page.getTotalPages())
                .totalResults(page.getTotalElements())
                .build();
    }


    @Transactional(readOnly = true)
    public UserMovieTvStats getUserShowsStats(String username) {
        return userTvRepository.getUserMovieStats(username);
    }

}
