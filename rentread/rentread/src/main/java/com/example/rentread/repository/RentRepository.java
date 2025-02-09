package com.example.rentread.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rentread.entity.Rent;

public interface RentRepository extends JpaRepository<Rent, Long> {

	long countByUserIdAndReturnDateIsNull(Long userId);

}
