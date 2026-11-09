package es.dam.di;

import es.dam.di.model.*;
import es.dam.di.repository.*;
import es.dam.di.repository.jdbc.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        // Instancias
        IAccountRepository accountRepo = new AccountRepositoryJdbc();
        ICategoryRepository categoryRepo = new CategoryRepositoryJdbc();
        IDeveloperRepository developerRepo = new DeveloperRepositoryJdbc();
        IVideogameRepository videogameRepo = new VideogameRepositoryJdbc();
        IReviewRepository reviewRepo = new ReviewRepositoryJdbc();
        ILibraryRepository libraryRepo = new LibraryRepositoryJdbc();

        // Limpieza previa
        reviewRepo.deleteAll();
        libraryRepo.deleteAll();
        videogameRepo.deleteAll();
        developerRepo.deleteAll();
        categoryRepo.deleteAll();
        accountRepo.deleteAll();

        System.out.println();
        System.out.println("PRUEBAS DE INSERTAR");

        // Inserciones
        Account account1 = new Account("ruben", new Email("ruben@example.com"), "pass123", LocalDate.now());
        Account account2 = new Account("miguel", new Email("miguel@example.com"), "pass456", LocalDate.now());
        account1 = accountRepo.save(account1);
        account2 = accountRepo.save(account2);
        System.out.println("Cuentas creadas: " + account1.getUserName() + ", " + account2.getUserName());

        Developer dev1 = new Developer("FromSoftware");
        Developer dev2 = new Developer("Team Cherry");
        dev1 = developerRepo.save(dev1);
        dev2 = developerRepo.save(dev2);
        System.out.println("Desarrolladores creados: " + dev1.getName() + ", " + dev2.getName());

        Category cat1 = new Category("Soulslike");
        Category cat2 = new Category("Metroidvania");
        cat1 = categoryRepo.save(cat1);
        cat2 = categoryRepo.save(cat2);
        System.out.println("Categorias creadas: " + cat1.getName() + ", " + cat2.getName());

        Videogame game1 = new Videogame("Elden Ring", new Price(59.99), LocalDate.of(2022, 2, 25), dev1.getId(), List.of(cat1.getId()));
        Videogame game2 = new Videogame("Hollow Knight", new Price(14.99), LocalDate.of(2017, 2, 24), dev2.getId(), List.of(cat2.getId()));
        game1 = videogameRepo.save(game1);
        game2 = videogameRepo.save(game2);
        System.out.println("Videojuegos creados: " + game1.getTitle() + ", " + game2.getTitle());

        Review rev1 = new Review(account1.getId(), game1.getId(), true, "Muy bueno");
        Review rev2 = new Review(account2.getId(), game1.getId(), true, "Me encanta");
        Review rev3 = new Review(account1.getId(), game2.getId(), false, "Muy difícil, no me mola");
        rev1 = reviewRepo.save(rev1);
        rev2 = reviewRepo.save(rev2);
        rev3 = reviewRepo.save(rev3);
        System.out.println("Reviews creadas");

        Library lib1 = new Library(account1.getId(), game1.getId(), LocalDate.now(), new BigDecimal("120.50"));
        Library lib2 = new Library(account1.getId(), game2.getId(), LocalDate.now(), new BigDecimal("45.00"));
        libraryRepo.save(lib1);
        libraryRepo.save(lib2);
        System.out.println("Dos juegos comprados por: " + account1.getUserName());

        System.out.println();
        System.out.println("PRUEBAS GENERALES");

        System.out.println("Cuentas totales: " + accountRepo.count());
        System.out.println("Videojuegos totales: " + videogameRepo.count());

        boolean existeJuego = videogameRepo.existsById(game1.getId());
        boolean existeInexistente = videogameRepo.existsById(9999L);
        System.out.println("Existe juego con ID = " + game1.getId() + ": " + existeJuego);
        System.out.println("Existe juego con ID = 9999" + existeInexistente);

        Videogame juegoBuscado = videogameRepo.findById(game1.getId());
        System.out.println("Juego encontrado: " + (juegoBuscado != null ? juegoBuscado.getTitle() : "No encontrado"));

        System.out.println("Categorías: ");
        for (Category c : categoryRepo.findAll()) {
            System.out.println(c.getName());
        }
        System.out.println();

        System.out.println();
        System.out.println("PRUEBAS CON OPTIONAL");

        Optional<Account> accOpt = accountRepo.findByIdOptional(account1.getId());
        Optional<Account> accOptVacio = accountRepo.findByIdOptional(9999L);
        System.out.println("Cuenta con ID = " + account1.getId() + ": " + accOpt.map(Account::getUserName).orElse("Vacío"));
        System.out.println("Cuenta con ID = 9999: " + accOptVacio.isPresent());

        List<Developer> listaDevs = developerRepo.findAllToList();
        System.out.println("Número de desarrolladores: " + listaDevs.size());


        System.out.println();
        System.out.println("PRUEBA DE MÉTODOS PROPIOS");

        List<Developer> devsFiltrados = developerRepo.findByNameContaining("Cherry");
        System.out.println("Número de desarrolladores que contienen 'Cherry': " + devsFiltrados.size());

        Optional<Category> catOpt = categoryRepo.findByName("Soulslike");
        System.out.println("Categoría Soulslike, presente: " + catOpt.isPresent());

        double porcentaje = reviewRepo.getRecommendationPercentageByVideogameId(game1.getId());
        System.out.println("El juego con ID = " + game1.getId() + " tiene " + porcentaje + "% de valoraciones positivas");

        List<Review> resenasJuego = reviewRepo.findByVideogameId(game1.getId());
        System.out.println("Número reviews del juego con ID = " + game1.getId() + ": " + resenasJuego.size() + " reseñas");

        Optional<Library> libOpt = libraryRepo.findByAccountIdAndVideogameId(account1.getId(), game1.getId());
        System.out.println("librería con cuenta ID = " + account1.getId() + " y juego ID = " + game1.getId() + ": " + libOpt.isPresent()
                + " horas jugadas al juego: " + libOpt.map(Library::getHoursPlayed).orElse(BigDecimal.ZERO));

        List<Library> usuarioLibs = libraryRepo.findByAccountId(account1.getId());
        System.out.println("Número de juegos de la cuenta ID = " + account1.getId() + usuarioLibs.size() + " juegos en biblioteca");

        System.out.println();
        System.out.println("PRUEBAS DE UPDATE");

        game1.setTitle("Elden Ring: GOTY Edition");
        game1.setPrice(new Price(69.99));
        videogameRepo.save(game1);

        Videogame juegoModificado = videogameRepo.findById(game1.getId());
        System.out.println("Título: " + juegoModificado.getTitle());
        System.out.println("Precio: " + juegoModificado.getPrice());


        System.out.println("PRUEBAS DE DELETE");

        System.out.println("Reseñas antes del delete: " + reviewRepo.count());
        reviewRepo.deleteById(rev3.getId());
        System.out.println("Reseña ID = " + rev3.getId() + " borrada");
        System.out.println("Reseñas después del delete: " + reviewRepo.count());

        System.out.println("Existe la reseña eliminada: " + reviewRepo.existsById(rev3.getId()));

    }
}