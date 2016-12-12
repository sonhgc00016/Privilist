package com.privilist.model;

/**
 * Created by SonH on 2015-06-30.
 */
public class RankDetails {
    private String full_name;
    private long reputation_points;
    private Rank rank;
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public long getReputation_points() {
        return reputation_points;
    }

    public void setReputation_points(long reputation_points) {
        this.reputation_points = reputation_points;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public static class Rank {
        private int id;
        private String name;
        private long points;
        private String description;
        private String perks;
        private String color;
        private String icon;
        private String deleted_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPoints() {
            return points;
        }

        public void setPoints(long points) {
            this.points = points;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPerks() {
            return perks;
        }

        public void setPerks(String perks) {
            this.perks = perks;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }
    }
}
