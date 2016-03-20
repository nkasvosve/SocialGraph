package com.codurance.social.model;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;

public class BaseEntity {

	@GraphId
	protected Long nodeId;
	@Indexed
	protected String id;
	private DateTime dateCreated;

	public BaseEntity() {
		dateCreated = new DateTime();
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
		return dateCreated;
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getFormattedDateCreated() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MMM-dd HH:mm");
		return dateCreated.toString(fmt);
	}

	public String getFormattedDayCreated() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MMM-dd");
		return dateCreated.toString(fmt);
	}

	public String getFormattedDay() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy");
		return dateCreated.toString(fmt);
	}

	public String formattedDate(DateTime d) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE, d MMM yyyy");
		return d.toString(fmt);
	}

	@Override
	public String toString() {
		return "BaseEntity [nodeId=" + nodeId + ", id=" + id + ", dateCreated=" + dateCreated + "]";
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
}
