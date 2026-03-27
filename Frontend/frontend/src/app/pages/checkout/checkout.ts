
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { CartService } from '../../services/cart.service';
import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { COLOMBIA_DEPARTMENTS, ColombiaDepartmentOption } from './colombia-locations';

type CheckoutField =
  | 'customerName'
  | 'phone'
  | 'email'
  | 'documentNumber'
  | 'address'
  | 'department'
  | 'municipality';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrls: ['./checkout.scss']
})
export class CheckoutComponent implements OnInit {
  private readonly requiredFieldsMessage = 'Por favor completa todos los campos antes de enviar el pedido.';
  readonly departments: ColombiaDepartmentOption[] = COLOMBIA_DEPARTMENTS;
  availableMunicipalities: string[] = [];

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
  fieldErrors: Record<CheckoutField, string> = {
    customerName: '',
    phone: '',
    email: '',
    documentNumber: '',
    address: '',
    department: '',
    municipality: ''
  };

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
    const normalizedPhone = phone.replace(/\D/g, '');
    const normalizedEmail = email.toLowerCase();
    const departmentLabel = this.formatCoverageLabel(department);
    const municipalityLabel = this.formatCoverageLabel(municipality);

    if (!cartItems.length) {
      this.resetFieldErrors();
      this.errorMessage = 'No hay productos en el carrito. Agrega algo antes de enviar el pedido.';
      return;
    }

    if (!this.validateForm(customerName, phone, email, documentNumber, address, department, municipality)) {
      return;
    }

    this.errorMessage = '';
    this.customerName = customerName;
    this.phone = normalizedPhone;
    this.email = normalizedEmail;
    this.documentNumber = documentNumber;
    this.address = address;
    this.department = department;
    this.municipality = municipality;

    const order = {
      customerName: customerName,
      phone: normalizedPhone,
      email: normalizedEmail,
      documentNumber: documentNumber,
      address: address,
      department: departmentLabel,
      municipality: municipalityLabel,
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
        message += `Ciudad: ${municipalityLabel}\n`;
        message += `Departamento: ${departmentLabel}\n`;

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

  onDepartmentChange() {
    const selectedDepartment = this.departments.find(
      (department) => department.name === this.department
    );

    this.availableMunicipalities = selectedDepartment?.municipalities ?? [];
    this.municipality = '';
    this.clearFieldError('department');
    this.clearFieldError('municipality');
  }

  formatCoverageLabel(value: string) {
    const baseValue = value.split('\\')[0].replace(/_/g, ' ').replace(/\s+/g, ' ').trim();
    if (!baseValue) {
      return '';
    }

    return baseValue
      .toLocaleLowerCase('es-CO')
      .replace(/(^|[\s(])([\p{L}])/gu, (_, prefix: string, letter: string) => {
        return `${prefix}${letter.toLocaleUpperCase('es-CO')}`;
      });
  }

  clearFieldError(field: CheckoutField) {
    this.fieldErrors[field] = '';
    if (this.errorMessage === this.requiredFieldsMessage || this.errorMessage === 'Revisa los campos marcados y corrige la informacion.') {
      this.errorMessage = '';
    }
  }

  private resetFieldErrors() {
    Object.keys(this.fieldErrors).forEach((field) => {
      this.fieldErrors[field as CheckoutField] = '';
    });
  }

  private validateForm(
    customerName: string,
    phone: string,
    email: string,
    documentNumber: string,
    address: string,
    department: string,
    municipality: string
  ) {
    this.resetFieldErrors();
    let hasMissingRequiredField = false;

    if (!customerName) {
      this.fieldErrors.customerName = 'Ingresa tu nombre completo.';
      hasMissingRequiredField = true;
    } else if (!this.isValidName(customerName)) {
      this.fieldErrors.customerName = 'El nombre solo puede contener letras y espacios.';
    }

    if (!phone) {
      this.fieldErrors.phone = 'Ingresa un numero de telefono.';
      hasMissingRequiredField = true;
    } else if (!this.isValidPhone(phone)) {
      this.fieldErrors.phone = 'Ingresa un telefono valido. Usa solo numeros o un formato telefonico real.';
    }

    if (!email) {
      this.fieldErrors.email = 'Ingresa un correo electronico.';
      hasMissingRequiredField = true;
    } else if (!this.isValidEmail(email)) {
      this.fieldErrors.email = 'Ingresa un correo valido.';
    }

    if (!documentNumber) {
      this.fieldErrors.documentNumber = 'Ingresa tu numero de identificacion.';
      hasMissingRequiredField = true;
    } else if (!this.isValidDocument(documentNumber)) {
      this.fieldErrors.documentNumber = 'La identificacion debe contener solo numeros y tener entre 5 y 20 digitos.';
    }

    if (!address) {
      this.fieldErrors.address = 'Ingresa la direccion de entrega.';
      hasMissingRequiredField = true;
    } else if (!this.isValidAddress(address)) {
      this.fieldErrors.address = 'Ingresa una direccion mas completa y coherente.';
    }

    if (!department) {
      this.fieldErrors.department = 'Selecciona un departamento.';
      hasMissingRequiredField = true;
    }

    if (!municipality) {
      this.fieldErrors.municipality = 'Selecciona una ciudad.';
      hasMissingRequiredField = true;
    }

    const hasErrors = Object.values(this.fieldErrors).some(Boolean);
    if (hasErrors) {
      this.errorMessage = hasMissingRequiredField
        ? this.requiredFieldsMessage
        : 'Revisa los campos marcados y corrige la informacion.';
    }

    return !hasErrors;
  }

  private isValidName(value: string) {
    const onlyLetters = value.replace(/[^\p{L}\s'.-]/gu, '');
    return /^[\p{L}\s'.-]+$/u.test(value) && onlyLetters.replace(/\s+/g, '').length >= 3;
  }

  private isValidPhone(value: string) {
    const digits = value.replace(/\D/g, '');
    return /^[0-9+\s()-]+$/.test(value) && digits.length >= 7 && digits.length <= 15;
  }

  private isValidEmail(value: string) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i.test(value);
  }

  private isValidDocument(value: string) {
    return /^\d{5,20}$/.test(value);
  }

  private isValidAddress(value: string) {
    const hasLetters = /[\p{L}]/u.test(value);
    return value.length >= 8 && hasLetters;
  }

}
