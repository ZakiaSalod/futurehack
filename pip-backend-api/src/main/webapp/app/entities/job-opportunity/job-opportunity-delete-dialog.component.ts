import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobOpportunity } from './job-opportunity.model';
import { JobOpportunityPopupService } from './job-opportunity-popup.service';
import { JobOpportunityService } from './job-opportunity.service';

@Component({
    selector: 'jhi-job-opportunity-delete-dialog',
    templateUrl: './job-opportunity-delete-dialog.component.html'
})
export class JobOpportunityDeleteDialogComponent {

    jobOpportunity: JobOpportunity;

    constructor(
        private jobOpportunityService: JobOpportunityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobOpportunityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobOpportunityListModification',
                content: 'Deleted an jobOpportunity'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-opportunity-delete-popup',
    template: ''
})
export class JobOpportunityDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobOpportunityPopupService: JobOpportunityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobOpportunityPopupService
                .open(JobOpportunityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
