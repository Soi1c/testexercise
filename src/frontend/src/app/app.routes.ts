import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from './components/auth/auth.component';
import {UserComponent} from './pages/user/user.component';
import {NgModule} from '@angular/core';
import {EmailConfirmComponent} from "./components/auth/emailConfirm/emailConfirm.component";
import {AuthService} from "./services/auth.service";

const ROUTES: Routes = [
  {
    path: '',
    component: AuthComponent
  },
  {
    canActivate: [AuthService],
    path: 'user',
    component: UserComponent,
    data: {title: 'Личный кабинет пользователя'}
  },
  {
    path: 'signup',
    component: EmailConfirmComponent,
    children:[
      {
      path: 'confirmEmail',
      component: EmailConfirmComponent
    }
    ]
  }

]

@NgModule({
  imports: [RouterModule.forRoot(ROUTES)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
