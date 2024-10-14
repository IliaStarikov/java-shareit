package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select distinct ir " +
            "from Request ir " +
            "left join fetch ir.requestor " +
            "left join fetch ir.items it " +
            "left join fetch it.owner " +
            "where ir.requestor.id = ?1 " +
            "order by ir.created asc ")
    List<Request> findByRequestor_Id(long requestorId);

    @Query("select ir " +
            "from Request ir " +
            "order by ir.created asc ")
    List<Request> findAllOrderByDate();

}
