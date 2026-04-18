package com.hisanjay.resumebuilderapi.repository;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hisanjay.resumebuilderapi.document.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);
}
