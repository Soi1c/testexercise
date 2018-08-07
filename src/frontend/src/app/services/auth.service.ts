import {LocalStorageService} from "./local-storage.service";
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";

@Injectable()
export class AuthService{
  constructor(private storage: LocalStorageService) {}
    private state = {
      access_token: null
    };

  canActivate(route: ActivatedRouteSnapshot,
              routerstate: RouterStateSnapshot): boolean {
    this.state.access_token = this.storage.getItem('ospAccessToken');
    return !!this.state.access_token;
  }
  set access_token(value) {
    this.state.access_token = value;
    if (!!value) {
      this.storage.setItem('ospAccessToken', value);
    } else {
      this.storage.removeItem('ospAccessToken');
    }
  }
  get access_token() {
    return this.state.access_token;
  }

}
