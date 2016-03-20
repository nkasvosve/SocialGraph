package com.codurance.social.model;

import org.joda.time.DateTime;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * @author nickk
 * 
 * This class models the user's postings
 */

@NodeEntity
public class Posting extends BaseEntity {

    private String message;
    
	@RelatedTo(type = RelationshipTypes.POSTED_BY, direction = Direction.OUTGOING)
	private User poster;
	
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public User getPoster() {
		return poster;
	}

	public void setPoster(User poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Posting [message=" + message + ", poster=" + poster + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((poster == null) ? 0 : poster.hashCode());
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
		Posting other = (Posting) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (poster == null) {
			if (other.poster != null)
				return false;
		} else if (!poster.equals(other.poster))
			return false;
		return true;
	}
}
