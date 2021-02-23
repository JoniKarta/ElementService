package com.guardian.validations;

import org.springframework.stereotype.Component;

import com.guardian.boundaries.ElementBoundary;
import com.guardian.data.ElementEntity;

@Component
public class ElementValidator {

	public boolean validateElementName(ElementBoundary element) {
		return element.getName() != null;
	}

	public boolean validateElementType(ElementBoundary element) {
		return element.getType() != null;
	}

	public boolean validateElementActive(ElementBoundary element) {
		return element.getActive() != null;
	}
	
	public boolean validateElementLocation(ElementBoundary element) {
		return element.getLocation() != null;
	}
	
	public boolean validateElementAttr(ElementBoundary element) {
		return element.getElementAttribute() != null;
	}

	public boolean isActive(ElementEntity elementEntity) {
		return elementEntity != null && elementEntity.getActive() != null && elementEntity.getActive().booleanValue();
	}
}
