import { Component, OnInit } from '@angular/core';
import {Korisnik} from "../../models/korisnik";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public imagePath = 'assets/user.png';
  public user: Korisnik | null = null;

  constructor() { }

  ngOnInit(): void {
    //todo rework get user

    // this.authService.getCurrentUser().subscribe({
    //   next: (data) => this.user = data,
    //   error: (err) => console.error(err)
    // });
  }

}
