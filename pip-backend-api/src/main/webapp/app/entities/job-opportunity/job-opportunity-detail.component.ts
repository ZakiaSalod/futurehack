import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobOpportunity } from './job-opportunity.model';
import { JobOpportunityService } from './job-opportunity.service';

@Component({
    selector: 'jhi-job-opportunity-detail',
    templateUrl: './job-opportunity-detail.component.html'
})
export class JobOpportunityDetailComponent implements OnInit, OnDestroy {

    jobOpportunity: JobOpportunity;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobOpportunityService: JobOpportunityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobOpportunities();
    }

    load(id) {
        this.jobOpportunityService.find(id).subscribe((jobOpportunity) => {
            this.jobOpportunity = jobOpportunity;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobOpportunities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobOpportunityListModification',
            (response) => this.load(this.jobOpportunity.id)
        );
    }
}
