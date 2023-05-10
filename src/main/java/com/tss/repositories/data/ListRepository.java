package com.tss.repositories.data;

import com.tss.entities.data.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {
}