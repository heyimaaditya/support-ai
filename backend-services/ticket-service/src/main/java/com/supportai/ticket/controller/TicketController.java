package com.supportai.ticket.controller;

import com.supportai.common.dto.ApiResponse;
import com.supportai.ticket.dto.*;
import com.supportai.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "Endpoints for creating and managing support tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<ApiResponse<TicketDTO>> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        TicketDTO response = ticketService.createTicket(request, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Ticket created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    public ResponseEntity<ApiResponse<TicketDTO>> getTicket(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        TicketDTO response = ticketService.getTicket(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Ticket retrieved"));
    }

    @GetMapping
    @Operation(summary = "List tickets with filtering and pagination")
    public ResponseEntity<ApiResponse<Page<TicketDTO>>> listTickets(
            TicketFilter filter,
            Pageable pageable,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        Page<TicketDTO> response = ticketService.listTickets(filter, tenantId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Tickets retrieved"));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign a ticket to an agent")
    public ResponseEntity<ApiResponse<TicketDTO>> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        TicketDTO response = ticketService.assignTicket(id, tenantId, request.getAssigneeId());
        return ResponseEntity.ok(ApiResponse.success(response, "Ticket assigned successfully"));
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "Mark a ticket as resolved")
    public ResponseEntity<ApiResponse<TicketDTO>> resolveTicket(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        TicketDTO response = ticketService.resolveTicket(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Ticket resolved successfully"));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Add a comment to a ticket")
    public ResponseEntity<ApiResponse<Void>> addComment(
            @PathVariable Long id,
            @Valid @RequestBody AddCommentRequest request,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        ticketService.addComment(id, tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Comment added successfully"));
    }
    
    @PostMapping("/{id}/close")
    @Operation(summary = "Permanently close a ticket")
    public ResponseEntity<ApiResponse<TicketDTO>> closeTicket(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        TicketDTO response = ticketService.closeTicket(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Ticket closed"));
    }
}