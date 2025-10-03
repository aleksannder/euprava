import {Region} from './enums/region.enum';

export interface RegisterUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: Date;
  city: string;
  address: string;
  gender: string;
  password: string;
  role: Role;
  region: Region;
}


export enum Role {
  ADMIN = 'ADMIN',
  ANALYST = 'ANALYST',
  CITIZEN = 'CITIZEN',
}

export interface UserInfo {
  firstName: string;
  lastName: string;
  email: string;
  city: string;
  address: string;
  region: Region;
  gender: string;
  jmbg: string;
}

export interface UserUpdateRequest {
  firstName: string;
  lastName: string;
  gender: string;
  region: Region;
  city: string;
  address: string;
}
