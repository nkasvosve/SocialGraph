package com.codurance.social.repository;

import org.springframework.data.neo4j.repository.CRUDRepository;

import com.codurance.social.model.Posting;

public interface PostingRepository extends CRUDRepository<Posting> {
}
