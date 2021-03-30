package com.guardian.logic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.guardian.boundaries.ElementBoundary;
import com.guardian.converters.ElementConverter;
import com.guardian.dal.ElementDao;
import com.guardian.data.ElementEntity;
import com.guardian.data.ElementLocation;
import com.guardian.exceptions.ElementNotFoundException;
import com.guardian.exceptions.ExcessiveReportsException;
import com.guardian.service.ElementService;
import com.guardian.validations.ElementValidator;

@Service
public class ElementServiceWithDB implements ElementService {
	private ElementDao elementDao;
	private ElementConverter elementConverter;
	private ElementValidator validator;

	@Autowired
	public void setEntityConverter(ElementConverter entityConverter) {
		this.elementConverter = entityConverter;
	}

	@Autowired
	public void setElementDao(ElementDao elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setValidator(ElementValidator validator) {
		this.validator = validator;
	}

	@Override
	public ElementBoundary create(ElementBoundary boundary) {

		// Get elements in range of approx. 500M of the element
		List<ElementEntity> elementsInRange = this.elementDao.findAllByTypeAndLocation_latBetweenAndLocation_lngBetween(
				boundary.getType(), boundary.getLocation().getLat() - 0.000500,
				boundary.getLocation().getLat() + 0.000500, boundary.getLocation().getLng() - 0.000500,
				boundary.getLocation().getLng() + 0.000500);

		if (elementsInRange.isEmpty()) {
			boundary.setActive(false);
			boundary.getElementAttribute().put("threshold", 1);
			return this.elementConverter.toBoundary(this.elementDao.save(this.elementConverter.toEntity(boundary)));
		}

		ElementEntity closestElement = elementsInRange.get(0);
		String userEmail = boundary.getCreatedBy().getUserEmail().replaceAll("\\.", "_");

		// Check if user who reports is not the one who created && user who reports did
		// not report on the same report already
		if (!closestElement.getCreatedBy().getUserEmail().equalsIgnoreCase(boundary.getCreatedBy().getUserEmail())
				&& !closestElement.getElementAttribute().containsKey(userEmail)) {
			int currentThreshold = (int) closestElement.getElementAttribute().get("threshold") + 1;

			closestElement.getElementAttribute().put("threshold", currentThreshold);
			closestElement.getElementAttribute().put(userEmail, "");

			if (currentThreshold >= 3)
				closestElement.setActive(true);

			ElementBoundary closeElementBoundary = this.elementConverter.toBoundary(closestElement);
			update(closestElement.getId(), closeElementBoundary);
			return closeElementBoundary;
		} else {
			throw new ExcessiveReportsException("Cannot report twice");
		}
	}

	@Override
	public void update(String elementId, ElementBoundary update) {
		ElementEntity existing = this.elementDao.findById(elementId)
				.orElseThrow(() -> new ElementNotFoundException("Cannot find element with id: " + elementId));
		if (this.validator.validateElementType(update)) {
			existing.setType(update.getType());
		}

		if (this.validator.validateElementName(update)) {
			existing.setName(update.getName());
		}

		if (this.validator.validateElementActive(update) && update.getActive() != existing.getActive()) {
			existing.setActive(update.getActive());
		}

		if (this.validator.validateElementLocation(update)) {
			existing.setLocation(new ElementLocation(update.getLocation().getLat(), update.getLocation().getLng()));
		}
		if (this.validator.validateElementAttr(update)) {
			existing.setElementAttribute(update.getElementAttribute());
		}

		this.elementDao.save(existing);
	}

	@Override
	public ElementBoundary getSpecificElement(String elementId) {
		return this.elementConverter.toBoundary(this.elementDao.findById(elementId)
				.orElseThrow(() -> new ElementNotFoundException("Cannot found element with id: " + elementId)));
	}

	@Override
	public List<ElementBoundary> getAllElementsByLocationFilters(Map<String, String> attr, String sortBy,
			String sortOrder, int page, int size) {

		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return elementDao
				.findAllByActiveAndLocation_latBetweenAndLocation_lngBetween(
						elementConverter.toBoolean(attr.get("active")), elementConverter.toDouble(attr.get("minLat")),
						elementConverter.toDouble(attr.get("maxLat")), elementConverter.toDouble(attr.get("minLng")),
						elementConverter.toDouble(attr.get("maxLng")), PageRequest.of(page, size, direction, sortBy))
				.stream().map(this.elementConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ElementBoundary> getAllElements(String value, String sortBy, String sortOrder, int page, int size) {
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.elementDao.findAll(PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.elementConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ElementBoundary> getAllElementsByName(String value, String sortBy, String sortOrder, int page,
			int size) {
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.elementDao.findAllByName(value, PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.elementConverter::toBoundary).collect(Collectors.toList());

	}

	@Override
	public List<ElementBoundary> getAllElementsByTypes(String value, String sortBy, String sortOrder, int page,
			int size) {
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.elementDao.findAllByType(value, PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.elementConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public void deleteAll() {
		this.elementDao.deleteAll();
	}

}
