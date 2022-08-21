package com.konopka.emailfilteringservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilterRepository extends CrudRepository<Filter,Long> {

    @Query("SELECT f FROM Filter f WHERE f.major LIKE %:major%")
    Optional<Filter> findByMajor(@Param("major") String major);

    @Query("SELECT f FROM Filter f WHERE f.major LIKE %:major% AND f.val LIKE %:value%")
    Optional<Filter> findByMajorAndVal(@Param("major") String major,
                                       @Param("value") String value);
}
