import {Region} from "./enums/region.enum";

export interface Korisnik {
  ime: string;
  prezime: string;
  email: string;
  lozinka: string;
  region: Region;
  rola?: Role;
}

export enum Role {
  EMPLOYER = 'EMPLOYER',
  CITIZEN = 'CITIZEN'
}
