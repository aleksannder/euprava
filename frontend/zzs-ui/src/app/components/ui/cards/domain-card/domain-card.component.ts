import {Component, Input} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {Router} from '@angular/router';

@Component({
  selector: 'app-domain-card',
  standalone: true,
    imports: [
        MatCard,
        MatIcon
    ],
  templateUrl: './domain-card.component.html',
  styleUrl: './domain-card.component.scss'
})
export class DomainCardComponent {
  @Input() domain!: any;

  constructor(private router: Router) {}

  goToDomain(domainCode: string) {
    this.router.navigate([`/domains/${domainCode}`]);
  }
}
