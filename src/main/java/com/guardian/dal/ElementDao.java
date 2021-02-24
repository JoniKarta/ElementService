package com.guardian.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.guardian.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String> {

	public List<ElementEntity> findAllByActiveAndLocation_latBetweenAndLocation_lngBetween(
			@Param("active") boolean active, @Param("minLat") double minLat, @Param("maxLat") double maxLat,
			@Param("minLng") double minLng, @Param("maxLng") double maxLng, Pageable pageable);

	public List<ElementEntity> findAllByName(@Param("name") String name, Pageable pageable);

	public List<ElementEntity> findAllByType(@Param("type") String type, Pageable pageable);
}
