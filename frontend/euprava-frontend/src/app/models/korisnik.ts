export interface Korisnik {
  ime: string;
  prezime: string;
  email: string;
  lozinka: string;
  rola?: Role;
}

export enum Role {
  EMPLOYER = 'EMPLOYER',
  CITIZEN = 'CITIZEN'
}
