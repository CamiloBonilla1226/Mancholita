package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderCreateResponse createPublicOrder(OrderCreateRequest req);

    Page<OrderAdminDto> listAdmin(Pageable pageable);

    OrderAdminDetailDto getAdminById(String id);

    byte[] exportAdminExcel();
}