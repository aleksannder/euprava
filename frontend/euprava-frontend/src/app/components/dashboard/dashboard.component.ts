import { Component, OnInit } from '@angular/core';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  rola: Role = Role.EMPLOYER;

  constructor(private roleService: RoleService) {}

  ngOnInit(): void {
    this.roleService.getRoles$().subscribe(roles => {
      const role = roles[0];

      switch (role) {
        case 'EMPLOYER':
          this.rola = Role.EMPLOYER;
          break;
        case 'CITIZEN':
          this.rola = Role.CITIZEN;
          break;
        default:
          this.rola = Role.CITIZEN;
      }
    });

  }

  get isEmployer(): boolean {
    return this.rola === Role.EMPLOYER;
  }
}
