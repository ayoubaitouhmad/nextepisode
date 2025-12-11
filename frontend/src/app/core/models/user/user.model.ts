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



export interface UserProfile {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  isDirty: boolean;
  avatar?: string;
  bio?: string;
  location?: string;
  website?: string;
  phone?: string;
  dateOfBirth?: string;
  preferredLanguage?: string;
  timezone?: string;
  notificationsEnabled?: boolean;
  profileVisibility: boolean;
  createdAt?: string;
}



export interface ProfileUpdateRequest {
  firstName?: string;
  lastName?: string;
  avatar?: string;
  bio?: string;
  location?: string;
  website?: string;
  phone?: string;
  dateOfBirth?: string;
  preferredLanguage?: string;
  timezone?: string;
  notificationsEnabled?: boolean;
}
