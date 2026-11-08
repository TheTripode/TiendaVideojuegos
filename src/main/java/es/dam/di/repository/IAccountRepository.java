package es.dam.di.repository;

import es.dam.di.model.Account;
import es.dam.di.model.Email;
import java.util.Optional;

public interface IAccountRepository extends IRepositorioExtend<Account, Long> {
    Optional<Account> findByEmail(Email email);
    Optional<Account> findByUserName(String userName);
}