import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessUnit } from './business-unit.model';
import { BusinessUnitPopupService } from './business-unit-popup.service';
import { BusinessUnitService } from './business-unit.service';

@Component({
    selector: 'jhi-business-unit-delete-dialog',
    templateUrl: './business-unit-delete-dialog.component.html'
})
export class BusinessUnitDeleteDialogComponent {

    businessUnit: BusinessUnit;

    constructor(
        private businessUnitService: BusinessUnitService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.businessUnitService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'businessUnitListModification',
                content: 'Deleted an businessUnit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-business-unit-delete-popup',
    template: ''
})
export class BusinessUnitDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessUnitPopupService: BusinessUnitPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.businessUnitPopupService
                .open(BusinessUnitDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
