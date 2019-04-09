import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobShadow } from './job-shadow.model';
import { JobShadowPopupService } from './job-shadow-popup.service';
import { JobShadowService } from './job-shadow.service';
import { BusinessUnit, BusinessUnitService } from '../business-unit';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-job-shadow-dialog',
    templateUrl: './job-shadow-dialog.component.html'
})
export class JobShadowDialogComponent implements OnInit {

    jobShadow: JobShadow;
    isSaving: boolean;

    businessunits: BusinessUnit[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobShadowService: JobShadowService,
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
        if (this.jobShadow.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobShadowService.update(this.jobShadow));
        } else {
            this.subscribeToSaveResponse(
                this.jobShadowService.create(this.jobShadow));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobShadow>) {
        result.subscribe((res: JobShadow) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobShadow) {
        this.eventManager.broadcast({ name: 'jobShadowListModification', content: 'OK'});
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
    selector: 'jhi-job-shadow-popup',
    template: ''
})
export class JobShadowPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobShadowPopupService: JobShadowPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobShadowPopupService
                    .open(JobShadowDialogComponent as Component, params['id']);
            } else {
                this.jobShadowPopupService
                    .open(JobShadowDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
