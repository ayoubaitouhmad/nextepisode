package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.TvListResponse;
import com.nextepisode.user_service.dto.TvResponse;
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
    public TvListResponse getUserFavoriteMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} favorite movies, page:{}", username, pageable.getPageNumber());
        Page<TvResponse> page = userTvRepository.findUserFavoriteMovies(username, pageable);
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


}
