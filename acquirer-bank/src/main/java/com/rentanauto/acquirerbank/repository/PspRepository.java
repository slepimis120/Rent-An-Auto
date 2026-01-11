package com.rentanauto.acquirerbank.repository;

import com.rentanauto.acquirerbank.domain.PSP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PspRepository extends JpaRepository<PSP, UUID> {
}
