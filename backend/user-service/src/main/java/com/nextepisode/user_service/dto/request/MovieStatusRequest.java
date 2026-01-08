package com.nextepisode.user_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.config.NamespaceUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieStatusRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Action is required")
    private Action action;

    public enum Category {
        FAVORITE("favorite"),
        WATCHED("watched"),
        WATCHLIST("watchlist");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Category fromName(String name) {

            if(name == null || name.isEmpty()) {
                return null;
            }
            for (Category category : Category.values()) {
                if (category.name().equalsIgnoreCase(name.toLowerCase())) {
                    return category;
                }
            }

            throw new IllegalArgumentException("Invalid category name: " + name);
        }
    }

    public enum Action {
        ADD(1, "Add"),
        REMOVE(0, "Remove");

        private final int value;
        private final String displayName;

        Action(int value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public int getValue() {
            return value;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Action fromName(String name) {

            if(name == null || name.isEmpty()) {
                return null;
            }
            for (Action action : Action.values()) {
                if (action.name().equalsIgnoreCase(name.toLowerCase())) {
                    return action;
                }
            }

            throw new IllegalArgumentException("Invalid action name: " + name);
        }


    }


}
