export interface RegisterUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: Role;
}

export interface RegisterUserResponse {
  success: boolean;
}

export enum Role {
  ADMIN = 'ADMIN',
  ANALYST = 'ANALYST',
  CITIZEN = 'CITIZEN',
}
