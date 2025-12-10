
export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}


export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  message: string;
  user: {
    id: number;
    email: string;
    username: string
  };
}

export interface PasswordChangeRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}
