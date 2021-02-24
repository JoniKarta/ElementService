package com.guardian.service;

import java.util.List;
import java.util.Map;

import com.guardian.boundaries.ElementBoundary;

public interface ElementService {

	ElementBoundary create(ElementBoundary input);

	void update(String elementId, ElementBoundary update);

	ElementBoundary getSpecificElement(String elementId);

	List<ElementBoundary> getAllElementsByLocationFilters(Map<String, String> attr, String sortBy, String sortOrder,
			int page, int size);

	List<ElementBoundary> getAllElements(String value, String sortBy, String sortOrder, int page, int size);

	List<ElementBoundary> getAllElementsByName(String value, String sortBy, String sortOrder, int page, int size);

	List<ElementBoundary> getAllElementsByTypes(String value, String sortBy, String sortOrder, int page, int size);

	void deleteAll();

}
