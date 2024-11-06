package ru.axothy.airline.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.axothy.airline.model.db.Town;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

}
