import { Component, DoCheck, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';
import { CartItem } from '../../models/cart-item';

@Component({
  selector: 'app-cart-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart-panel.html',
  styleUrls: ['./cart-panel.scss']
})
export class CartPanelComponent implements DoCheck {

  items: CartItem[] = [];
  total = 0;

  @Output() checkoutClicked = new EventEmitter<void>();
  @Output() closePanel = new EventEmitter<void>();

  constructor(private cartService: CartService) {}

  ngDoCheck(): void {
    this.items = this.cartService.getItems();
    this.total = this.cartService.getTotal();
  }

  remove(productId: number) {
    this.cartService.removeProduct(productId);
  }

  increase(product: any) {
    this.cartService.addProduct(product);
  }

  decrease(productId: number) {
    this.cartService.removeProduct(productId);
  }

  goToCheckout() {
    this.checkoutClicked.emit();
  }

  closeCart() {
    this.closePanel.emit();
  }
}