import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BusinessUnit } from './business-unit.model';
import { BusinessUnitPopupService } from './business-unit-popup.service';
import { BusinessUnitService } from './business-unit.service';
import { Industry, IndustryService } from '../industry';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-business-unit-dialog',
    templateUrl: './business-unit-dialog.component.html'
})
export class BusinessUnitDialogComponent implements OnInit {

    businessUnit: BusinessUnit;
    isSaving: boolean;

    industries: Industry[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private businessUnitService: BusinessUnitService,
        private industryService: IndustryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.industryService.query()
            .subscribe((res: ResponseWrapper) => { this.industries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.businessUnit.id !== undefined) {
            this.subscribeToSaveResponse(
                this.businessUnitService.update(this.businessUnit));
        } else {
            this.subscribeToSaveResponse(
                this.businessUnitService.create(this.businessUnit));
        }
    }

    private subscribeToSaveResponse(result: Observable<BusinessUnit>) {
        result.subscribe((res: BusinessUnit) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: BusinessUnit) {
        this.eventManager.broadcast({ name: 'businessUnitListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackIndustryById(index: number, item: Industry) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-business-unit-popup',
    template: ''
})
export class BusinessUnitPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessUnitPopupService: BusinessUnitPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.businessUnitPopupService
                    .open(BusinessUnitDialogComponent as Component, params['id']);
            } else {
                this.businessUnitPopupService
                    .open(BusinessUnitDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
