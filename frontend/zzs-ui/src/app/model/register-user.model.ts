import {Region} from './enums/region.enum';

export interface RegisterUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: Role;
  region: Region;
}


export enum Role {
  ADMIN = 'ADMIN',
  ANALYST = 'ANALYST',
  CITIZEN = 'CITIZEN',
}
