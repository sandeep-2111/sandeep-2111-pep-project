package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
            account.getPassword() == null || account.getPassword().length() <= 4) {
            return null;
        }

        Account existingAccount = accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existingAccount != null) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}
