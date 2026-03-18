
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
  private readonly requiredFieldsMessage = 'Por favor completa todos los campos antes de enviar el pedido.';

  ngOnInit(): void {
    // Always start with a clean checkout form. Previous orders should not persist.
  }

  @Output() orderCompleted = new EventEmitter<void>();

  customerName = '';
  phone = '';
  email = '';
  documentNumber = '';
  address = '';
  department = '';
  municipality = '';

  errorMessage = '';

  constructor(
    private orderService: OrderService,
    public cartService: CartService
  ) { }

  submitOrder() {
    const cartItems = this.cartService.getItems();
    const customerName = this.customerName.trim();
    const phone = this.phone.trim();
    const email = this.email.trim();
    const documentNumber = this.documentNumber.trim();
    const address = this.address.trim();
    const department = this.department.trim();
    const municipality = this.municipality.trim();

    if (!cartItems.length) {
      this.errorMessage = 'No hay productos en el carrito. Agrega algo antes de enviar el pedido.';
      return;
    }

    if (
      !customerName ||
      !phone ||
      !email ||
      !documentNumber ||
      !address ||
      !department ||
      !municipality
    ) {
      this.errorMessage = this.requiredFieldsMessage;
      return;
    }

    this.errorMessage = '';
    this.customerName = customerName;
    this.phone = phone;
    this.email = email;
    this.documentNumber = documentNumber;
    this.address = address;
    this.department = department;
    this.municipality = municipality;

    const order = {
      customerName: customerName,
      phone: phone,
      email: email,
      documentNumber: documentNumber,
      address: address,
      department: department,
      municipality: municipality,
      items: cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    };


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
