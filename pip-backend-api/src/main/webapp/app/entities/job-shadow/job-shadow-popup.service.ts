import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JobShadow } from './job-shadow.model';
import { JobShadowService } from './job-shadow.service';

@Injectable()
export class JobShadowPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jobShadowService: JobShadowService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.jobShadowService.find(id).subscribe((jobShadow) => {
                    jobShadow.datetime = this.datePipe
                        .transform(jobShadow.datetime, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.jobShadowModalRef(component, jobShadow);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jobShadowModalRef(component, new JobShadow());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jobShadowModalRef(component: Component, jobShadow: JobShadow): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jobShadow = jobShadow;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
