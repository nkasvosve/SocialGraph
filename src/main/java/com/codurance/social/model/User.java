package com.codurance.social.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * @author nickk
 * 
 * This class represents a user. It models the user's relationship
 * to their postings, follows, and followers 
 */

@NodeEntity
public class User extends BaseEntity {

	private String userName;

	@RelatedTo(type = RelationshipTypes.POSTINGS, direction = Direction.OUTGOING)
	private Set<Posting> postings = new LinkedHashSet<>();

	@RelatedTo(type = RelationshipTypes.FOLLOWED_BY, direction = Direction.OUTGOING)
	private Set<User> followers = new HashSet<>();

	@RelatedTo(type = RelationshipTypes.FOLLOWS, direction = Direction.OUTGOING)
	private Set<User> follows = new HashSet<>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Posting> getPostings() {
		return postings;
	}

	public void setPostings(Set<Posting> postings) {
		this.postings = postings;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public Set<User> getFollows() {
		return follows;
	}

	public void setFollows(Set<User> follows) {
		this.follows = follows;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
}
