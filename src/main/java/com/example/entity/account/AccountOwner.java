package com.example.entity.account;

import com.example.entity.Role;

public interface AccountOwner {
    String getEmail();

    Role getRole();

    void linkTo(AppUser user);
}
