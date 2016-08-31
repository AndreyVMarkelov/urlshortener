package ru.andreymarkelov.interview.infobip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.andreymarkelov.interview.infobip.domain.Account;
import ru.andreymarkelov.interview.infobip.repository.AccountRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountId(username);
        if (account != null) {
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.singleton(new GrantedAuthority() {
                        @Override
                        public String getAuthority() {
                            return "USER";
                        }
                    });
                }

                @Override
                public String getPassword() {
                    return account.getPassword();
                }

                @Override
                public String getUsername() {
                    return username;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }
        throw new UsernameNotFoundException(String.format("Account with ID:%s not found", username));
    }

    @Transactional
    public Account createAccount(String accountId, String password) {
        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(passwordEncoder.encode(password));
        return accountRepository.save(account);
    }
}
