import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';

import {RouterModule, Router} from '@angular/router';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

import {AuthService} from '../../../../core/services/auth/auth-service';
import {LoginRequest} from '../../../../core/models/auth/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule],
  template: `
    <div class="flex items-center justify-center  ">
      <div class="w-full max-w-md space-y-6  p-8 rounded-lg ">
        <h1 class="text-center text-white text-3xl font-bold">Log In</h1>

        <div class="relative flex items-center">
          <span class="flex-grow border-t border-gray-700"></span>
          <span class="px-4 text-gray-400">ou</span>
          <span class="flex-grow border-t border-gray-700"></span>
        </div>

        <div *ngIf="errorMsg"
             class="p-4 mb-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400" role="alert">
          <span class="font-medium">
            {{ errorMsg }}
          </span>
        </div>

        <form [formGroup]="loginForm" (ngSubmit)="login()" class="space-y-4">

          <div>
            <label class="required block text-gray-300 mb-1">Username</label>
            <input
              formControlName="username"
              name="username"
              type="text"
              required
              [ngClass]="{
                        'w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500' : true,
                        'border-red-500' : isValidFormInput('username')

                   }"
            />
            <div *ngIf="formInputHasError('username', 'required')" class="error">
              Username is required
            </div>
            <div *ngIf="formInputHasError('username', 'minlength')" class="error">
              Username must be at least 4 characters
            </div>

            <div *ngIf="formInputHasError('username', 'maxlength')" class="error">
              Username must not over 20 characters
            </div>

            <span *ngIf="getServerFieldError('username')" class="mt-1 text-sm text-red-400 block">
              {{ getServerFieldError('username') }}
            </span>

          </div>


          <div>
            <label class="required block text-gray-300 mb-1">Password</label>
            <input
              formControlName="password"
              name="password"
              type="password"
              required
              [ngClass]="{
                        'w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500' : true,
                        'border-red-500' : isValidFormInput('password')

                   }"
            />
            <div *ngIf="formInputHasError('password', 'required')" class="error">
              Password is required
            </div>
            <div *ngIf="formInputHasError('password', 'minlength')" class="error">
              Password must be at least 8 characters
            </div>

            <div *ngIf="formInputHasError('password', 'maxlength')" class="error">
              Password must not over 20 characters
            </div>

            <span *ngIf="getServerFieldError('password')" class="mt-1 text-sm text-red-400 block">
              {{ getServerFieldError('password') }}
            </span>

          </div>

          <button
            type="submit"

            [disabled]="loginForm.invalid || loading"
            [ngClass]="{
              'w-full bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700 transition' : true,
              'cursor-not-allowed' : loginForm.invalid
            }"
          >
            {{ loading ? 'Connexion...' : 'Se connecter' }}
          </button>
        </form>

        <div class="text-center">
          <a routerLink="/forgot-password" class="text-gray-400 hover:underline">Mot de passe oublié ?</a>
        </div>
        <div class="text-center text-gray-400">
          Vous n'avez pas de compte ? <a routerLink="/auth/signup" class="underline">Inscrivez-vous ici.</a>
        </div>

      </div>
    </div>
  `

})
export class LoginComponent {

  errorMsg: string | null = null;
  loading = false;

  fieldErrors: FieldErrors = {};

  loginForm: FormGroup;

  constructor(
    private auth: AuthService,
    private router: Router,
    private fb: FormBuilder,
  ) {
    if (auth.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }

    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
    });

  }


  // Check if the form input valid (has error)
  isValidFormInput(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }

  // Check if the form input has error and get it
  formInputHasError(fieldName: string, errorType = ""): boolean {
    const field = this.loginForm.get(fieldName);
    return field ? field.hasError(errorType) && field.touched : false;
  }

  // Get server field error if exist
  getServerFieldError(fieldName: string): string | null {
    return this.fieldErrors[fieldName] || null;
  }

  login() {
    this.errorMsg = null;
    this.loading = true;
    this.fieldErrors = {};

    const req: LoginRequest = this.loginForm.value as LoginRequest;


    this.auth.login(req).subscribe({
      next: res => {
        console.log(res);
        this.loading = false;
        // res.user contains id, firstName, lastName
        this.router.navigateByUrl('/');
      },
      error: err => this.handleError(err)
    });
  }

  handleError(err: any) {
    this.loading = false;
    this.errorMsg = err.error?.message || 'Invalid credentials';

    // Handle validation errors with field-specific messages
    if (err.error?.code === "VAL_001") {
      this.fieldErrors = err.error.fieldErrors;
    }
  }
}

interface FieldErrors {
  [key: string]: string;
}
