import {Component} from "@angular/core";
import {ApiService} from "../../../services/api.service";

@Component({
  selector:'app-share-request',
  templateUrl: './sharingrequest.component.html',
  styleUrls:['./sharingrequest.component.scss']
})
export class SharingRequestComponent {

  constructor(private api: ApiService){
  }

  public getMyRequests(){
    this.api.user.getMyRequests()
      .subscribe(
        response=>{

        }
      );
  }

}
