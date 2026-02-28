package com.mancholita.backend.application;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mancholita.backend.api.dto.OrderAdminDetailDto;
import com.mancholita.backend.api.dto.OrderAdminDto;
import com.mancholita.backend.api.dto.OrderAdminListDto;
import com.mancholita.backend.api.dto.OrderCreateRequest;
import com.mancholita.backend.api.dto.OrderCreateResponse;
import com.mancholita.backend.domain.Order;
import com.mancholita.backend.domain.OrderItem;
import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.OrderRepository;
import com.mancholita.backend.infrastructure.ProductRepository;



@Service
public class OrderServiceImpl implements OrderService {

    

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // ⚠️ Configura aquí el número de WhatsApp del negocio (Colombia 57 + número)
    private static final String WHATSAPP_TO = "57XXXXXXXXXX";

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
@Transactional(readOnly = true)
public OrderAdminDetailDto getAdminDetail(String id) {

    Order o = orderRepository.findWithItemsById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

    var items = o.getItems().stream()
            .map(i -> new OrderAdminDetailDto.Item(
                    i.getProductId(),
                    i.getProductName(),
                    i.getUnitPrice(),
                    i.getQuantity(),
                    i.getLineTotal()
            ))
            .toList();

    return new OrderAdminDetailDto(
            o.getId(),
            o.getCreatedAt(),
            o.getCustomerName(),
            o.getEmail(),
            o.getPhone(),
            o.getDocumentNumber(),
            o.getDepartment(),
            o.getMunicipality(),
            o.getAddress(),
            o.getTotal(),
            items
    );
}
    @Override
    @Transactional
    public OrderCreateResponse createPublicOrder(OrderCreateRequest req) {

        String orderId = generateOrderId();

        // Validar items y calcular total con datos reales de BD
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        // 1) Consolidar items repetidos (productId -> suma de quantities)
Map<Long, Integer> consolidated = new LinkedHashMap<>();
        for (OrderCreateRequest.OrderItemRequest i : req.items) {
    if (i.productId == null) {
        throw new IllegalArgumentException("productId is required");
    }
    if (i.quantity == null || i.quantity < 1) {
        throw new IllegalArgumentException("quantity must be >= 1 for productId=" + i.productId);
    }

    consolidated.merge(i.productId, i.quantity, Integer::sum);
}

// 2) Regla opcional anti-abuso (recomendada): máximo 50 unidades por producto
for (Map.Entry<Long, Integer> e : consolidated.entrySet()) {
    if (e.getValue() > 50) {
        throw new IllegalArgumentException("quantity too large for productId=" + e.getKey());
    }
}
        for (Map.Entry<Long, Integer> entry : consolidated.entrySet()) {
    Long productId = entry.getKey();
    int qty = entry.getValue();

    Product p = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

    if (!p.isActive()) {
        throw new IllegalArgumentException("Product is inactive: " + productId);
    }

    BigDecimal lineTotal = p.getPrice().multiply(BigDecimal.valueOf(qty));
    total = total.add(lineTotal);

    orderItems.add(new OrderItem(
            p.getId(),
            p.getName(),
            p.getPrice(),
            qty,
            lineTotal
    ));
}
        Order order = new Order(
                orderId,
                LocalDateTime.now(),
                req.customerName,
                req.email,
                req.phone,
                req.documentNumber,
                req.department,
                req.municipality,
                req.address,
                total
        );

        for (OrderItem oi : orderItems) {
            order.addItem(oi);
        }

        orderRepository.save(order);

        String whatsappText = buildWhatsappText(order, orderItems);
        String whatsappUrl = "https://wa.me/" + WHATSAPP_TO + "?text=" +
                URLEncoder.encode(whatsappText, StandardCharsets.UTF_8);

        return new OrderCreateResponse(order.getId(), order.getTotal(), whatsappUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderAdminDto> listAdmin(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(o -> new OrderAdminDto(
                        o.getId(),
                        o.getCreatedAt(),
                        o.getCustomerName(),
                        o.getEmail(),
                        o.getPhone(),
                        o.getDocumentNumber(),
                        o.getDepartment(),
                        o.getMunicipality(),
                        o.getAddress(),
                        o.getTotal()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderAdminDetailDto getAdminById(String id) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        List<OrderAdminDetailDto.Item> items = o.getItems().stream()
                .map(i -> new OrderAdminDetailDto.Item(
                        i.getProductId(),
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getLineTotal()
                ))
                .toList();

        return new OrderAdminDetailDto(
                o.getId(),
                o.getCreatedAt(),
                o.getCustomerName(),
                o.getEmail(),
                o.getPhone(),
                o.getDocumentNumber(),
                o.getDepartment(),
                o.getMunicipality(),
                o.getAddress(),
                o.getTotal(),
                items
        );
    }

     @Override
    @Transactional(readOnly = true)
    public Page<OrderAdminListDto> listAdmin(String q, Pageable pageable) {
        String normalizedQ = (q == null || q.trim().isEmpty()) ? null : q.trim();
        return orderRepository.findAdminList(normalizedQ, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportExcelAll() {
        List<Order> orders = orderRepository.findAll();
        return buildExcel(orders, "Orders-All");
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportExcelDaily(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusivo
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        return buildExcel(orders, "Orders-" + date);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportAdminExcel() {
        List<Order> orders = orderRepository.findAll();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Orders");

            // Header
            Row header = sheet.createRow(0);
            String[] cols = new String[] {
                    "OrderId", "CreatedAt", "CustomerName", "Email", "Phone",
                    "DocumentNumber", "Department", "Municipality", "Address", "Total"
            };

            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            // Rows
            int r = 1;
            for (Order o : orders) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(o.getId());
                row.createCell(1).setCellValue(String.valueOf(o.getCreatedAt()));
                row.createCell(2).setCellValue(o.getCustomerName());
                row.createCell(3).setCellValue(o.getEmail());
                row.createCell(4).setCellValue(o.getPhone());
                row.createCell(5).setCellValue(o.getDocumentNumber());
                row.createCell(6).setCellValue(o.getDepartment());
                row.createCell(7).setCellValue(o.getMunicipality());
                row.createCell(8).setCellValue(o.getAddress());
                row.createCell(9).setCellValue(o.getTotal().toPlainString());
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            return workbookToBytes(wb);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to export Excel");
        }
    }

    // ----------------- helpers -----------------

    private String generateOrderId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String rnd = randomBase36(4);
        return date + rnd;
    }

    private String randomBase36(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

     private byte[] buildExcel(List<Order> orders, String sheetName) {
        try (Workbook wb = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet(sheetName);

            // Header
            Row header = sheet.createRow(0);
            String[] cols = {"OrderId", "CreatedAt", "CustomerName", "Phone", "Total"};

            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            // Rows
            int r = 1;
            for (Order o : orders) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(o.getId());
                row.createCell(1).setCellValue(String.valueOf(o.getCreatedAt()));
                row.createCell(2).setCellValue(o.getCustomerName());
                row.createCell(3).setCellValue(o.getPhone());
                row.createCell(4).setCellValue(o.getTotal().toPlainString());
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            wb.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to export Excel");
        }
    }

    private String buildWhatsappText(Order order, List<OrderItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola Mancholita, quiero hacer un pedido.").append("\n\n");
        sb.append("Pedido: ").append(order.getId()).append("\n");
        sb.append("Cliente: ").append(order.getCustomerName()).append("\n");
        sb.append("Correo: ").append(order.getEmail()).append("\n");
        sb.append("Celular: ").append(order.getPhone()).append("\n");
        sb.append("Documento: ").append(order.getDocumentNumber()).append("\n");
        sb.append("Dirección: ").append(order.getAddress()).append("\n");
        sb.append("Departamento/Municipio: ").append(order.getDepartment()).append(" / ").append(order.getMunicipality()).append("\n\n");

        sb.append("Productos:").append("\n");
        for (OrderItem i : items) {
            sb.append("- ").append(i.getProductName())
              .append(" x").append(i.getQuantity())
              .append(" = ").append(i.getLineTotal().toPlainString())
              .append("\n");
        }
        sb.append("\nTotal: ").append(order.getTotal().toPlainString());
        return sb.toString();
    }

    private byte[] workbookToBytes(Workbook wb) throws Exception {
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            wb.write(baos);
            return baos.toByteArray();
        }
    }

    @Override
@Transactional(readOnly = true)
public byte[] exportExcelRange(LocalDate from, LocalDate to) {
    if (to.isBefore(from)) {
        throw new IllegalArgumentException("to must be >= from");
    }

    LocalDateTime start = from.atStartOfDay();
    LocalDateTime endExclusive = to.plusDays(1).atStartOfDay();

    List<Order> orders = orderRepository.findByCreatedAtBetween(start, endExclusive);
    return buildExcel(orders, "Orders-" + from + "-to-" + to);
}
}