import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { JobShadow } from './job-shadow.model';
import { JobShadowService } from './job-shadow.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-job-shadow',
    templateUrl: './job-shadow.component.html'
})
export class JobShadowComponent implements OnInit, OnDestroy {
jobShadows: JobShadow[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jobShadowService: JobShadowService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.jobShadowService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.jobShadows = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.jobShadowService.query().subscribe(
            (res: ResponseWrapper) => {
                this.jobShadows = res.json;
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
        this.registerChangeInJobShadows();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: JobShadow) {
        return item.id;
    }
    registerChangeInJobShadows() {
        this.eventSubscriber = this.eventManager.subscribe('jobShadowListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
