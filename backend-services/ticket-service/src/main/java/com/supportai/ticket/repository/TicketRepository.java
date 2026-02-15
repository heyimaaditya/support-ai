package com.supportai.ticket.repository;

import com.supportai.ticket.domain.Ticket;
import com.supportai.ticket.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByIdAndTenantId(Long id, String tenantId);
    Page<Ticket> findByTenantIdAndStatus(String tenantId, TicketStatus status, Pageable pageable);
    List<Ticket> findByAssigneeIdAndStatusIn(Long assigneeId, List<TicketStatus> statuses);
    @Query("SELECT t FROM Ticket t WHERE t.status NOT IN ('RESOLVED', 'CLOSED') AND t.slaBreachAt < :now")
    List<Ticket> findSlaBreached(LocalDateTime now);

    long countByTenantIdAndStatus(String tenantId, TicketStatus status);
}