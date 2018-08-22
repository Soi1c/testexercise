import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import {BookService} from "../../../services/book.service";

@Component({
  selector: 'app-readbook',
  templateUrl: './readBook.component.html',
  styleUrls:['./readBook.component.scss']
})
export class ReadBookComponent implements OnInit{

  constructor(private api: ApiService, private book: BookService ){
  }
  currentPage;
  public lines:string [];
  public page;
  public title;

  continueReading(){
    this.api.book.continueReading(this.book.bookId)
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
    this.api.book.getPage(this.book.bookId, pageId)
      .subscribe(
        response=>{
          this.page = response.text;
          this.lines = this.page.split('\n');
        },
        error =>{}
      );
  }

  ngOnInit(){
    this.title = this.book.bookName;
    this.continueReading();
  }
}
