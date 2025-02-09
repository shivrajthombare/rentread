package com.example.rentread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rentread.entity.Book;
import com.example.rentread.entity.Rent;
import com.example.rentread.entity.User;
import com.example.rentread.repository.BookRepository;
import com.example.rentread.repository.RentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentService {
	
	@Autowired
	RentRepository rentRepository;
	
	


	@Autowired
	 BookRepository bookRepository;

	   

	    public Rent rentBook(Long userId, Long bookId) {
	        long activeRentals = rentRepository.countByUserIdAndReturnDateIsNull(userId);
	        if (activeRentals >= 2) {
	            throw new RuntimeException("Cannot rent more than 2 books at a time");
	        }

	        Book book = bookRepository.findById(bookId)
	                .orElseThrow(() -> new RuntimeException("Book not found"));
	        if (!book.isAvailabilityStatus()) {
	            throw new RuntimeException("Book is not available for rent");
	        }

	        book.setAvailabilityStatus(false);
	        bookRepository.save(book);

	        Rent rental = new Rent(
	               new User(userId),
	                new Book(book),
	                LocalDate.now());

	        return rentRepository.save(rental);
	    }

	    public Rent returnBook(Long rentalId) {
	        Rent rent = rentRepository.findById(rentalId)
	                .orElseThrow(() -> new RuntimeException("Rental not found"));
	        if (rent.getReturnDate() != null) {
	            throw new RuntimeException("Book already returned");
	        }

	        rent.setReturnDate(LocalDate.now());
	        rent.getBook().setAvailabilityStatus(true);
	        bookRepository.save(rent.getBook());

	        return rentRepository.save(rent);
	    }
	}



