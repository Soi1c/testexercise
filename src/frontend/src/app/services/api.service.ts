///<reference path="../../../node_modules/@angular/core/src/di/metadata.d.ts"/>
import {Headers, Http, RequestOptionsArgs} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/zip';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/share';
import {forwardRef, Inject, Injectable} from "@angular/core";
import {AuthService} from "./auth.service";


const
  BASE = '/api',
  anonOptions = () => ({
    withCredentials: true,
    headers: new Headers({
      'Content-Type': 'application/json'
    })
  }),
  authorisedOptions = headerValue => ({
    headers: new Headers({
      Authorization: headerValue,
      'Content-Type': 'application/json'
    })
  }),
  prepare = response => ('_body' in response ? {
    ...response,
    body: JSON.parse(response._body)
  } : {...response}),

  prepareSuccess = response =>
    prepare(response).body,

  prepareError = response => {
    const error = prepare(response);
    return Observable.throw(error);
  };


class HttpRequest {

  constructor(public http: Http) {
  }

  get(url: string, params?: {}, customOptions?: any): Observable<any> {
    console.log('get');
    const options = customOptions || anonOptions();
    return this.http
      .get(url, {...options, params})
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }

  post(url: string, request: any, customOptions?: any): Observable<any> {
    return this.http
      .post(url, request, customOptions || anonOptions())
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }

  delete(url: string, params?: {}, customOptions?): Observable<any> {
    return this.http
      .delete(url, {...(customOptions || anonOptions()), params})
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }

  put(url: string, body: any, customOptions?: any): Observable<any> {
    return this.http
      .put(url, body, customOptions || anonOptions())
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }

}


@Injectable()
export class ApiService{
    public account;
    public bookshelves;

    constructor(http: Http
                ,@Inject(forwardRef(() => AuthService)) auth: AuthService){
      this.account = new Accounts(http);
      this.bookshelves = new BookShelf(http,auth);
    }

}


class Accounts extends HttpRequest{
    constructor(http: Http) {
      super(http);
    }

    createUser(user): Observable<any> {
      return this.post(`${BASE}/signup/submit`,user);
    }


    confirmEmail(token): Observable<any> {
      return this.get(`${BASE}/signup/confirmEmail?token=`+token);
   }

  login(user): Observable<any> {
      return this.http.post(`${BASE}/login`,user);
  }

}

class BookShelf extends HttpRequest{
  constructor(http: Http, protected auth: AuthService) {
    super(http);
  }

  getBookShelves(): Observable<any>{

    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/bookshelf`,null, authorisedOptions(token));
  }

  getBooksFromShelves(): Observable<any>{
    return this.get(`${BASE}/mocks/books.json`);
  }

  createBookShelf(shelf):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.post(`${BASE}/bookshelf`,shelf, authorisedOptions(token));
  }

}
