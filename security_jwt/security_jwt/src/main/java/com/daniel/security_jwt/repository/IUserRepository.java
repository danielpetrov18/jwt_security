package com.daniel.security_jwt.repository;

import java.util.Optional;

import com.daniel.security_jwt.user.User;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(final String email);

}