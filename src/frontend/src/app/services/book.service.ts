import {Injectable} from "@angular/core";

@Injectable()
export class BookService{

  private _bookId;
  private _bookName;

  get bookName() {
    return this._bookName;
  }

  set bookName(value) {
    this._bookName = value;
  }

  set bookId(value) {
    this._bookId = value;
  }
  get bookId() {
    return this._bookId;
  }
}
