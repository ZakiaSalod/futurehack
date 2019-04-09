import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessUnit } from './business-unit.model';
import { BusinessUnitService } from './business-unit.service';

@Component({
    selector: 'jhi-business-unit-detail',
    templateUrl: './business-unit-detail.component.html'
})
export class BusinessUnitDetailComponent implements OnInit, OnDestroy {

    businessUnit: BusinessUnit;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private businessUnitService: BusinessUnitService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBusinessUnits();
    }

    load(id) {
        this.businessUnitService.find(id).subscribe((businessUnit) => {
            this.businessUnit = businessUnit;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBusinessUnits() {
        this.eventSubscriber = this.eventManager.subscribe(
            'businessUnitListModification',
            (response) => this.load(this.businessUnit.id)
        );
    }
}
