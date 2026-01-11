import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {UserRequest} from "../../models/requests/user-request.model";
import {AuthService} from "../../services/auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environment/environment";

@Component({
  selector: 'app-register-page',
  standalone: false,
  templateUrl: './register-page.html',
  styleUrl: './register-page.css',
})
export class RegisterPage {

  userRequest: UserRequest = {} as UserRequest;
  repeatPassword: string = "";
  errorMessage: string | undefined;
  code: string = "";

  constructor(
      protected router: Router,
      private authService: AuthService,
  ) { }

  ngOnInit(): void {
      this.userRequest.role = environment.userRole;
  }

  async create() {
      if (this.userRequest.password != this.repeatPassword) {
          console.log("Passwords not matching!");
          return;
      }

      try {
          const userResponse = await this.authService.create(this.userRequest).toPromise();
          if (userResponse) {
              console.log("Successfully created a user!");
              console.log("Returning to login page...");
              this.router.navigate(['login']);
          } else {
              console.log("Some error occurred, response is null");
          }
      } catch (error) {
          console.log(error);
          if (error instanceof HttpErrorResponse) {
              this.errorMessage = error.error;
          }
      }
  }

}
