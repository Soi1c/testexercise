///<reference path="../../../../services/api.service.ts"/>
import {Component} from "@angular/core";
import {ApiService} from "../../../../services/api.service";
import R from "ramda";

@Component({
  selector:'app-bookshelf',
  templateUrl: './singlebookshelf.component.html',
  styleUrls: ['./singlebookshelf.component.scss']
})
export class SinglebookshelfComponent{
  private chosen;
  constructor(private api: ApiService){
    this.showBookList({page: 0, size: 10});
  }
  public tableBooks = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'name', 'description', 'dateStart']
  };

  private showBookList= (filter?) =>{
    this.api.bookshelves.getBooksFromShelves()
      .subscribe(
        (response) => {
          console.log(response);
          this.tableBooks.data = R.prop('books', response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response.books)
            : this.tableBooks.data;

          console.log(this.tableBooks.data);

          this.tableBooks.size = R.propOr(this.tableBooks.size, 'totalElements', response);
          this.tableBooks.page = R.propOr(this.tableBooks.page, 'number', response);
          console.log(this.tableBooks.page);
        });
  }

  public rowClicked = (data) => {
    this.chosen = data;
  }
  public paginatorChanged = (data) =>
    this.showBookList({size: data.pageSize, page: data.pageIndex});
}
