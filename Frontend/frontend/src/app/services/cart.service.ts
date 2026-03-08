import { Injectable } from '@angular/core';
import { CartItem } from '../models/cart-item';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private items: CartItem[] = [];

  addProduct(product: Product) {
    const existing = this.items.find(i => i.product.id === product.id);

    if (existing) {
      existing.quantity++;
    } else {
      this.items.push({
        product: product,
        quantity: 1
      });
    }

    console.log('Carrito:', this.items);
  }

  getItems() {
    return this.items;
  }

  getCount(): number {
    return this.items.reduce((total, item) => total + item.quantity, 0);
  }

  getTotal(): number {
    return this.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
  }
  removeProduct(productId: number) {
    const item = this.items.find(i => i.product.id === productId);

    if (!item) return;

    if (item.quantity > 1) {
      item.quantity--;
    } else {
      this.items = this.items.filter(i => i.product.id !== productId);
    }
  }
  clearCart() {
    this.items = [];
  }

}