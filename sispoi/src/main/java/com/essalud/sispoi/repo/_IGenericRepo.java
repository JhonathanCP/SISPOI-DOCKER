package com.essalud.sispoi.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface _IGenericRepo<T, ID> extends JpaRepository<T, ID> {

}
