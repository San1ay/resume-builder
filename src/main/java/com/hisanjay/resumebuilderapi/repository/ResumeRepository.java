package com.hisanjay.resumebuilderapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hisanjay.resumebuilderapi.model.Resume;

public interface ResumeRepository extends MongoRepository<Resume, String> {

    List<Resume> findByUserId(String userId);

    Optional<Resume> findByUserIdAndId(String userId, String Id);

}
