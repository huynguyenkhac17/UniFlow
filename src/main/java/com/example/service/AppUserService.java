package com.example.service;

import com.example.entity.account.AccountOwner;
import com.example.entity.account.AppUser;
import com.example.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Chưa có tk -> tạo mới, nếu có rồi thì thôi
    @Transactional
    public void sync(AccountOwner accountOwner) {
        if (appUserRepository.findByEmail(accountOwner.getEmail()).isEmpty()) {
            AppUser appUser = new AppUser(
                    accountOwner.getEmail(),
                    passwordEncoder.encode("123456"),
                    accountOwner.getRole()
            );

            accountOwner.linkTo(appUser);
            appUserRepository.save(appUser);
        }
    }

    @Transactional
    public void deleteAccount(AccountOwner accountOwner) {
        appUserRepository.findByEmail(accountOwner.getEmail()).ifPresent(appUserRepository::delete);
    }
    
}
