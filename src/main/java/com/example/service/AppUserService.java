package com.example.service;

import com.example.entity.account.AppUser;
import com.example.entity.person.Person;
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

    // person chưa có tk -> tạo mới, nếu có rồi thì thôi
    @Transactional
    public void sync(Person person) {
        if (appUserRepository.findByEmail(person.getEmail()).isEmpty()) { // tài khoản chính là email
            AppUser appUser = new AppUser(
                    person.getEmail(),
                    passwordEncoder.encode("123456"), // default pass
                    person.getRole()
            );

            appUserRepository.save(appUser);
        }
    }

    @Transactional
    public void deleteAccount(Person person) {
        appUserRepository.findByEmail(person.getEmail()).ifPresent(appUserRepository::delete);
    }
    
}
