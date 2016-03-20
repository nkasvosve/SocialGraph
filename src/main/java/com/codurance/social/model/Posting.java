package com.codurance.social.model;

import java.util.UUID;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * @author nickk
 * 
 *         This class models the user's postings
 */

@NodeEntity
public class Posting extends BaseEntity {

	@Indexed
	protected String id;
	private String message;

	@RelatedTo(type = RelationshipTypes.POSTED_BY, direction = Direction.OUTGOING)
	private User poster;

	private String posterName;

	public Posting() {
		id = UUID.randomUUID().toString();
	}

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

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	@Override
	public String toString() {
		return "Posting [nodeId=" + nodeId + ", id=" + id + ", message=" + message + ", poster=" + posterName
				+ super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((poster == null) ? 0 : poster.hashCode());
		result = prime * result + ((posterName == null) ? 0 : posterName.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
		if (posterName == null) {
			if (other.posterName != null)
				return false;
		} else if (!posterName.equals(other.posterName))
			return false;
		return true;
	}

	private static final long serialVersionUID = 8680498711219482877L;
}
