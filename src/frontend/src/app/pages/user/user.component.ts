import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'user-home-app',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {
  constructor(protected router: Router){

  }
  public index = 1;

  logout(){
    this.router.navigate(['']);
  }
}
