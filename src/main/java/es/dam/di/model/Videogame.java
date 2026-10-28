package es.dam.di.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Videogame {
    private Long id;
    private String title;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Long developerId;

    public Videogame(){
    }

    public Videogame(String title, BigDecimal price, LocalDate releaseDate, Long developerId) {
        this(null, title, price, releaseDate, developerId);
    }

    public Videogame(Long id, String title, BigDecimal price, LocalDate releaseDate, Long developerId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developerId = developerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }
}
