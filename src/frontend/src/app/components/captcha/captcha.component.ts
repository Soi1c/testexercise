import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-captcha',
  templateUrl: './captcha.component.html',
  styleUrls: ['./captcha.component.scss']
})
export class CaptchaComponent implements OnInit{

  code = '';
  captcha = '';
  captchaForm;

  setCaptcha() {
    this.code = '';
    let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (let i = 0; i < 6; i++) {
      this.code += possible.charAt(Math.floor(Math.random() * possible.length));
    }
  }


  public сheckCaptcha():boolean {
    if (this.code == this.captcha) {
      return true;
    } else {
      return false;
    }
  }

  ngOnInit(){
    this.captchaForm = new FormGroup({
      captcha: new FormControl(''),
    });
    this.setCaptcha()
  }
}
