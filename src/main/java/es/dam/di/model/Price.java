package es.dam.di.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Price {
    private final BigDecimal value;

    public Price(BigDecimal value) {
        // comparteTo() mtodo para comparar los BIGDECIMAL -> devuelve <0 si menor, 0 si igual, >0 si mayor
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser nulo ni negativo");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP); //2 decimales y redondea el segundo decimal si >5
    }

    public Price(double value) {
        this(BigDecimal.valueOf(value)); //Hace que no sea impreciso
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + " €";
    }
}