package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User addUser(User user);

    List<String> getEmails();

    User updateUser(User user, long userId);

    void deleteUser(long userId);

    List<User> getUsers();

    Optional<User> findUser(long userId);
}