import { Component, OnInit } from '@angular/core';
import {AuthService, Korisnik} from "../../services/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public imagePath = 'assets/user.png';
  public user: Korisnik | null = null;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (data) => this.user = data,
      error: (err) => console.error(err)
    });
  }

}
