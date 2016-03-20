package com.codurance.social.repository;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class SocialGraph {

	@Autowired
	private Neo4jTemplate template;

	public Neo4jTemplate getTemplate() {
		return template;
	}

	public void setTemplate(Neo4jTemplate template) {
		this.template = template;
	}

	/*@PostConstruct
	public void registerShutdownHook() {
		final GraphDatabase graphDb = template.getGraphDatabase();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				((GraphDatabaseService) graphDb).shutdown();
			}
		});
	}*/
}
