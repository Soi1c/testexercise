import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import R from "ramda";
import {BookService} from "../../../services/book.service";

const VIEW_STATES = {
  LIST:"SHOW_LIST_OF_BOOKS",
  BOOK:"SHOW_BOOK"
};

@Component({
  selector:'app-approvedBooks',
  templateUrl: './approvedBooks.component.html',
  styleUrls:['./approvedBooks.component.scss']
})
export class ApprovedBooksComponent implements OnInit{
  constructor(private api: ApiService, protected book: BookService){
  }
  state;
  public table = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'bookName','bookshelfName','expireDate']
  };

  errors = '';
  currentPage;
  public lines:string [];
  public page;
  public title;


  public isListShowable = () =>this.state === VIEW_STATES.LIST;
  public isBookShowable = () =>this.state === VIEW_STATES.BOOK;

  public getBookList = (filter?) =>{
    this.state = VIEW_STATES.LIST;
    this.api.user.getMySharedBooks()
      .subscribe(
        response=>{
          this.table.data = R.prop( response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response)
            : this.table.data;
          this.errors = '';
          if(this.table.data.length == 0){
            this.errors = 'У вас еще нет одобренных книг...'
          }
        },
        error => {}
      );
  }

  public showBook(data){
    this.book.bookId=data.book_id;
    this.book.bookName = data.bookName;
    this.continueReading();
  }

  public paginatorChanged = (data) =>
    this.getBookList({size: data.pageSize, page: data.pageIndex});

  public rowClicked = (data) => {
    this.showBook(data);
  }

  continueReading(){
    this.state = VIEW_STATES.BOOK;
    this.api.user.continueReading(this.book.bookId)
      .subscribe(
        response =>{
          this.currentPage = response.numeration;
          this.page = response.text;
          this.lines = this.page.split('\n');
        },
        error => {}
      );


  }

  getNextPage(){
    this.currentPage++;
    this.getPage(this.currentPage);
  }

  getPrevPage(){
    if(this.currentPage > 1)
      this.currentPage--;
    this.getPage(this.currentPage);
  }

  getPage(pageId){
    this.api.user.getPage(this.book.bookId, pageId)
      .subscribe(
        response=>{
          this.page = response.text;
          this.lines = this.page.split('\n');
        },
        error =>{}
      );
  }

  ngOnInit() {
    this.getBookList({size: 10, page: 1});

  }

}
