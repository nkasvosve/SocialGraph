package com.codurance.social.repository;

import org.springframework.data.neo4j.repository.CRUDRepository;
import org.springframework.transaction.annotation.Transactional;

import com.codurance.social.model.User;

public interface UserRepository extends CRUDRepository<User> {

	@Transactional
	User findByUserName(String username);
}
