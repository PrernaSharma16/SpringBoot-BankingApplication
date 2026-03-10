import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class Login {

  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(){
    const data = {
      username: this.username,
      password: this.password
    };

    this.authService.login(data).subscribe({
      next: (token) => {
        console.log('JWT Token: ',token);
        localStorage.setItem('token', token);
        alert('Login successful :)');
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        alert('Invalid Credentials');
      }
    });
  }
}
