package com.codurance.social.model;

import java.io.Serializable;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

public class BaseEntity implements Serializable {

	@GraphId
	protected Long nodeId;
	@Indexed
	protected String id;
	private Long dateCreated;

	public BaseEntity() {
		dateCreated = new DateTime().getMillis();
		id = UUID.randomUUID().toString();
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

	public DateTime getDateCreated() {
		return new DateTime(dateCreated);
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated.getMillis();
	}

	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "BaseEntity [nodeId=" + nodeId + ", id=" + id + ", " + "dateCreated=" + new DateTime(dateCreated) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;
}
