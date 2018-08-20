///<reference path="../../../../services/api.service.ts"/>
import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../../services/api.service";
import R from "ramda";
import {BookshelvesComponent} from "../bookshelves.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserComponent} from "../../user.component";
import {BookService} from "../../../../services/book.service";


const VIEW_STATES = {
  LIST:"SHOW_LIST_OF_BOOKS",
  CREATION:"SHOW_CREATION",
  BOOK:"SHOW_BOOK"
}

@Component({
  selector:'app-bookshelf',
  templateUrl: './singlebookshelf.component.html',
  styleUrls: ['./singlebookshelf.component.scss']
})
export class SinglebookshelfComponent implements OnInit{
  private chosenBook;
  errors = '';
  uploadTitle = 'Загрузить книгу';
  public state;
  public formCreation: FormGroup;
  public form: FormGroup;
  public changeBookshelfForm: FormGroup;
  public bookId;
  newBookshelf;
  public dataBooks ={
    bookshelfList: []
  };

  constructor(private api: ApiService, protected  bookshelves: BookshelvesComponent
  ,protected userPage: UserComponent, protected book: BookService){
   // this.showBookList({page: 0, size: 10});
  }

  public tableBooks = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'name', 'description']
  };

  public isListShowable = () =>this.state === VIEW_STATES.LIST;
  public isCreationShowable = () =>this.state === VIEW_STATES.CREATION;
  public isBookShowable = () =>this.state === VIEW_STATES.BOOK;

  public showList(){
    this.form.controls.bookshelfId.disable();
    this.form.controls.bookshelfName.patchValue(this.bookshelves.chosen.name);
    this.form.controls.bookshelfId.patchValue(this.bookshelves.chosen.id);
    this.state = VIEW_STATES.LIST;
    this.showBookList();
  }

  public showCreation(){
    this.state = VIEW_STATES.CREATION;
    this.formCreation.reset();
  }

  public showBook(data: boolean){
    this.state = VIEW_STATES.BOOK;

    if(data){
      this.formCreation.controls.bookName.patchValue(this.chosenBook.name);
      this.formCreation.controls.bookDescription.patchValue(this.chosenBook.description);
    }
  }

  private showBookList= (filter?) =>{
    this.api.bookshelves.getBooksFromBookshelf(this.bookshelves.chosen.id)
      .subscribe(
        (response) => {
          this.tableBooks.data = R.prop( response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response)
            : this.tableBooks.data;
          this.tableBooks.size = R.propOr(this.tableBooks.size, 'totalElements', response);
          this.tableBooks.page = R.propOr(this.tableBooks.page, 'number', response);
        });
  }

  public rowClicked = (data) => {
    this.chosenBook = data;
    this.showBook(true);
  }

  public deleteBookshelf(){
    let id  = this.bookshelves.chosen.id;
    this.api.bookshelves.deleteBookshelfById(id)
      .subscribe(
        response => {

          this.bookshelves.showList();
        },
        error => {
          this.errors = 'Прости, удалишь в другой раз';
        }
      );
  }

  public createBook(){
    this.api.book.createBook({name:this.formCreation.value.bookName, description: this.formCreation.value.bookDescription
      , bookshelfId: this.bookshelves.chosen.id})
      .subscribe(
        response =>{
          this.bookId = response.id;
          this.showBook(false);
        },
        error =>{}
      );
  }

  public deleteBook(){
    this.api.book.deleteBook(this.chosenBook.id)
      .subscribe(
        response =>{
          this.showList();
        },
        error =>{
          this.showList();
        }
      );
  }

  public updateBookShelf(){


  }

  public fileUploaded(e){
    let element = e.srcElement;
    const file = element.files[0];
    if(!this.bookId){
      this.bookId = this.chosenBook.id;
    }
    if (file) {
      this.uploadTitle = 'Загружаю';
      this.api.book.uploadBook(this.bookId,file)
        .subscribe(
          () => {
            this.uploadTitle = 'Успех';
            setTimeout(this.uploadTitle = 'Загрузить книгу',2000);
          }
        );
    }

  }

  public saveBook(){
    if(!this.bookId){
      this.bookId = this.chosenBook.id;
    }
    this.api.book.updateBook(this.bookId, {name:this.formCreation.value.bookName, description: this.formCreation.value.bookDescription
      , bookshelfId: this.bookshelves.chosen.id}).subscribe(
        response =>{},
        error =>{}
    );
  }

  public readBook(){
    if(!this.bookId){
      this.bookId = this.chosenBook.id;
    }
    this.userPage.index = 0;
    this.book.bookId=this.bookId;
    this.book.bookName = this.chosenBook.name;

  }

  public changeBookshelf(){
  if(!this.bookId){
      this.bookId = this.chosenBook.id;
    }
    this.api.book.changeBookShelf(this.bookId, this.newBookshelf)
      .subscribe(
        response =>{

        },
        error =>{}
      );
    this.showList();
  }

  public showPrev(){
    this.bookshelves.showList();

  }

  public paginatorChanged = (data) =>
    this.showBookList({size: data.pageSize, page: data.pageIndex});

  public getBookshelvesList(){
    this.api.bookshelves.getBookShelves()
      .subscribe(
        (response) => {
          this.dataBooks.bookshelfList = response.map(shelf => {
            return {
              name: shelf.name,
              value: shelf.id
            }
          });

        });

  }

  ngOnInit(){
    this.formCreation = new FormGroup({
      bookName: new FormControl('',[Validators.required]),
      bookDescription: new FormControl('',[Validators.required]),
      upload: new FormControl('')
    });
    this.form = new FormGroup({
      bookshelfId: new FormControl('',[Validators.required]),
      bookshelfName: new FormControl('',[Validators.required])
    });
    this.changeBookshelfForm = new FormGroup({
      bookshelf: new FormControl('',[Validators.required])
    });
    this.uploadTitle = 'Загрузить книгу';
    this.showList();
    this.getBookshelvesList();
  }
}
