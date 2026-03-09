
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { CartService } from '../../services/cart.service';
import { Component, EventEmitter, Output, OnInit } from '@angular/core';


@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrls: ['./checkout.scss']
})
export class CheckoutComponent implements OnInit {
  ngOnInit(): void {
  const savedData = localStorage.getItem('checkoutData');

  if (savedData) {
    const data = JSON.parse(savedData);

    this.customerName = data.customerName || '';
    this.phone = data.phone || '';
    this.email = data.email || '';
    this.documentNumber = data.documentNumber || '';
    this.address = data.address || '';
    this.department = data.department || '';
    this.municipality = data.municipality || '';
  }
}

  @Output() orderCompleted = new EventEmitter<void>();

  customerName = '';
  phone = '';
  email = '';
  documentNumber = '';
  address = '';
  department = '';
  municipality = '';

  constructor(
    private orderService: OrderService,
    private cartService: CartService
  ) { }

  submitOrder() {

    const cartItems = this.cartService.getItems();

    const order = {
      customerName: this.customerName,
      phone: this.phone,
      email: this.email,
      documentNumber: this.documentNumber,
      address: this.address,
      department: this.department,
      municipality: this.municipality,
      items: cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    };

    localStorage.setItem('checkoutData', JSON.stringify({
  customerName: this.customerName,
  phone: this.phone,
  email: this.email,
  documentNumber: this.documentNumber,
  address: this.address,
  department: this.department,
  municipality: this.municipality
}));

    console.log('ORDEN A ENVIAR:', order);

    this.orderService.createOrder(order).subscribe({
      next: (response) => {

        console.log('ORDEN CREADA:', response);

        const cartItems = this.cartService.getItems();

        const orderId = response.id ?? response.orderId ?? 'N/A';

        let message = `Pedido Mancholita\n`;
        message += `Orden #${orderId}\n\n`;

        let total = 0;

        cartItems.forEach(item => {

          const price = item.product.price;
          const subtotal = price * item.quantity;

          total += subtotal;

          message += `${item.product.name}\n`;
          message += `Cantidad: ${item.quantity}\n`;
          message += `Precio: $${price.toLocaleString('es-CO')}\n\n`;

        });

        

        message += `TOTAL: $${total.toLocaleString('es-CO')}\n\n`;

        message += `Cliente:\n`;
        message += `Nombre: ${this.customerName}\n`;
        message += `Teléfono: ${this.phone}\n`;
        message += `Correo: ${this.email}\n`;
        message += `Identificación: ${this.documentNumber}\n`;
        message += `Dirección: ${this.address}\n`;
        message += `Ciudad: ${this.municipality}\n`;
        message += `Departamento: ${this.department}\n`;

        const phoneNumber = '573153504020';

        const url = `https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`;

        window.open(url, '_blank');
        this.cartService.clearCart();
        this.orderCompleted.emit();



      },
      error: (err) => {
        console.error('Error creando pedido', err);
      }
    });

  }

}