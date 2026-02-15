package com.supportai.ticket.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.supportai.common.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketStatus status = TicketStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketPriority priority = TicketPriority.MEDIUM;

    private String category;
    private String sentiment;

    private Long assigneeId;
    
    @Column(nullable = false)
    private Long requesterId;

    @Column(nullable = false)
    private String requesterEmail;

    @Column(nullable = false)
    private String tenantId;

    private LocalDateTime slaBreachAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

   
    private boolean aiProcessed;
    
    @Column(columnDefinition = "TEXT")
    private String aiResponse;
    
    private Double aiConfidence;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TicketComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TicketAttachment> attachments = new ArrayList<>();
}

