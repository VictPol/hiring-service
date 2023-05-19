package com.hirix.repository;

import com.hirix.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findEmployeesByFullNameLike(String query);
    List<Employee> findEmployeesByFullNameLikeAndBirthdayAfter(String query, Timestamp birthday);

}
