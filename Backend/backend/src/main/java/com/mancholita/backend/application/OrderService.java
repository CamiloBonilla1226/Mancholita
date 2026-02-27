package com.mancholita.backend.application;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mancholita.backend.api.dto.OrderAdminDetailDto;
import com.mancholita.backend.api.dto.OrderAdminDto;
import com.mancholita.backend.api.dto.OrderAdminListDto;
import com.mancholita.backend.api.dto.OrderCreateRequest;
import com.mancholita.backend.api.dto.OrderCreateResponse;


public interface OrderService {

    OrderCreateResponse createPublicOrder(OrderCreateRequest req);

    Page<OrderAdminDto> listAdmin(Pageable pageable);

    OrderAdminDetailDto getAdminById(String id);

    byte[] exportAdminExcel();

    Page<OrderAdminListDto> listAdmin(String q, Pageable pageable);

    byte[] exportExcelAll();
    byte[] exportExcelDaily(LocalDate date);
}