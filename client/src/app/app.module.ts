import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { StudentsComponent } from './teacher/students/students.component';
import { HomeComponent } from './home/home.component';
import { VmsComponent } from './teacher/vms/vms.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { LoginDialogComponent } from './auth/login-dialog/login-dialog.component';
import { TokenInterceptor } from './auth/token.interceptor';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { TeacherComponent } from './teacher/teacher.component';
import { CourseDashboard } from './teacher/course-dashboard/course-dashboard';
import { MatChipsModule } from '@angular/material/chips';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CourseDialogComponent } from './teacher/course-dialog/course-dialog.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RegisterDialogComponent } from './auth/register-dialog/register-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    StudentsComponent,
    HomeComponent,
    VmsComponent,
    PageNotFoundComponent,
    LoginDialogComponent,
    TeacherComponent,
    CourseDashboard,
    CourseDialogComponent,
    RegisterDialogComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MatToolbarModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatSidenavModule,
    MatTabsModule,
    MatListModule,
    MatButtonModule,
    MatTableModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatSortModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatChipsModule,
    FlexLayoutModule,
    MatSlideToggleModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent, RegisterDialogComponent],
})
export class AppModule { }
