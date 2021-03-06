import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogCreateVmComponent } from '../student/vms-student/dialog-create-vm/dialog-create-vm.component';


export interface DialogData {
  countVcpus: number;
  countRam: number;
  countDisk: number;
  image: String;
  courseAc: String;
  teamName: String;
}

@Component({
  selector: 'app-open-vm',
  templateUrl: './open-vm.component.html',
  styleUrls: ['./open-vm.component.sass']
})
export class OpenVmComponent implements OnInit {

  path: String;

  constructor(
    public dialogRef: MatDialogRef<DialogCreateVmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
      dialogRef.disableClose = true;
  }

  ngOnInit(): void {
  }

  closeVm(): void {
    this.dialogRef.close();
  }

}
