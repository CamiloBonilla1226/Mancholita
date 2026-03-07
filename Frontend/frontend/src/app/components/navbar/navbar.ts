import { Component, DoCheck, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class NavbarComponent implements DoCheck {

  cartCount = 0;

  @Output() cartClicked = new EventEmitter<void>();

  constructor(private cartService: CartService) {}

  ngDoCheck(): void {
    this.cartCount = this.cartService.getCount();
  }

  openCart() {
    this.cartClicked.emit();
  }

}