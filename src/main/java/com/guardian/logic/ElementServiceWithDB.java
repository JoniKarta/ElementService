package com.guardian.logic;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.guardian.boundaries.ElementBoundary;
import com.guardian.converters.EntityConverter;
import com.guardian.dal.ElementDao;
import com.guardian.data.ElementEntity;
import com.guardian.data.ElementLocation;
import com.guardian.exceptions.ElementNotFoundException;
import com.guardian.service.ElementService;
import com.guardian.validations.ElementValidator;

@Service
public class ElementServiceWithDB implements ElementService {
	private ElementDao elementDao;
	private EntityConverter entityConverter;
	private ElementValidator validator;

	@Autowired
	public void setEntityConverter(EntityConverter entityConverter) {
		this.entityConverter = entityConverter;
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
		return this.entityConverter.toBoundary(this.elementDao.save(this.entityConverter.toEntity(boundary)));
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
		return this.entityConverter.toBoundary(this.elementDao.findById(elementId)
				.orElseThrow(() -> new ElementNotFoundException("Cannot found element with id: " + elementId)));
	}

	@Override
	public List<ElementBoundary> getAllElements(String sortBy, String sortOrder, int page, int size) {
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.elementDao.findAll(PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.entityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ElementBoundary> getAllElementsByPerimeter(String value, String sortBy, String sortOrder, int page,
			int size) {
//		
//			return elementDao
//			.findAllByActiveAndLocation_latBetweenAndLocation_lngBetween(true,
//					(Double) action.getActionAttributes().get("minLat"),
//					(Double) action.getActionAttributes().get("maxLat"),
//					(Double) action.getActionAttributes().get("minLng"),
//					(Double) action.getActionAttributes().get("maxLng"))
//			.stream().map(this.elementConverter::convertFromEntity)
//			.collect(Collectors.toList());
		
		return null;
	}

}
