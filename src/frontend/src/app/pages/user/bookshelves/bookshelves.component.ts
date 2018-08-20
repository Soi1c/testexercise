import {Component, OnInit, ViewChild} from "@angular/core";
import R from "ramda";
import {ApiService} from "../../../services/api.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { MdPaginator} from '@angular/material';


const VIEW_STATES = {
    LIST:"SHOW_LIST_OF_SHELFS",
    CREATION:"SHOW_CREATION",
    SINGLE: "SHOW_SINGLE_SHELF",
    BOOK:"SHOW_BOOK"
}

@Component({
  selector: 'app-bookshelves',
  templateUrl: './bookshelves.component.html',
  styleUrls:['./bookshelves.component.scss']
})
export class BookshelvesComponent implements OnInit{

  constructor(private api: ApiService){
    this.showList();
  }

  @ViewChild(MdPaginator) paginator: MdPaginator;


  public form: FormGroup;
  public state;
  public chosen;
  public table = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'name']
  };
  errors = '';


  public isListShowable = () =>this.state === VIEW_STATES.LIST;
  public isCreationShowable = () =>this.state === VIEW_STATES.CREATION;
  public isSingleShelfShowable = () =>this.state === VIEW_STATES.SINGLE;
  public isBookShowable = () =>this.state === VIEW_STATES.BOOK;

  public showList(){
    this.getList({page: 0, size: 10});
  }
  public showCreation(){
    this.form.reset();
    this.state = VIEW_STATES.CREATION;
  }
  public showSingleShelf(){
    this.state = VIEW_STATES.SINGLE;
  }
  public showBook(){
    this.state = VIEW_STATES.BOOK;
  }

  private getList = (filter?) => {
    this.state = VIEW_STATES.LIST;
    this.api.bookshelves.getBookShelves(filter)
      .subscribe(
        (response) => {
          this.table.data = R.prop( response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response)
            : this.table.data;
          this.table.size = this.table.data.length;
        //  this.table.data = new MatTableDataSource
          this.table.page = R.propOr(this.table.page, 'number', response);
        });
  }

  createBookShelf(){
    this.api.bookshelves.createBookShelf({name: this.form.value.bookshelfName})
      .subscribe(
        response =>{
          this.form.reset();
          this.showList();
        },
        error =>{
          this.errors = 'Ups';
        }
      );
  }

  public rowClicked = (data) => {
    this.chosen = data;
    this.showSingleShelf();
  }
  public paginatorChanged = (data) =>
    this.getList({size: data.pageSize, page: data.pageIndex});

  ngOnInit() {
    //this.table.data.paginator = this.paginator;
    this.form = new FormGroup({
      bookshelfName: new FormControl('',[Validators.required]),
    });
  }
}
