package es.dam.di.model;

import java.time.LocalDate;

public class Review {
    private Long id;
    private Long accountId;
    private Long videogameId;
    private boolean recommended;
    private String comment;
    private LocalDate publicationDate;

    public Review() {
    }

    public Review(Long accountId, Long videogameId, boolean recommended, String comment) {
        this(null, accountId, videogameId, recommended, comment, LocalDate.now());
    }

    public Review(Long id, Long accountId, Long videogameId, boolean recommended, String comment, LocalDate publicationDate) {
        this.id = id;
        this.accountId = accountId;
        this.videogameId = videogameId;
        this.recommended = recommended;
        this.comment = comment;
        this.publicationDate = publicationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getVideogameId() {
        return videogameId;
    }

    public void setVideogameId(Long videogameId) {
        this.videogameId = videogameId;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}
