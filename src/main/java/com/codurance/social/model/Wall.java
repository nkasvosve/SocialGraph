package com.codurance.social.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * @author nickk
 * 
 *         This class models the user's postings
 */

@NodeEntity
public class Wall extends BaseEntity {

	@RelatedTo(type = RelationshipTypes.OWNED_BY, direction = Direction.OUTGOING)
	private User owner;
	
	@RelatedTo(type = RelationshipTypes.POSTINGS, direction = Direction.OUTGOING)
	private Set<Posting> postings = new HashSet<Posting>();

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<Posting> getPostings() {
		return postings;
	}

	public void setPostings(Set<Posting> postings) {
		this.postings = postings;
	}
	private static final long serialVersionUID = -5501305022251649941L;

}
