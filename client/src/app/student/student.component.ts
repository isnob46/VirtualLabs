import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatSidenav } from "@angular/material/sidenav";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { BehaviorSubject, combineLatest, Observable, Subscription } from "rxjs";
import { map, skip, switchMap, tap } from "rxjs/operators";
import { Course } from "../models/course.model";
import { ToastService } from "../services/toast.service";
import { UtilsService } from "../services/utils.service";
import { StudentService } from "../services/student.service";
import { AuthService } from "../auth/auth.service";

@Component({
  selector: "app-student",
  templateUrl: "./student.component.html",
  styleUrls: ["./student.component.sass"],
})
export class StudentComponent implements OnInit {
  menuSubscription: Subscription;
  private _reloadSubject$: BehaviorSubject<void> = new BehaviorSubject(null);
  courses$: Observable<Course[]>;
  dialogSubscription: Subscription;
  isLoading = false;
  routeSubscription: Subscription;
  reloadCourseFromServiceSubscription: Subscription;
  studentId: string;
  @ViewChild(MatSidenav) sidenav: MatSidenav;
  constructor(
    private utilsService: UtilsService,
    public dialog: MatDialog,
    private toastService: ToastService,
    private studentService: StudentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // reload courses from service need when an other component modify course and request reload to service
    this.reloadCourseFromServiceSubscription = this.utilsService.reloadCurses$
      .pipe(skip(1))
      .subscribe(() => this._reloadSubject$.next(null));

    this.menuSubscription = this.utilsService.toggleMenu$.subscribe(() => {
      if (this.sidenav) this.sidenav.opened = !this.sidenav.opened;
    });

    this.courses$ = this.authService.currentUser$.pipe(
      switchMap((User) =>
        this._reloadSubject$.pipe(
          tap(() => (this.isLoading = true)),
          switchMap(() =>
            this.studentService.getCoursesByStudentId(User.userId)
          ),
          tap(() => (this.isLoading = false))
        )
      )
    );
  }
}