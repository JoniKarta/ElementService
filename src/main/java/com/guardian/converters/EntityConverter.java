package com.guardian.converters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.guardian.boundaries.Creator;
import com.guardian.boundaries.ElementBoundary;
import com.guardian.boundaries.Location;
import com.guardian.data.ElementCreator;
import com.guardian.data.ElementEntity;
import com.guardian.data.ElementLocation;

@Component
public class EntityConverter {

	public ElementBoundary toBoundary(ElementEntity entity) {
		return new ElementBoundary(entity.getId(), entity.getType(), entity.getIcon(), entity.getName(),
				entity.getActive(), entity.getCreatedTimestamp(), new Creator(entity.getCreatedBy().getUserEmail()),
				new Location(entity.getLocation().getLat(), entity.getLocation().getLng()),
				entity.getElementAttribute());

	}

	public ElementEntity toEntity(ElementBoundary boundary) {
		ElementEntity entity = new ElementEntity(boundary.getType(), boundary.getIcon(), boundary.getName(),
				boundary.getActive(), new ElementCreator(boundary.getCreatedBy().getUserEmail()),
				new ElementLocation(boundary.getLocation().getLat(), boundary.getLocation().getLng()),
				boundary.getElementAttribute());
		entity.setCreatedTimestamp(new Date());
		return entity;
	}

}
