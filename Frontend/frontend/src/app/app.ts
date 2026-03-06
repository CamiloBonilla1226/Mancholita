import { Component } from '@angular/core';
import { CatalogComponent } from './pages/catalog/catalog';
import { NavbarComponent } from './components/navbar/navbar';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CatalogComponent, NavbarComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {}