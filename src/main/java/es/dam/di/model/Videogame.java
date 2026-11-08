package es.dam.di.model;

import java.time.LocalDate;
import java.util.Objects;

public class Videogame {
    private Long id;
    private String title;
    private Price price;
    private LocalDate releaseDate;
    private Long developerId;

    public Videogame() {
    }

    public Videogame(String title, Price price, LocalDate releaseDate, Long developerId) {
        this(null, title, price, releaseDate, developerId);
    }

    public Videogame(Long id, String title, Price price, LocalDate releaseDate, Long developerId) {
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

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Videogame videogame = (Videogame) o;
        return Objects.equals(id, videogame.id) && Objects.equals(title, videogame.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Videogame{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", developerId=" + developerId +
                '}';
    }
}