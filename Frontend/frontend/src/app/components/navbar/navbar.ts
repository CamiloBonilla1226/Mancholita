import { Component, DoCheck } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class NavbarComponent implements DoCheck {

  cartCount = 0;

  constructor(private cartService: CartService) {}

  ngDoCheck(): void {
    this.cartCount = this.cartService.getCount();
  }

}