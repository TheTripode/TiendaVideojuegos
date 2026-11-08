package es.dam.di.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(accountId, library.accountId) && Objects.equals(videogameId, library.videogameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, videogameId);
    }

    @Override
    public String toString() {
        return "Library{" +
                "accountId=" + accountId +
                ", videogameId=" + videogameId +
                ", purchaseDate=" + purchaseDate +
                ", hoursPlayed=" + hoursPlayed +
                '}';
    }
}