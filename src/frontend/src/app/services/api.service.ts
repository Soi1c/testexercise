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
import {httpFactory} from "@angular/http/src/http_module";
import {observableToBeFn} from "rxjs/testing/TestScheduler";


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
  uploaderOptions = header => ({
    headers: new Headers({
      Authorization: header,
      'enctype': 'multipart/form-data',
    })
  }),
  prepare = response => ('_body' in response ? {
    ...response,
    body: JSON.parse(response._body)
  } : {...response}),

  prepareSuccess = response =>{
    if(response._body != ""){
      return prepare(response).body;
    }
    let res = '{"status" :"ok"}';
    return res;
  },


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
    public book;
    public user;
    constructor(http: Http
                ,@Inject(forwardRef(() => AuthService)) auth: AuthService){
      this.account = new Accounts(http);
      this.bookshelves = new BookShelf(http,auth);
      this.book = new Book(http, auth);
      this.user = new User(http, auth);
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

  getBookShelves(filter): Observable<any>{

    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/bookshelf`,filter, authorisedOptions(token));
  }

  deleteBookshelfById(id): Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.delete(`${BASE}/bookshelf/`+id,null, authorisedOptions(token));
  }

  createBookShelf(shelf):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.post(`${BASE}/bookshelf`,shelf, authorisedOptions(token));
  }

  getBooksFromBookshelf(id){
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/bookshelf/`+ id,null, authorisedOptions(token));
  }

}

class Book extends HttpRequest{
  constructor(http: Http, protected auth: AuthService) {
    super(http);
  }

  createBook(book):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.post(`${BASE}/books`,book, authorisedOptions(token));
  }

  deleteBook(id):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.delete(`${BASE}/books/`+id,null, authorisedOptions(token));
  }

  updateBook(id, book):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.put(`${BASE}/books/`+id,book, authorisedOptions(token));
  }

  changeBookShelf(bookId, shelfId):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.put(`${BASE}/books/`+bookId+`/movetoanotherbookshelf/`+ shelfId, null, authorisedOptions(token));
  }

  uploadBook(id, file):Observable<any>{
    const token = `${this.auth.access_token}`;
    let form = new FormData();
    form.append('file', file);
    return this.post(`${BASE}/books/`+id,form, uploaderOptions(token));
  }

  continueReading(id):Observable <any>{
    const token = `${this.auth.access_token}`;
    return this.get(`${BASE}/books/`+id+`/continuereading`,null, uploaderOptions(token));
  }

  getPage(bookId, pageId):Observable <any>{
    const token = `${this.auth.access_token}`;
    return this.get(`${BASE}/books/`+bookId+`/pages/` + pageId,null, uploaderOptions(token));
  }
}

class User extends HttpRequest {
  constructor(http: Http, protected auth: AuthService) {
    super(http);
  }

  getAllUsers():Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users`, null, authorisedOptions(token));
  }

  getUserBookshelves(userId):Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/` + userId, null, authorisedOptions(token));
  }

  getAllBooksFromAnotherUserBookshelf(userId, bookshelfId):Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/` + userId+`/bookshelves/`+bookshelfId, null, authorisedOptions(token));
  }

  getBookFromAnotherUserBookshelf(userId, bookshelfId, bookId):Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/` + userId+`/bookshelves/`+bookshelfId+`/`+bookId, null, authorisedOptions(token));
  }

  createBookSharingRequest(book):Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.post(`${BASE}/users/booksharingrequest`, book, authorisedOptions(token));
  }

  getMyRequests():Observable <any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/getmyrequests`, null, authorisedOptions(token));
  }

  allowRequest(id,date?):Observable<any>{
    console.log(date);
    let token = `${this.auth.access_token}`;
    return this.put(`${BASE}/users/allowrequest/`+id + `?expireDate=` + date, null, authorisedOptions(token));
  }

  refuseRequest(id,failReason):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.put(`${BASE}/users/refuserequest/`+id, failReason, authorisedOptions(token));
  }

  getMyRefusedRequests():Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/myrefusedrequests`, null, authorisedOptions(token));
  }

  getMySharedBooks():Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/mysharedbooks`, null, authorisedOptions(token));
  }

  getSharedBookById(id):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/mysharedbooks/`+id, null, authorisedOptions(token));
  }

  getPage(bookId, pageId):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/mysharedbooks/`+bookId+`/pages/`+pageId, null, authorisedOptions(token));
  }

  continueReading(bookId):Observable<any>{
    let token = `${this.auth.access_token}`;
    return this.get(`${BASE}/users/mysharedbooks/`+bookId+`/continuereading`, null, authorisedOptions(token));
  }
}
