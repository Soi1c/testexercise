///<reference path="../../../node_modules/@angular/core/src/di/metadata.d.ts"/>
import {Http, RequestOptionsArgs} from "@angular/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";


const
  BASE = '/api',
  B64KEY = 'ZGVmYXVsdDpzZWNyZXQ=',
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
  oauthOptions = headerValue => ({
    headers: new Headers({
      Authorization: headerValue,
      'Content-Type': 'application/x-www-form-urlencoded'
    })
  }),
  uploaderOptions = header => ({
    headers: new Headers({
      Authorization: header,
      'enctype': 'multipart/form-data'
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
    const options = customOptions || anonOptions();
    return this.http
      .get(url, {...options, params})
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }

  post(url: string, request: any, customOptions?: any): Observable<any> {
    console.log("post: " + url);
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

  patch(url: string, body: any, customOptions?: any): Observable<any> {
    return this.http
      .patch(url, body, customOptions || anonOptions())
      .map(prepareSuccess)
      .catch(prepareError)
      .share();
  }
}


@Injectable()
export class ApiService{
    public account;
    public bookshelves;

    constructor(http: Http){
      this.account = new Accounts(http);
      this.bookshelves = new BookShelf(http);
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

}

class BookShelf extends HttpRequest{
  constructor(http: Http) {
    super(http);
  }

  getBookShelves(id): Observable<any>{
    return this.get(`${BASE}/mocks/bookshelves.json`);
  }

  getBooksFromShelves(): Observable<any>{
    return this.get(`${BASE}/mocks/books.json`);
  }
}
