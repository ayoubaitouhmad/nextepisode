package com.nextepisode.user_service.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTvId implements Serializable {

    @Column(name = "user_username")
    private String userUsername;

    @Column(name = "tv_id")
    private Long tvId;

    // CRITICAL: equals and hashCode are required for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTvId that = (UserTvId) o;
        return Objects.equals(userUsername, that.userUsername) &&
                Objects.equals(tvId, that.tvId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUsername, tvId);
    }
}