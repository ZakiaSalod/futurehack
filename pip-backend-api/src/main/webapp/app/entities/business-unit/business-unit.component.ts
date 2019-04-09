import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { BusinessUnit } from './business-unit.model';
import { BusinessUnitService } from './business-unit.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-business-unit',
    templateUrl: './business-unit.component.html'
})
export class BusinessUnitComponent implements OnInit, OnDestroy {
businessUnits: BusinessUnit[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private businessUnitService: BusinessUnitService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.businessUnitService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.businessUnits = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.businessUnitService.query().subscribe(
            (res: ResponseWrapper) => {
                this.businessUnits = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInBusinessUnits();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: BusinessUnit) {
        return item.id;
    }
    registerChangeInBusinessUnits() {
        this.eventSubscriber = this.eventManager.subscribe('businessUnitListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
