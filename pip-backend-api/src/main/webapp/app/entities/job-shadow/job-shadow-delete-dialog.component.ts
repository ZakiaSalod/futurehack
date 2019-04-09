import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobShadow } from './job-shadow.model';
import { JobShadowPopupService } from './job-shadow-popup.service';
import { JobShadowService } from './job-shadow.service';

@Component({
    selector: 'jhi-job-shadow-delete-dialog',
    templateUrl: './job-shadow-delete-dialog.component.html'
})
export class JobShadowDeleteDialogComponent {

    jobShadow: JobShadow;

    constructor(
        private jobShadowService: JobShadowService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobShadowService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobShadowListModification',
                content: 'Deleted an jobShadow'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-shadow-delete-popup',
    template: ''
})
export class JobShadowDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobShadowPopupService: JobShadowPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobShadowPopupService
                .open(JobShadowDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
