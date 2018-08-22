import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import R from "ramda";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MASKS} from "../../../libs/masks";

@Component({
  selector:'app-share-request',
  templateUrl: './sharingrequest.component.html',
  styleUrls:['./sharingrequest.component.scss']
})
export class SharingRequestComponent implements OnInit{

  constructor(private api: ApiService){
  }

  readonly VIEW_STATES = {
    LIST:"SHOW_LIST_OF_BOOKS",
    ACCEPT_OR_REJECT_VIEW:"ACCEPT_OR_REJECT_VIEW"
  };

  state;
  requestId = '';

  public form: FormGroup;

  public tableBooks = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'bookshelfName', 'bookName', 'askingUsername', 'allowed']
  };

  MASKS = MASKS;

  public isListShowable = () =>this.state === this.VIEW_STATES.LIST;
  public isApproveShowable = () =>this.state === this.VIEW_STATES.ACCEPT_OR_REJECT_VIEW;

  public getAllRequest(){
    this.state = this.VIEW_STATES.LIST;
    this.api.user.getMyRequests()
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

  public getRejectedRequest(){
    this.api.user.getMyRefusedRequests()
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
    this.state = this.VIEW_STATES.ACCEPT_OR_REJECT_VIEW;
    this.form.reset();
    this.requestId = data.id;
    this.form.controls.id.patchValue(this.requestId);
    this.form.controls.bookshelfName.patchValue(data.bookshelfName);
    this.form.controls.bookName.patchValue(data.bookName);
    this.form.controls.username.patchValue(data.askingUsername);

  }

  public accept(){
    let date = '';
    if(this.form.controls.expirationDate.value){
      date = this.form.controls.expirationDate.value;
    }
    this.api.user.allowRequest(this.requestId,date)
      .subscribe(
        response=>{
          this.getAllRequest();
        }
      );
  }

  public reject(){
    this.api.user.refuseRequest(this.requestId, {refuseDescription:this.form.controls.failReason.value})
      .subscribe(
        response=>{
          this.getAllRequest();
        }
      );
  }

  ngOnInit(){
    this.getAllRequest();
    this.form = new FormGroup({
      id: new FormControl('',[Validators.required]),
      bookshelfName: new FormControl('',[Validators.required]),
      bookName: new FormControl('',[Validators.required]),
      username: new FormControl('',[Validators.required]),
      expirationDate: new FormControl(''),
      failReason: new FormControl('')

  });

    this.form.controls.id.disable();
    this.form.controls.bookshelfName.disable();
    this.form.controls.bookName.disable();
    this.form.controls.username.disable();
  }
}
