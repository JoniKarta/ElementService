package com.guardian.service;

import java.util.List;

import com.guardian.boundaries.ElementBoundary;

public interface ElementService {

	ElementBoundary create(ElementBoundary input);

	void update(String elementId, ElementBoundary update);

	ElementBoundary getSpecificElement(String elementId);

	List<ElementBoundary> getAllElements(String sortBy, String sortOrder, int page, int size);

	List<ElementBoundary> getAllElementsByPerimeter(String value, String sortBy, String sortOrder, int page, int size);

}
