package com.example.authbackend.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByCategoryIgnoreCaseIn(List<String> categories);

    // Búsqueda por categoría
    Page<Activity> findByCategoryIgnoreCase(String category, Pageable pageable);

    // Búsqueda por destino
    Page<Activity> findByDestinationIgnoreCase(String destination, Pageable pageable);

    // Búsqueda por rango de precio
    @Query("SELECT a FROM Activity a WHERE a.price >= :minPrice AND a.price <= :maxPrice")
    Page<Activity> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, Pageable pageable);

    // Búsqueda combinada: categoría + rango de precio + destino
    @Query("SELECT a FROM Activity a WHERE " +
            "(:category IS NULL OR a.category ILIKE %:category%) AND " +
            "(:destination IS NULL OR a.destination ILIKE %:destination%) AND " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice)")
    Page<Activity> findByFilters(
            @Param("category") String category,
            @Param("destination") String destination,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    // Búsqueda por nombre
    Page<Activity> findByNameIgnoreCaseContaining(String name, Pageable pageable);
}