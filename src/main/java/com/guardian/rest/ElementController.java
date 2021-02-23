package com.guardian.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardian.boundaries.ElementBoundary;
import com.guardian.service.ElementService;

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

	@RequestMapping(path = "/elements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementByFilters(
			@RequestParam(name = "filterType", required = false, defaultValue = "") String type,
			@RequestParam(name = "filterValue", required = false) String value,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdTimestamp") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") String sortOrder,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {

		switch (type) {
		case "PERIMETER":
			return this.elementService.getAllElementsByPerimeter(value, sortBy, sortOrder, page, size)
					.toArray(new ElementBoundary[0]);
//		case RouteFilterType.BY_CREATED_TIMESTAMP:
//			return this.packageTrackingService.getTracksByCreatedTimestamp(email, value, sortBy, sortOrder, page, size)
//					.toArray(new TrackBoundary[0]);
//		case RouteFilterType.BY_STATUS:
//			return this.packageTrackingService.getTracksByStatus(email, value, sortBy, sortOrder, page, size)
//					.toArray(new TrackBoundary[0]);
//		default:
//			return this.elementService.getAllElements(email, sortBy, sortOrder, page, size)
//					.toArray(new ElementBoundary[0]);
//		}
		}
		return this.elementService.getAllElements(sortBy, sortOrder, page, size).toArray(new ElementBoundary[0]);
	}
}
