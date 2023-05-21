package com.hirix.repository;

import com.hirix.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findCompaniesByFullTitleLike(String query);
}
