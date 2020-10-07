import { Injectable } from '@angular/core';
import { Student } from '../models/student.model';
import { HttpClient } from '@angular/common/http';
import {
  BehaviorSubject,
  Observable,
  throwError,
  combineLatest,
  of,
} from 'rxjs';
import { catchError, retry, tap } from 'rxjs/operators';
import * as _ from 'lodash';
import { environment } from 'src/environments/environment';
import { Course } from '../models/course.model';
import { NetErr } from '../models/error.model';
import { ToastService } from './toast.service';
const BASE_PATH = environment.apiUrl;

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  constructor(private http: HttpClient, private toastService: ToastService) { }

  // TODO MODIFY THIS TO GETALLCOURSE BY TEACHER
  getAllCourses(): Observable<Course[]> {
    const url = BASE_PATH + 'courses';
    return this.http.get<Course[]>(url).pipe(catchError((e) => this.handleError(e)));
  }
  getCourse(name: string): Observable<Course> {
    const url = BASE_PATH + 'courses/' + name;
    return this.http.get<Course>(url).pipe(catchError((e) => this.handleError(e)));
  }

  updateCourse(course: Course, name: string): Observable<Course> {
    const url = BASE_PATH + 'courses/' + name;
    return this.http.patch<Course>(url, course).pipe(catchError((e) => this.handleError(e)));
  }

  addCourse(course: Course) {
    const url = BASE_PATH + 'courses/';
    return this.http.post<Course>(url, course).pipe(catchError((e) => this.handleError(e)));

  }
  enrollOne(course: Course, student: Student): Observable<void> {
    const url = BASE_PATH + 'courses/' + course.name + '/enrollOne';
    return this.http
      .post<void>(url, _.omit(student, 'links'))
      .pipe(catchError((e) => this.handleError(e)));
  }

  unEnrollMany(course: Course, studentIds: Array<string>): Observable<Array<boolean>> {
    const url = BASE_PATH + 'courses/' + course.name + '/unEnrollMany';
    return this.http
      .patch<Array<string>>(url, studentIds)
      .pipe(catchError((e) => this.handleError(e)));
  }



  private handleError(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    // this.toastService.error(err.message, err.status.toString())

    // window.alert(errorMessage);


    this.toastService.error(error.error.message, error.error.status.toString())
    console.log('HTTP ERROR', error);
    return of(null);
  }
}
