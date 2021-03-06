import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { MASKS } from '../../libs/masks';
import {CaptchaComponent} from '../captcha/captcha.component';
import {ApiService} from "../../services/api.service";
import {error} from "util";
import { Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {validate} from "codelyzer/walkerFactory/walkerFn";


@Component({
    selector: 'app-auth-dialog',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit{
  @ViewChild(CaptchaComponent) captcha: CaptchaComponent;

  constructor(private api: ApiService, protected router: Router, protected authService: AuthService){}

  ngOnInit() {

    this.form = new FormGroup({
      password: new FormControl('',[Validators.required]),
      username: new FormControl('',[Validators.required, Validators.pattern(/^[-.\w]+@([\w-]+\.)+[\w-]{2,12}$/)])
    });
    this.clearForm();
  }


  public form: FormGroup;
  MASKS = MASKS;
  errors = '';
  confirmMessage = '';
  status = {
    login: true,
    forgotPassword: false,
    forgotLogin: false,
    registration: false,
  }

  confirmEmailState = false;

  public togglePassRecovery(e: Event) {
    e.preventDefault();
    this.clearForm();
    this.status = {
      login: false,
      forgotPassword: true,
      forgotLogin: false,
      registration: false

    }
  }

  public toggleLoginRecovery(e: Event) {
    e.preventDefault();
    this.clearForm();
    this.status = {
      login: false,
      forgotPassword: false,
      forgotLogin: true,
      registration: false

    }
  }

  public toggleLoginForm() {
    this.clearForm();
    this.errors = '';
    this.status = {
      login: true,
      forgotPassword: false,
      forgotLogin: false,
      registration: false

    }
  }

  public toggleRegistrationForm() {
    this.clearForm();
    this.errors = '';

    this.status = {
      login: false,
      forgotPassword: false,
      forgotLogin: false,
      registration: true

    }
  }


  authorize(){
    console.log("authorize" + this.form.value.username);
    this.api.account.createUser({email: this.form.value.username, password: this.form.value.password })
      .subscribe(
        (response) =>{},
        (error) =>{}
      );

  }

  createNewUser(){
    if(this.captcha.сheckCaptcha()){
      console.log("Succsess");
      this.errors = '';
      this.authorize();
      this.toggleLoginForm();
    }else{
      this.errors = 'Вы робот';
      this.captcha.setCaptcha();
      this.captcha.captcha = '';
    }
  }

  sendPasswordOnEmail(){
    this.status = {
      login: true,
      forgotPassword: false,
      forgotLogin: false,
      registration: false
    };
    console.log("send password on email");
  }
  login(){

    this.api.account.login({email: this.form.value.username, password: this.form.value.password })
      .subscribe(
        (response) =>{
          let headersValue = response.headers;
          this.authService.access_token = headersValue.get('authorization');
          this.router.navigate(['/user']);
        },
        (error) => {
          this.errors = 'Wrong login or password';
          console.log(error);
        }
    );

  }

  clearForm(){
    this.form.reset();
  }


}


