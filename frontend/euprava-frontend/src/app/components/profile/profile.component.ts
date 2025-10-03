import { Component, OnInit } from '@angular/core';
import {Korisnik} from "../../models/korisnik";
import {UsersService} from "../../services/users.service";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public imagePath = 'assets/user.png';
  public user: Korisnik | null = null;

  constructor(private usersService: UsersService, private authUtil: AuthTokenUtil) { }

  ngOnInit(): void {
  this.authUtil.getUserProfile().subscribe((profile) => {
    this.usersService.getCurrentUser(profile.email).subscribe({
      next: (data) => this.user = data,
      error: (err) => {console.log(err)}
    })
  })
  }

}
