import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-email-confirm',
  templateUrl: './emailConfirm.component.html',
  styleUrls:['./emailConfirm.component.scss']
})
export class EmailConfirmComponent {

  token = '';
  public message;
  constructor(private api: ApiService
              ,protected router: Router
              ,private activateRoute: ActivatedRoute){
    this.token = activateRoute.snapshot.queryParams['token'];
    console.log(this.token);
    this.api.account.confirmEmail(this.token)
      .subscribe(
        value =>{
          console.log('sd');
          this.message = 'Поздравляем, ваша почта подтверждена';
        },
        error =>{
          console.log();
          this.message   = 'Что-то пошло не так';
        }
      );

  }

  login(){
    this.router.navigate(['']);
  }

}
