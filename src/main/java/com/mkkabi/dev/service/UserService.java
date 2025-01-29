package com.mkkabi.dev.service;

import com.mkkabi.dev.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User readById(long id);
    User update(User user);
    void delete(long id);
    List<User> getAll();
    User getUserByEmail(String email);

}
