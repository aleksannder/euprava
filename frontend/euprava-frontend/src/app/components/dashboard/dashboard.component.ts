import { Component, OnInit } from '@angular/core';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";
import {Observable} from "rxjs";
import {AuthService} from "@auth0/auth0-angular";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  isEmployer$!: Observable<boolean>

  constructor(private userAuthUtil: AuthTokenUtil) {
    this.isEmployer$ = this.userAuthUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {
    this.isEmployer$ = this.userAuthUtil.hasPermission('mup:employer');
  }

}
