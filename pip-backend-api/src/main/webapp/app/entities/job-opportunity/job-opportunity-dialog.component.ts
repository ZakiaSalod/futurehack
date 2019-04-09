import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobOpportunity } from './job-opportunity.model';
import { JobOpportunityPopupService } from './job-opportunity-popup.service';
import { JobOpportunityService } from './job-opportunity.service';
import { BusinessUnit, BusinessUnitService } from '../business-unit';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-job-opportunity-dialog',
    templateUrl: './job-opportunity-dialog.component.html'
})
export class JobOpportunityDialogComponent implements OnInit {

    jobOpportunity: JobOpportunity;
    isSaving: boolean;

    businessunits: BusinessUnit[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobOpportunityService: JobOpportunityService,
        private businessUnitService: BusinessUnitService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.businessUnitService.query()
            .subscribe((res: ResponseWrapper) => { this.businessunits = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jobOpportunity.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobOpportunityService.update(this.jobOpportunity));
        } else {
            this.subscribeToSaveResponse(
                this.jobOpportunityService.create(this.jobOpportunity));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobOpportunity>) {
        result.subscribe((res: JobOpportunity) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobOpportunity) {
        this.eventManager.broadcast({ name: 'jobOpportunityListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBusinessUnitById(index: number, item: BusinessUnit) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-job-opportunity-popup',
    template: ''
})
export class JobOpportunityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobOpportunityPopupService: JobOpportunityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobOpportunityPopupService
                    .open(JobOpportunityDialogComponent as Component, params['id']);
            } else {
                this.jobOpportunityPopupService
                    .open(JobOpportunityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
