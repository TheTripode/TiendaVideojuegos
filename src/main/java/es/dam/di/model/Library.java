package es.dam.di.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Library {
    private Long accountId;
    private Long videogameId;
    private LocalDate purchaseDate;
    private BigDecimal hoursPlayed;

    public Library() {
    }

    public Library(Long accountId, Long videogameId) {
        this(accountId, videogameId, LocalDate.now(), BigDecimal.ZERO);
    }

    public Library(Long accountId, Long videogameId, LocalDate purchaseDate, BigDecimal hoursPlayed) {
        this.accountId = accountId;
        this.videogameId = videogameId;
        this.purchaseDate = purchaseDate;
        this.hoursPlayed = hoursPlayed;
    }

    // --- Getters y Setters ---

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

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(BigDecimal hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }
}