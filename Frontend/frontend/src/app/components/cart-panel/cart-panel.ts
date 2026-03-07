import { Component, DoCheck } from '@angular/core';
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

  constructor(private cartService: CartService) {}

  ngDoCheck(): void {
    this.items = this.cartService.getItems();
    this.total = this.cartService.getTotal();
  }

  remove(productId: number) {
  this.cartService.removeProduct(productId);
}

}