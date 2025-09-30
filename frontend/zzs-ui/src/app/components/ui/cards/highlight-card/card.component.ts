import {Component, Input} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {NgForOf} from "@angular/common";
import {Router} from '@angular/router';

@Component({
  selector: 'app-cards',
  standalone: true,
    imports: [
        MatCard,
        MatIcon,
        NgForOf
    ],
  template: `
    <mat-card class="highlight-card" (click)="goToDomain(domain)">
      <mat-icon class="highlight-icon"> {{ icon }}</mat-icon>
      <h2> {{ value }}</h2>
      <p> {{ label }}</p>
    </mat-card>
  `,
  styles: [`
    .highlight-card {
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.15);
      transition: transform 0.2s ease-in-out;

      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start; // sve ide od vrha, nema sudaranja
      text-align: center;
      min-height: 180px; // obezbedi dovoljno prostora

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.25);
      }

      .highlight-icon {
        color: #1976d2;
      }

      h2 {
        margin: 8px 0 4px;
        font-size: 1.6rem;
        font-weight: bold;
        color: #2c3e50;
      }

      p {
        font-size: 0.95rem;
        color: #666;
      }
    }

  `]
})
export class CardComponent {
  @Input() icon?: string;
  @Input() value!: string;
  @Input() label!: string;
  @Input() domain!: string;

  constructor(private router: Router) {
  }

  goToDomain(domainCode: string) {
    this.router.navigate([`/domains/${domainCode}`])
  }
}
