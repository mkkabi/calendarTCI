package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import java.util.logging.Logger;
import com.softserve.itacademy.tools.AppLogger;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = new AppLogger("UserServiceImpl.class");

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        try {
            User savedUser = repository.save(user);
            logger.info("saved user "+savedUser.getId());
            return savedUser;
        } catch (IllegalArgumentException e) {
            logger.warning("user was null, no user created");
            throw new NullEntityReferenceException("User was not created, try again");
        }
    }

    @Override
    public User readById(long id) {
        Optional<User> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading user, found user "+optional.get().getFirstName());
            return optional.get();
        }
        logger.warning("could not find user with the specified ID "+id);
        throw new EntityNotFoundException("Could not find user with ID "+id);
    }

    @Override
    public User update(User user) {
        if (user != null) {
            logger.info("updating valid user "+user.getId());
            User oldUser = readById(user.getId());
            if (oldUser != null) {

                logger.info("updating uer id = "+user.getId()+" user role = "+user.getRole().getName());
                return repository.save(user);
            }
        }
        logger.warning("user was not found in DB while updating, user does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
        if (user != null) {
            repository.delete(user);
        } else {
            throw new EntityNotFoundException("Could not find user with ID "+id);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = repository.findAll();
        logger.info("found "+users.size()+" users in DB");
        return users.isEmpty() ? new ArrayList<>() : users;
    }

    @Override
    public User getUserByEmail(String email){
        Optional<User> optional = repository.getUserByEmail(email);
        if (optional.isPresent()) {
            logger.info("reading user by email, found user "+optional.get().getFirstName());
            return optional.get();
        }
        logger.warning("could not find user with the specified email "+email);
        throw new EntityNotFoundException("Could not find user with email "+email);
    }

}