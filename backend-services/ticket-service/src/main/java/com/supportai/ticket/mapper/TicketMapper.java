package com.supportai.ticket.mapper;

import com.supportai.ticket.domain.Ticket;
import com.supportai.ticket.dto.CreateTicketRequest;
import com.supportai.ticket.dto.TicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketDTO toDTO(Ticket ticket);

    @Mapping(target = "status", constant = "NEW")
    Ticket toEntity(CreateTicketRequest request);
}