package es.dam.di.repository;

import es.dam.di.model.Account;
import java.util.Optional;

public interface IAccountRepository extends IRepositorioExtend<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUserName(String userName);
}