import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { jwtInterceptor } from './interceptors/jwt-interceptor';
import { Login } from './login/login';
import { Dashboard } from './dashboard/dashboard';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter([
      {path: '', component: Login},
      {path: 'dashboard', component: Dashboard}
    ]),
    provideHttpClient(withInterceptors([jwtInterceptor]))
  ]
};
