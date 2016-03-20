package com.codurance.social.repository;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
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

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();

			}
		});
	}
}
