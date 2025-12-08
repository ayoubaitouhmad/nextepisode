export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  emailVerified: boolean;
  isActive: boolean;
  createdAt: string;
  lastLogin: string | null;
}
