import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService, SignupRequest} from '../../../../core/services/auth-service';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule],
  template: `
    <div class="flex items-center justify-center">
      <div class="w-full max-w-md space-y-6 p-8 rounded-lg">
        <h1 class="text-center text-white text-3xl font-bold">Inscription</h1>

        <h1>
          {{ errorMsg }}
        </h1>

        <div class="relative flex items-center">
          <span class="flex-grow border-t border-gray-700"></span>
          <span class="px-4 text-gray-400">ou</span>
          <span class="flex-grow border-t border-gray-700"></span>
        </div>

        <form [formGroup]="registrationForm" (ngSubmit)="signup()" class="space-y-4">

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
              Username must be at least 3 characters
            </div>

            <div *ngIf="formInputHasError('username', 'maxlength')" class="error">
              Username must not over 20 characters
            </div>

            <span *ngIf="getServerFieldError('username')" class="mt-1 text-sm text-red-400 block">
              {{ getServerFieldError('username') }}
            </span>

          </div>
          <div>
            <label class="required block text-gray-300 mb-1">Email</label>
            <input
              formControlName="email"
              name="email"
              type="text"
              required
              [ngClass]="{
                        'w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500' : true,
                        'border-red-500' : isValidFormInput('email')

                   }"
            />
            <div *ngIf="formInputHasError('email', 'required')" class="error">
              Email is required
            </div>
            <div *ngIf="formInputHasError('email', 'email')" class="error">
              Email must be valid email
            </div>

            <span *ngIf="getServerFieldError('email')" class="mt-1 text-sm text-red-400 block">
              {{ getServerFieldError('email') }}
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
            [disabled]="registrationForm.invalid || loading"
            [ngClass]="{
              'w-full bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700 transition' : true,
              'cursor-not-allowed' : registrationForm.invalid
            }"

          >
            S’inscrire
          </button>
        </form>


        <div class=" text-center">
          <a routerLink="/forgot-password" class="text-gray-400 hover:underline">Mot de passe oublié ?
          </a>
        </div>
        <div class="text-center text-gray-400">
          Vous avez déjà un compte ? <a routerLink="/auth/login" class="underline">
          Connexion
        </a>
        </div>
      </div>
    </div>

  `
})
export class SignupComponent {
  // Global server error message
  errorMsg: string | null = null;
  // List of all fields error from the server
  fieldErrors: FieldErrors = {};

  loading = false;

  registrationForm: FormGroup;


  constructor(
    private auth: AuthService,
    private router: Router,
    private fb: FormBuilder,
  ) {

    if (auth.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }

    this.registrationForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
    });


  }

  // Check if the form input valid (has error)
  isValidFormInput(fieldName: string): boolean {
    const field = this.registrationForm.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }
  // Check if the form input has error and get it
  formInputHasError(fieldName: string, errorType = ""): boolean {
    const field = this.registrationForm.get(fieldName);
    return field ? field.hasError(errorType) && field.touched : false;
  }
  // Get server field error if exist
  getServerFieldError(fieldName: string): string | null {
    return this.fieldErrors[fieldName] || null;
  }

  // Handle sign up
  signup() {
    if (this.registrationForm.valid) {
      this.errorMsg = null;
      this.loading = true;
      this.fieldErrors = {};

      const req: SignupRequest = this.registrationForm.value;

      this.auth.register(req).subscribe({
        next: user => {
          this.loading = false;
          this.router.navigateByUrl('/auth/login');
        },
        error: err => this.handleError(err)
      });
    } else {
      // Mark all fields as touched to show validation errors
      this.registrationForm.markAllAsTouched();
    }
  }
  // Handle the case we have issue from the server
  handleError(err: any) {
    console.log(err)
    this.loading = false;
    this.errorMsg = err.error.message;
    if (err.error.hasOwnProperty("fieldErrors")) {
      this.fieldErrors = err.error.fieldErrors;
    }
  }


}

interface FieldErrors {
  [key: string]: string;
}
