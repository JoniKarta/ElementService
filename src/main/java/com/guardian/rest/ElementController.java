package com.guardian.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardian.boundaries.ElementBoundary;
import com.guardian.service.ElementService;
import com.guardian.utility.FilterTypes;

@RestController
public class ElementController {

	private ElementService elementService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@RequestMapping(path = "/elements", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createElementBoundary(@RequestBody ElementBoundary input) {
		return this.elementService.create(input);
	}

	@RequestMapping(path = "/elements/{elementId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("elementId") String elementId, @RequestBody ElementBoundary update) {
		this.elementService.update(elementId, update);
	}

	@RequestMapping(path = "/elements/{elementId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElementBoundary(@PathVariable("elementId") String elementId) {
		return this.elementService.getSpecificElement(elementId);
	}

	@RequestMapping(path = "/elements/location", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementByLocationFilters(@RequestParam(required = true) Map<String, String> attr,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdTimestamp") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") String sortOrder,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.elementService.getAllElementsByLocationFilters(attr, sortBy, sortOrder, page, size)
				.toArray(new ElementBoundary[0]);
	}

	@RequestMapping(path = "/elements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementByFilters(
			@RequestParam(name = "type", required = false, defaultValue = "") String type,
			@RequestParam(name = "value", required = false) String value,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdTimestamp") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") String sortOrder,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {

		switch (type) {

		case FilterTypes.BY_NAME:
			return this.elementService.getAllElementsByName(value, sortBy, sortOrder, page, size)
					.toArray(new ElementBoundary[0]);

		case FilterTypes.BY_TYPE:
			return this.elementService.getAllElementsByTypes(value, sortBy, sortOrder, page, size)
					.toArray(new ElementBoundary[0]);
		}
		return this.elementService.getAllElements(value, sortBy, sortOrder, page, size).toArray(new ElementBoundary[0]);
	}

	@RequestMapping(path = "/elements", method = RequestMethod.DELETE)
	public void deleteAll() {
		this.elementService.deleteAll();
	}

}
