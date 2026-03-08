import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrls: ['./checkout.scss']
})
export class CheckoutComponent {

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
  ) {}

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

    console.log('ORDEN A ENVIAR:', order);

    this.orderService.createOrder(order).subscribe({
      next: (response) => {

        console.log('ORDEN CREADA:', response);

        let message = `Pedido Mancholita\n\n`;

        cartItems.forEach(item => {
          message += `${item.product.name} x${item.quantity}\n`;
        });

        message += `\nCliente:\n`;
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

      },
      error: (err) => {
        console.error('Error creando pedido', err);
      }
    });

  }

}