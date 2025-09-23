import {Component, Inject} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {AuthService} from '@auth0/auth0-angular';

@Component({
  selector: 'app-logout-button',
  standalone: true,
  imports: [],
  templateUrl: './logout-button.component.html',
  styleUrl: './logout-button.component.scss'
})
export class LogoutButtonComponent {

    constructor(@Inject(DOCUMENT) public document: Document, public auth: AuthService) {
    }
}
