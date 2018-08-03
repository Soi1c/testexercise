import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from './components/auth/auth.component';
import {UserComponent} from './pages/user/user.component';
import {NgModule} from '@angular/core';

const ROUTES: Routes = [
  {
    path: '',
    component: AuthComponent
  },
  {
    path: 'user',
    component: UserComponent,
    data: {title: 'Личный кабинет пользователя'}
  }
]

@NgModule({
  imports: [RouterModule.forRoot(ROUTES)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
