package com.supportai.ticket.service;

import com.supportai.common.exception.ResourceNotFoundException;
import com.supportai.ticket.config.CacheConfig;
import com.supportai.ticket.domain.Ticket;
import com.supportai.ticket.domain.TicketPriority;
import com.supportai.ticket.domain.TicketStatus;
import com.supportai.ticket.dto.*;
import com.supportai.ticket.mapper.TicketMapper;
import com.supportai.ticket.repository.TicketCommentRepository;
import com.supportai.ticket.repository.TicketRepository;
import com.supportai.ticket.repository.TicketSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketCommentRepository commentRepository;

    @Transactional
    public TicketDTO createTicket(CreateTicketRequest request, String tenantId) {
        log.info("Creating ticket for tenant: {}", tenantId);
        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setTenantId(tenantId);
        ticket.setSlaBreachAt(calculateSla(ticket.getPriority()));
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }

    public Page<TicketDTO> listTickets(TicketFilter filter, String tenantId, Pageable pageable) {
        log.debug("Listing tickets for tenant {} with filters: {}", tenantId, filter);
        Specification<Ticket> spec = TicketSpecification.filterTickets(
                tenantId,
                filter.getStatuses(),
                filter.getPriorities(),
                filter.getCategory(),
                filter.getAssigneeId(),
                filter.getSearchTerm(),
                filter.getStartDate(),
                filter.getEndDate()
        );
        return ticketRepository.findAll(spec, pageable).map(ticketMapper::toDTO);
    }

    @Transactional
    @CacheEvict(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO resolveTicket(Long id, String tenantId) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        log.info("Ticket {} resolved", id);
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Cacheable(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO getTicket(Long id, String tenantId) {
        log.info("Cache miss for ticket ID: {}. Fetching from DB...", id);
        return ticketRepository.findByIdAndTenantId(id, tenantId)
                .map(ticketMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Transactional
    @CacheEvict(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO updateTicketStatus(Long id, String tenantId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        
        ticket.setStatus(newStatus);
        
        if (newStatus == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Transactional
    @CacheEvict(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO assignTicket(Long id, String tenantId, Long assigneeId) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        
        ticket.setAssigneeId(assigneeId);
        ticket.setStatus(TicketStatus.OPEN);
        
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Transactional
    @CacheEvict(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO closeTicket(Long id, String tenantId) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());
        
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Transactional
    @CacheEvict(value = CacheConfig.TICKETS_CACHE, key = "#id + '-' + #tenantId")
    public TicketDTO reopenTicket(Long id, String tenantId) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setResolvedAt(null);
        ticket.setClosedAt(null);
        
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public void addComment(Long ticketId, String tenantId, AddCommentRequest request) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        com.supportai.ticket.domain.TicketComment comment = com.supportai.ticket.domain.TicketComment.builder()
                .ticket(ticket)
                .body(request.getBody())
                .isInternal(request.isInternal())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .build();

        commentRepository.save(comment);
        log.info("Added comment to ticket {}", ticketId);
    }

    private LocalDateTime calculateSla(TicketPriority priority) {
        return switch (priority) {
            case URGENT -> LocalDateTime.now().plusHours(4);
            case HIGH -> LocalDateTime.now().plusHours(12);
            case MEDIUM -> LocalDateTime.now().plusDays(2);
            case LOW -> LocalDateTime.now().plusDays(5);
        };
    }
}